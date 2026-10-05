package etfbl.mdp.gui.controllers;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.Client;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ClientsController implements Initializable {
	
	private Stage stage;
    private Scene scene;

    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn clientUsername;
    @FXML
    private TableColumn clientName;
    @FXML
    private TableColumn clientLastName;
    @FXML
    private TableColumn clientVehicle;
    @FXML
    private TableColumn clientAddress;
    @FXML
    private TableColumn clientPhoneNumber;
    @FXML
    private TableColumn clientEmail;
    @FXML
    private TableColumn clientToggleBlock;
    @FXML
    private Label infoLabel;
    
    private List<Client> clients = null;
    public static Client selectedClient = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(selectedClient != null) {
        	selectedClient=null;
        }
        
        try {
        	URL url = new URL(Constants.BASE_URL_CLIENTS);
        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        	conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP GET Request Failed with Error code : "
                        + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonResponse.append(line);
            }
            conn.disconnect();
            
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Client>>() {}.getType();
            clients = gson.fromJson(jsonResponse.toString(), listType);
        }
        catch(Exception e) {
        	ServiceLogger.logger.severe(e.getMessage());
        }
        
        
        clientUsername.setCellValueFactory(new PropertyValueFactory<Client, String>("username"));
        clientName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        clientLastName.setCellValueFactory(new PropertyValueFactory<Client, String>("lastName"));
        clientVehicle.setCellValueFactory(new PropertyValueFactory<Client, String>("vehicleModel"));
        clientAddress.setCellValueFactory(new PropertyValueFactory<Client, String>("address"));
        clientPhoneNumber.setCellValueFactory(new PropertyValueFactory<Client, String>("phoneNumber"));
        clientEmail.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));

        
        clientToggleBlock.setCellFactory(col -> new TableCell<Client, Void>() {
            private final Button button = new Button();

            {
                // Event handler za klik na dugme
                button.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    if (client != null) {
                        	if(toggleBlock(client.getUsername())) {
                        		client.setActive(!client.isActive());
                                updateButtonText(client);
                        	}
                    }
                });
            }

            
            private void updateButtonText(Client client) {
                if (client.isActive()) {
                    button.setText("Block");
                    button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                } else {
                    button.setText("Activate");
                    button.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Client client = getTableView().getItems().get(getIndex());
                    updateButtonText(client);
                    setGraphic(button);
                }
            }
        });

        clientsTable.getItems().setAll(clients);

        clientsTable.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Client st = row.getItem();
                    selectedClient = st;

                }
            });
            return row;
        });
    }
    
    private boolean toggleBlock(String username) {
    	boolean success = false;
    	
    	try {
    		URL url = new URL (Constants.BASE_URL_CLIENTS +  "/" + username);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("PUT");
    		conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            	success = true;
            conn.disconnect();
    	}
    	catch(Exception e) {
    		ServiceLogger.logger.severe(e.getMessage());
    	}
    	
    	
    	return success;
    }
    
    public void deleteClient(ActionEvent e) {
    	if(selectedClient != null) {
    		try {
    			URL url = new URL(Constants.BASE_URL_CLIENTS + "/" + selectedClient.getUsername());
    			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    			conn.setRequestMethod("DELETE");
    			conn.setRequestProperty("Content-Type", "application/json");
    			conn.setDoOutput(true);
    			
    			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
    				infoLabel.setText("Client successfully deleted.");
    		}
    		catch(Exception ex) {
    			ServiceLogger.logger.severe(ex.getMessage());
    		}
    	}
    	else {
    		infoLabel.setText("You need to select client to delete.");
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
