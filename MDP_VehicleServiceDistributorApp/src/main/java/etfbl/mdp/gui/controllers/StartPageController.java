package etfbl.mdp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import etfbl.mdp.constants.Constants;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartPageController implements Initializable {

	private Stage stage;
    private Scene scene;
    
    
    public void toSupplies(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/supplies-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void toOrders(ActionEvent event) {
    	try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/orders-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
	public void toUpdateSupplies(ActionEvent event) {
		try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/update-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(!Constants.initialized) {
			new Thread(() -> {
		        try {
		        	
		            DistributorServer server = new DistributorServer();
	
		            DistributorRegister reg = new DistributorRegister(LoginController.currentName, server.getPort());
		            reg.registerAtService();
	
		            server.start();
	
		            System.out.println("Dobavljač server pokrenut na portu " + server.getPort());
		            Constants.initialized = true;
	
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }).start();
			
		}
	}
}
