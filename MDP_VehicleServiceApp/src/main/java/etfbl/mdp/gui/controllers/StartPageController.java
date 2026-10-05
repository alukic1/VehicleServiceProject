package etfbl.mdp.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartPageController {

	private Stage stage;
    private Scene scene;
    
    public void toClients(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/clients-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void toAppointments(ActionEvent event) {
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
    
    public void toVehicleParts(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/vehicleParts-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void toChat(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/chat-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
