package etfbl.mdp.gui.controllers;

import java.net.URL;

import org.json.JSONObject;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ClientLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.net.HttpURLConnection;

public class RegisterController {

	private Stage stage;
    private Scene scene;
    
    @FXML
    private TextField name;
    @FXML
    private TextField lastName;
    @FXML
    private TextField vehicleModel;
    @FXML
    private TextField address;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label infoLabel;
	
    public void register(ActionEvent event) {
    	String enteredName = name.getText(); String enteredLastName = lastName.getText();
    	String enteredVehicleModel = vehicleModel.getText();
    	String enteredAddress = address.getText(); String enteredPhoneNum = phoneNumber.getText();
    	String enteredEmail = email.getText(); 
    	String enteredUsername = username.getText();
		String enteredPassword = password.getText();
		
    	if(enteredName.length() < 1 || enteredLastName.length() < 1 ||
    			enteredVehicleModel.length() < 1 || enteredAddress.length() < 1 ||
    			enteredPhoneNum.length() < 1 || enteredEmail.length() < 1 ||
    			enteredUsername.length() < 1 || enteredPassword.length() < 1)
    		infoLabel.setText("All fields are required!");
    	else {
    		try {
    			String jsonData = String.format(
    				    "{\"name\":\"%s\",\"lastName\":\"%s\",\"vehicleModel\":\"%s\",\"address\":\"%s\",\"phoneNumber\":\"%s\",\"email\":\"%s\",\"username\":\"%s\",\"password\":\"%s\"}",
    				    enteredName, enteredLastName, enteredVehicleModel, enteredAddress, enteredPhoneNum, enteredEmail, enteredUsername, enteredPassword
    				);
    			URL url = new URL(Constants.BASE_URL_CLIENTS);
    			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    			conn.setDoOutput(true);
				conn.setRequestMethod("POST"); 
				conn.setRequestProperty("Content-Type", "application/json");
				JSONObject input = new JSONObject(jsonData);
				
				OutputStream os = conn.getOutputStream();
				os.write(input.toString().getBytes());
				os.flush();
				
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					infoLabel.setText("Username already in use. Try again.");
				}
				else {
					try{
			            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/login-view.fxml"));
			            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			            scene = new Scene(root);
			            stage.setScene(scene);
			            stage.show();}
			        catch(Exception ex){
			        	 ClientLogger.logger.severe(ex.getMessage());
			        }
				}

    		}
    		catch(Exception e) {
    			 ClientLogger.logger.severe(e.getMessage());
    		}
    	}
    }
	
	public void goBack(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/login-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }	
}
