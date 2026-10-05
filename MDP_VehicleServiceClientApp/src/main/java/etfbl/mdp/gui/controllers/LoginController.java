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

public class LoginController {

	private Stage stage;
    private Scene scene;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label infoLabel;
    
    public static String currentUsername = null;
    
    public void login(ActionEvent event) {
   
    	String enteredUsername = username.getText();
		String enteredPassword = password.getText();
		String jsonData = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", enteredUsername, enteredPassword);
		
		if(enteredUsername.length() < 1 || enteredPassword.length() < 1)
			infoLabel.setText("All fields are required!");
		else {
			try {
				URL url = new URL(Constants.LOGIN_URL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST"); 
				conn.setRequestProperty("Content-Type", "application/json");
				JSONObject input = new JSONObject(jsonData);
				OutputStream os = conn.getOutputStream();
				os.write(input.toString().getBytes());
				os.flush();
				
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					infoLabel.setText("Wrong username or password. Try again.");
					 ClientLogger.logger.warning("Unsuccessful login: " + username);
				}
				else {
					currentUsername = enteredUsername;
					try{
			            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/startPage-view.fxml"));
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
    
    public void toRegister(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/register-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
        	 ClientLogger.logger.severe(ex.getMessage());
        }
    }
}
