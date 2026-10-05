package etfbl.mdp.gui.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.DistributorLogger;
import etfbl.mdp.models.Order;
import etfbl.mdp.models.OrderStatus;
import etfbl.mdp.models.VehiclePart;
import etfbl.mdp.models.Invoice;
import etfbl.mdp.orders.ConnectionFactoryUtil;
import etfbl.mdp.server.DistributorServer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import mdp.invoice.InvoiceInterface;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;

public class OrdersController implements Initializable{

	private Stage stage;
    private Scene scene;
    
    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn orderCode;
    @FXML
    private TableColumn orderQuantity;
    @FXML
    private TableColumn orderStatus;
    @FXML
    private Label infoLabel;
    
    private List<Order> orders = new ArrayList<>();
    
    private Order selected = null;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	orderCode.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("partCode"));
    	orderQuantity.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("quantity"));
    	orderStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> cellData) {
		        Order app = cellData.getValue();
		        String type = app.getStatus().toString();
		        
		        return new SimpleStringProperty(type);
		    }
		});
    	
    	try {
	    	Connection connection = ConnectionFactoryUtil.createConnection();
	        Channel channel = connection.createChannel();
			channel.queueDeclare(Constants.QUEUE_NAME + "_" + LoginController.currentName, false, false, false, null);
			
	
	        GetResponse response;
	        try {
				while ((response = channel.basicGet(Constants.QUEUE_NAME + "_" + LoginController.currentName, true)) != null) {
				    byte[] body = response.getBody();
				    ByteArrayInputStream bis = new ByteArrayInputStream(body);
				    ObjectInputStream ois;
					try {
						ois = new ObjectInputStream(bis);
						Order order = (Order) ois.readObject();
						boolean hasEnough = false;
						for(VehiclePart p : DistributorServer.parts) {
							if(p.getCode().equals(order.getPartCode()) && p.getAvailableQuantity() >= order.getQuantity())
							{
								hasEnough = true;
								p.setAvailableQuantity(p.getAvailableQuantity() - order.getQuantity());
							}
						}
						if(hasEnough)
							orders.add(order);
					} catch (IOException e) {
						DistributorLogger.logger.severe(e.getMessage());
					}
				    
				}
			} catch (ClassNotFoundException | IOException e) {
				
				DistributorLogger.logger.severe(e.getMessage());
			}
	
	        channel.close();
	        connection.close();
    	}
    	catch(Exception e) {
    		DistributorLogger.logger.severe(e.getMessage());
    	}
    	
    	ordersTable.getItems().setAll(orders);
    	ordersTable.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Order st = row.getItem();
                    selected = st;

                }
            });
            return row;
        });
    }
    
    public void goBack(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/startPage-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void confirmOrder(ActionEvent event) {
    	if(selected == null) {
    		infoLabel.setText("You need to select the order.");
    	}
    	else {
    		selected.setStatus(OrderStatus.CONFIRMED);
    		
    		VehiclePart soldPart = null;
    		for(VehiclePart p : DistributorServer.parts) {
    			if(p.getCode().equals(selected.getPartCode()))
    					soldPart = new VehiclePart(p.getCode(), p.getName(), p.getManufacturer(), p.getPrice(), selected.getQuantity(), p.getDescription());
    		}
    		if(soldPart != null) {
    			try (Socket socket = new Socket(Constants.REGISTER_HOST, Constants.CONFIRMED_ORDER_PORT);
    		             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
    		             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
    					
    		            oos.writeObject(soldPart); 
    		            oos.flush();

    		        } catch (IOException e) {
    		        	DistributorLogger.logger.severe(e.getMessage());
    		        }
    		}

    		Invoice invoice = generateInvoice(selected);
    		try {
    			String name = "AccountingService";
    			Registry registry = LocateRegistry.getRegistry(1099);
    			InvoiceInterface accountingService =  (InvoiceInterface)registry.lookup(name);
    			accountingService.saveInvoice(invoice);
    		}
    		catch(Exception e) {
				DistributorLogger.logger.severe(e.getMessage());
    		}
    	}
    }
    
    public void rejectOrder(ActionEvent event) {
    	if(selected == null) {
    		infoLabel.setText("You need to select the order.");
    	}
    	else {
    		selected.setStatus(OrderStatus.REJECTED);
    	}
    }
    
    private Invoice generateInvoice(Order order) {
    	BigDecimal price = new BigDecimal(0.0);
    	for(VehiclePart p : DistributorServer.parts) {
			if(p.getCode().equals(order.getPartCode()))
					price = p.getPrice();
		
		}
    	
    	Invoice invoice = new Invoice(order.getPartCode(), price, order.getQuantity());
    	
    	return invoice;
    }
}
