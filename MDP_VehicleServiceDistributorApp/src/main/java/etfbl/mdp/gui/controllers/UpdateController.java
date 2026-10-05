package etfbl.mdp.gui.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.DistributorLogger;
import etfbl.mdp.models.VehiclePart;
import etfbl.mdp.server.DistributorServer;
import etfbl.mdp.service.DistributorRegister;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateController{

	private Stage stage;
    private Scene scene;
    
    @FXML
    private TextField codeField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField mnfrField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextArea descField;
    @FXML
    private Label infoLabel;
    
    
    public void add(ActionEvent event) {
    	if(codeField.getText().length() <= 0 || nameField.getText().length() <= 0 ||
    			mnfrField.getText().length() <= 0 || priceField.getText().length() <= 0 ||
    			quantityField.getText().length() <= 0 || descField.getText().length() <= 0)
    		infoLabel.setText("All fields need to be filled.");
    	else {
    	
    		try(Socket socket = new Socket(Constants.UPDATE_SUPPLY_HOST, DistributorServer.UPDATE_PORT);
    				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    	             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
    			
    			BigDecimal price = null;
    			int quantity;
    			
    			try {
    				price = new BigDecimal(priceField.getText());
    				quantity = Integer.parseInt(quantityField.getText());
    				VehiclePart order = new VehiclePart(codeField.getText(), nameField.getText(), mnfrField.getText(), price, quantity, descField.getText());
    		    			
    		   		out.writeObject(order);
    		   		out.flush();
        
    		   		Object response = in.readObject();
    		   		System.out.println("Server response: " + response);
    		   		
    		   		goBack(event);
    			}
    			catch(NumberFormatException ex) {
    				DistributorLogger.logger.severe(ex.getMessage());
    			}        
                
    		}
    		catch(Exception e) {
    			DistributorLogger.logger.severe(e.getMessage());
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
            ex.printStackTrace();
        }
    }
}
