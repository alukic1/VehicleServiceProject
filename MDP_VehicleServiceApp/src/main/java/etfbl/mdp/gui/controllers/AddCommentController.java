package etfbl.mdp.gui.controllers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCommentController implements Initializable{
	private Stage stage;
    private Scene scene;

    @FXML
    private TextArea commentField;
    @FXML
    private Label appInfo;
    @FXML
    private Label infoLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
    	if(AppointmentsController.selectedScheduled != null) {
    	/*	 LocalDateTime dt = AppointmentsController.selectedScheduled.getDateTime();
		     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:00");
		     String formatted = dt.format(formatter);*/
    		appInfo.setText(AppointmentsController.selectedScheduled.getClientUsername() + " " + AppointmentsController.selectedScheduled.getDateTime());
    	}
    }
    
    public void goBack(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/appointments-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void addComment(ActionEvent event) {
    	if(commentField.getText().length() <= 0)
    	{
    		infoLabel.setText("Comment field cannot be empty.");
    		return;
    	}
    	try {
    		URL url = new URL(Constants.BASE_URL_APPOINTMENTS + "/" + AppointmentsController.selectedScheduled.getId() + "/" + "comment");
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("PUT");
    		conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			
			String json = commentField.getText();
			conn.getOutputStream().write(json.getBytes());
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
			
			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				infoLabel.setText("Error while adding a comment.");
			}
			goBack(event);
    	}
    	catch(Exception e) {
    		ServiceLogger.logger.severe(e.getMessage());
    	}
    }

}
