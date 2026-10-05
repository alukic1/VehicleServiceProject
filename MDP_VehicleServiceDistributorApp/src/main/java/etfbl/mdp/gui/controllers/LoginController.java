package etfbl.mdp.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	private Stage stage;
    private Scene scene;

    @FXML
    private TextField nameField;
    @FXML
    private Label infoLabel;
    
    public static String currentName = null;
    
    public void login(ActionEvent event) {
    	if(nameField.getText().length() <= 0) {
    		infoLabel.setText("Name field is required.");
    		return;
    	}
    	currentName = nameField.getText();
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
