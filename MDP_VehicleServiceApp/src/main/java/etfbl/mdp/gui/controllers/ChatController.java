package etfbl.mdp.gui.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import etfbl.mdp.chat.ChatClient;
import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ChatController  implements Initializable{
	private Stage stage;
    private Scene scene;
    
    @FXML
    private ListView<String> usersList;
    @FXML
    private ListView<String> messagesList;
    @FXML
    private TextArea newMessage;
    @FXML
    private Label infoLabel;
    
    private String selectedUser = null;
    
    private ChatClient chatClient = ChatClient.getInstance();
    

    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	if(selectedUser != null)
    		selectedUser = null;
    	
    	List<String> users = getUsernames();
    	usersList.getItems().setAll(users);
    	
    	usersList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
    	    selectedUser = newVal;  
    	    if (selectedUser != null) {
    	    	messagesList.getItems().setAll(chatClient.conversations.get(selectedUser));
    	    }
    	});	
    	
    }
   
    
    public void sendMsg(ActionEvent event) {
    	if(selectedUser == null) {
    		infoLabel.setText("You need to selected user to send message to."); 
    		return;
    	}
    	
    	if(newMessage.getText().length() <= 0) {
    		infoLabel.setText("You cannot send an empty message.");
    		return;
    	}
    	
    	chatClient.sendMessage(selectedUser, newMessage.getText());
    
    }
    
    private List<String> getUsernames(){
    	ArrayList<String> result = new ArrayList<>();
    	try {
    		URL url = new URL(Constants.BASE_URL_CLIENTS + "/usernames");
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
            Type listType = new TypeToken<List<String>>() {}.getType();
            result = gson.fromJson(jsonResponse.toString(), listType);
    	}
    	catch(Exception e) {
    		ServiceLogger.logger.severe(e.getMessage());
    	}
    
    	return result;
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
