package etfbl.mdp.gui.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.distributor.DistributorManager;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.DistributorInfo;
import etfbl.mdp.models.VehiclePart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class OrderController implements Initializable{

	private Stage stage;
    private Scene scene;
    
    @FXML
    private TableView<VehiclePart> partsTable;
    @FXML
    private TableColumn partsCode;
    @FXML
    private TableColumn partsName;
    @FXML
    private TableColumn partsManufacturer;
    @FXML
    private TableColumn partsPrice;
    @FXML
    private TableColumn partsQuantity;
    @FXML
    private TableColumn partsDescription;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField codeField;
    @FXML
    private TextField quantityField;
    @FXML
    private ChoiceBox distributorChoice;
    
    private List<DistributorInfo> distributors;
    
    @Override
   	public void initialize(URL location, ResourceBundle resources) {
       	
    	List<String> distributorNames = new ArrayList<>();
    	distributors = DistributorManager.getDistributors();
    	
    	for(DistributorInfo d : distributors)
    		distributorNames.add(d.getName());
    	
    	ObservableList<String> observableList = FXCollections.observableArrayList(distributorNames);
    	distributorChoice.setItems(observableList);
    	
       	partsCode.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("code"));
       	partsName.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("name"));
       	partsManufacturer.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("manufacturer"));
       	partsPrice.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("price"));
       	partsQuantity.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("availableQuantity"));
       	partsDescription.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("description"));
       	
       	distributorChoice.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
           setParts(newValue.toString());
        });
       	
       
    }
    
    private void setParts(String d) {
    	DistributorInfo selected = null;
		for(DistributorInfo db : distributors) {
			if(db.getName().equals(d))
				selected = db;
		}	
		
		List<VehiclePart> parts = new ArrayList<>();
		
		if(selected != null) {
			try (Socket socket = new Socket(selected.getIp(), selected.getPort());
		             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
		            
					out.println("GET_OFFER");
		            
					Object obj = in.readObject();
		            if (obj instanceof List) {
		                parts = (List<VehiclePart>) obj;
		            }
		            partsTable.getItems().setAll(parts);
		            System.out.println("Server response: " + parts);

		        } catch (IOException | ClassNotFoundException e) {
		            e.printStackTrace();
		        }
		}
		
       	
    }
    
    public void order(ActionEvent event) {
		if(codeField.getText().length() <= 0 || quantityField.getText().length() <= 0)
			infoLabel.setText("All fields need to be filled.");
		else {
			int q = 0;
			try {
				q = Integer.parseInt(quantityField.getText());
			}
			catch(NumberFormatException e) {
				infoLabel.setText("Quantity needs to be an integer number.");
			}
			if(q <= 0)
				infoLabel.setText("Quantity needs to be a positive number.");
			
			else {
				
				DistributorInfo selected = null;
				for(DistributorInfo d : distributors) {
					if(d.getName().equals(distributorChoice.getValue()))
						selected = d;
				}
				if(selected != null) {
					try (Socket socket = new Socket(selected.getIp(), selected.getPort());
				             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
				            
				            out.println("ORDER;" + codeField.getText() + ";" + quantityField.getText());
	
				            
				            String response = in.readLine();
				            System.out.println("Server response: " + response);
	
				        } catch (IOException e) {
				        	ServiceLogger.logger.severe(e.getMessage());
				        }
				}
			}
		}
   	}
    
    
    public void goBack(ActionEvent event) {
		try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/startPage-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
        	ServiceLogger.logger.severe(ex.getMessage());
        }
	}
    
   
    
}
