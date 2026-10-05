package etfbl.mdp.gui.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ClientLogger;
import etfbl.mdp.models.Appointment;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;

public class PastAppointmentsController implements Initializable{

	private Stage stage;
    private Scene scene;
    
    @FXML
    private TableView<Appointment> appTable;
    @FXML
    private TableColumn appDateTime;
    @FXML
    private TableColumn appServiceType;
    @FXML
    private TableColumn appStatus;
    @FXML
    private TableColumn appDescription;
    
    private List<Appointment> appointments = new ArrayList<>();
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	try {
    		URL url = new URL(Constants.BASE_URL_APPOINTMENTS + "/" + LoginController.currentUsername);
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
            Type listType = new TypeToken<List<Appointment>>() {}.getType();
            appointments = gson.fromJson(jsonResponse.toString(), listType);
    	}
    	catch(Exception e) {
    		 ClientLogger.logger.severe(e.getMessage());
    	}
    /*	
    	appDateTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        LocalDateTime dt = app.getDateTime();
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:00");
		        String formatted = dt.format(formatter);
		        
		        return new SimpleStringProperty(formatted);
		    }
		});*/
    	appDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateTime"));
		
    	appServiceType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        String type = app.getType().getType();
		        
		        return new SimpleStringProperty(type);
		    }
		});
		
    	appStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        String status = app.getStatus().name();
		        
		        return new SimpleStringProperty(status);
		    }
		});
    	
    	appDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        String comment = app.getComment() == null ? "" : app.getComment();
		        
		        return new SimpleStringProperty(comment);
		    }
		});
    	
    	appTable.getItems().setAll(appointments);
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
