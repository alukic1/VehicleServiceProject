package etfbl.mdp.gui.controllers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.google.gson.Gson;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ClientLogger;
import etfbl.mdp.models.Appointment;
import etfbl.mdp.models.MakeAppointmentDto;
import etfbl.mdp.models.ServiceType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RequestAppointmentController implements Initializable {

	private Stage stage;
    private Scene scene;
    
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> timePicker;
    @FXML
    private ComboBox<String> typePicker;
    @FXML
    private Label infoLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	    
    	datePicker.setDayCellFactory(picker -> new DateCell() {
    	    @Override
    	    public void updateItem(LocalDate date, boolean empty) {
    	        super.updateItem(date, empty);
    	        if (empty || date == null) {
    	            setDisable(true);
    	        } else if (date.toEpochDay() < LocalDate.now().toEpochDay()) {
    	            setDisable(true);
    	            setStyle("-fx-background-color: #cccccc;");
    	        }
    	    }
    	});
    	
        for (int i = 0; i < 24; i++) {
            timePicker.getItems().add(String.format("%02d:00", i));
        }
        timePicker.getSelectionModel().select(LocalTime.now().getHour() + ":00");
        
        typePicker.getItems().add(ServiceType.REGULAR_SERVICE.getType());
        typePicker.getItems().add(ServiceType.REPAIR.getType());
    }
    
    public void request(ActionEvent event) {
    	if(datePicker.getValue() == null || timePicker.getValue() == null || timePicker.getValue().isEmpty()
    			|| typePicker.getValue() == null || typePicker.getValue().isEmpty()) {
    		infoLabel.setText("All fields need to be filled.");
    		return;
    	}
    	
    	LocalDate date = datePicker.getValue();
    	String time = timePicker.getValue(); 
    	LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));

    	LocalDateTime dateTime = LocalDateTime.of(date, localTime);
    
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:00");
        String formatted = dateTime.format(formatter);
        
    	ServiceType type = typePicker.getValue().equals(ServiceType.REGULAR_SERVICE.getType()) ? ServiceType.REGULAR_SERVICE : ServiceType.REPAIR;
    
    	MakeAppointmentDto app = new MakeAppointmentDto(LoginController.currentUsername, formatted, type);
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(app);
    	
    	try {
    		URL url = new URL(Constants.BASE_URL_APPOINTMENTS);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("POST");
    		conn.setRequestProperty("Content-Type", "application/json");
    		conn.setDoOutput(true);
    		
    		conn.getOutputStream().write(json.getBytes());
    		conn.getOutputStream().flush();
    		conn.getOutputStream().close();
    		
    		if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
    			throw new RuntimeException("HTTP POST Request Failed with Error code : "
                        + conn.getResponseCode());
    		}
    		
    		goBack(event);
    	}
    	catch(Exception e) {
    		 ClientLogger.logger.severe(e.getMessage());
    	}
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
