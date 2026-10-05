package etfbl.mdp.gui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import etfbl.mdp.distributor.DistributorManager;
import etfbl.mdp.models.DistributorInfo;
import etfbl.mdp.models.VehiclePart;
import etfbl.mdp.services.VehiclePartService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class VehiclePartsController implements Initializable{

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
    
    private VehiclePartService service = new VehiclePartService();
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    
    	
    	partsCode.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("code"));
    	partsName.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("name"));
    	partsManufacturer.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("manufacturer"));
    	partsPrice.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("price"));
    	partsQuantity.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("availableQuantity"));
    	partsDescription.setCellValueFactory(new PropertyValueFactory<VehiclePart, String>("description"));
    	
    	ArrayList<VehiclePart> parts = service.getVehicleParts();
    	partsTable.getItems().setAll(parts);
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
    
    public void toOrder(ActionEvent event) {
		try{
            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/orderPage-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();}
        catch(Exception ex){
            ex.printStackTrace();
        }
	}
}
