package etfbl.mdp.gui.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.Client;
import etfbl.mdp.models.CreateInvoiceDto;
import etfbl.mdp.models.VehiclePart;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateInvoiceController implements Initializable{

	private Stage stage;
    private Scene scene;

    @FXML
    private Label appInfo;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField initialPriceField;
    @FXML
    private ListView<VehiclePart> partsList;
    
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
    	
    	if(AppointmentsController.selectedScheduled != null) {
    	/*	 LocalDateTime dt = AppointmentsController.selectedScheduled.getDateTime();
		     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:00");
		     String formatted = dt.format(formatter);*/
    		appInfo.setText(AppointmentsController.selectedScheduled.getClientUsername() + " " + AppointmentsController.selectedScheduled.getDateTime());
    	}
    	
    	List<VehiclePart> parts = getParts();
    	partsList.getItems().addAll(parts);
    	partsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
    	initialPriceField.textProperty().addListener((obs, oldValue, newValue) -> {
    	    if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
    	        initialPriceField.setText(oldValue);
    	    }
    	});
    	
    	
    }
    
    private List<VehiclePart> getParts(){
    	List<VehiclePart> parts = new ArrayList<>();
    	try {
    		URL url = new URL(Constants.BASE_URL_VEHICLE_PARTS);
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
            Type listType = new TypeToken<List<VehiclePart>>() {}.getType();
            parts = gson.fromJson(jsonResponse.toString(), listType);
    	}
    	catch(Exception e) {
    		ServiceLogger.logger.severe(e.getMessage());
    	}
    	return parts;
    }
    
    public void finish(ActionEvent event) {
    	ObservableList<VehiclePart> selectedItems = partsList.getSelectionModel().getSelectedItems();
  
    	List<VehiclePart> selectedParts = new ArrayList<>(selectedItems);  
    /*	if(selectedParts == null)
    		selectedParts = new ArrayList<>();
    	*/
    	//List<VehiclePart> selectedParts = partsList.getSelectionModel().getSelectedItems();
    	try {
    		BigDecimal price = null;
    		try {
    		 price = new BigDecimal(initialPriceField.getText());
    		 CreateInvoiceDto dto = new CreateInvoiceDto(AppointmentsController.selectedScheduled, selectedParts, price);
         	Gson gson = new Gson();
         	String json = gson.toJson(dto);
         	
         	URL url = new URL(Constants.BASE_URL_INVOICE);
         	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         	conn.setRequestMethod("POST");
         	conn.setDoOutput(true);
             conn.setRequestProperty("Content-Type", "application/json");
         	
             conn.getOutputStream().write(json.getBytes());
             conn.getOutputStream().flush();
             conn.getOutputStream().close();
             
             if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
             	 throw new RuntimeException("HTTP POST Request Failed with Error code : "
                          + conn.getResponseCode());
             }
         	
             goBack(event);
    		}
    		catch(NumberFormatException ex) {
    			infoLabel.setText("Initial price not valid. Try again.");
    		}
        	
    	}
    	catch(MalformedURLException e) {
    		ServiceLogger.logger.severe(e.getMessage());
    	}
    	catch(Exception e) {
    		ServiceLogger.logger.severe(e.getMessage());
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
        	ServiceLogger.logger.severe(ex.getMessage());
        }
    }
}
