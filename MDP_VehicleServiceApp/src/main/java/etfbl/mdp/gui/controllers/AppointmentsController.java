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
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.Appointment;
import etfbl.mdp.models.Client;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AppointmentsController implements Initializable{

	private Stage stage;
    private Scene scene;
    
    @FXML
    private TableView<Appointment> scheduledTable;
    @FXML
    private TableColumn scheduledId;
    @FXML
    private TableColumn scheduledClient;
    @FXML
    private TableColumn scheduledVehicle;
    @FXML
    private TableColumn scheduledDateTime;
    @FXML
    private TableColumn scheduledServiceType;
    @FXML
    private TableColumn scheduledComment;
    @FXML
    private Label scheduledInfoLabel;
    
    private List<Appointment> scheduledAppointments = new ArrayList<>();
    public static Appointment selectedScheduled = null;
    
    
    @FXML
    private TableView<Appointment> requestsTable;
    @FXML
    private TableColumn reqId;
    @FXML
    private TableColumn reqClient;
    @FXML
    private TableColumn reqVehicle;
    @FXML
    private TableColumn reqDateTime;
    @FXML
    private TableColumn reqServiceType;
    @FXML
    private Label reqInfoLabel;
    
    private List<Appointment> reqAppointments = new ArrayList<>();
    public static Appointment selectedReq = null;
    
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		if(selectedScheduled != null)
			selectedScheduled = null;
		if(selectedReq != null)
			selectedReq = null;
		
		getScheduled();
		getRequests();
		
		scheduledId.setCellValueFactory(new PropertyValueFactory<Appointment, String>("id"));
		scheduledClient.setCellValueFactory(new PropertyValueFactory<Appointment, String>("clientUsername"));
		
		scheduledVehicle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        Client client = getClientByUsername(app.getClientUsername());
		        String vehicle = client.getVehicleModel();
		        return new SimpleStringProperty(vehicle);
		    }
		});
		/**
		scheduledDateTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        LocalDateTime dt = app.getDateTime();
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:00");
		        String formatted = dt.format(formatter);
		        
		        return new SimpleStringProperty(formatted);
		    }
		});*/
		
		scheduledDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateTime"));
		
		scheduledServiceType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        String type = app.getType().getType();
		        
		        return new SimpleStringProperty(type);
		    }
		});
		
		scheduledComment.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        String comment = app.getComment();
		        
		        return new SimpleStringProperty(comment);
		    }
		});
		
		scheduledTable.getItems().setAll(scheduledAppointments);
		scheduledTable.setRowFactory(tv -> {
            TableRow<Appointment> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Appointment st = row.getItem();
                    selectedScheduled = st;

                }
            });
            return row;
        });
		
		
		reqId.setCellValueFactory(new PropertyValueFactory<Appointment, String>("id"));
		reqClient.setCellValueFactory(new PropertyValueFactory<Appointment, String>("clientUsername"));
		
		reqVehicle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        Client client = getClientByUsername(app.getClientUsername());
		        String vehicle = client.getVehicleModel();
		        return new SimpleStringProperty(vehicle);
		    }
		});
		
		/*
		reqDateTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        LocalDateTime dt = app.getDateTime();
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:00");
		        String formatted = dt.format(formatter);
		        
		        return new SimpleStringProperty(formatted);
		    }
		});*/
		
		reqDateTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dateTime"));
		
		reqServiceType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> cellData) {
		        Appointment app = cellData.getValue();
		        String type = app.getType().getType();
		        
		        return new SimpleStringProperty(type);
		    }
		});
		
		requestsTable.getItems().setAll(reqAppointments);
		requestsTable.setRowFactory(tv -> {
            TableRow<Appointment> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Appointment st = row.getItem();
                    selectedReq = st;

                }
            });
            return row;
        });
	}

	private void getScheduled() {
		try {
			URL url = new URL(Constants.BASE_URL_APPOINTMENTS);
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
            scheduledAppointments = gson.fromJson(jsonResponse.toString(), listType);
		}
		catch(Exception e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
	}
	
	private void getRequests() {
		try {
			URL url = new URL(Constants.BASE_URL_APPOINTMENTS + "/requests");
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
            reqAppointments = gson.fromJson(jsonResponse.toString(), listType);
		}
		catch(Exception e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
	}
	
	
	private Client getClientByUsername(String username) {
		Client client = null;
		try {
			URL url = new URL(Constants.BASE_URL_CLIENTS + "/" + username);
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
            Type type = new TypeToken<Client>() {}.getType();
            client = gson.fromJson(jsonResponse.toString(), type);
		}
		catch(Exception e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
		return client;
	}
	
	
	public void addComment(ActionEvent event) {
    	if(selectedScheduled != null) {
    		try{
                Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/addComment-view.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();}
            catch(Exception ex){
            	ServiceLogger.logger.severe(ex.getMessage());
            }
    	}
    	else {
    		scheduledInfoLabel.setText("You need to select the appointment.");
    	}
    }
	
	public void finishAppointment(ActionEvent event) {
		if(selectedScheduled != null) {
			try{
	            Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/createInvoice-view.fxml"));
	            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();}
	        catch(Exception ex){
	        	ServiceLogger.logger.severe(ex.getMessage());
	        }
    	}
    	else {
    		scheduledInfoLabel.setText("You need to select the appointment.");
    	}
    }
	
	public void confirmAppointment(ActionEvent event) {
		if(selectedReq != null) {
    		try {
    			URL url = new URL(Constants.BASE_URL_APPOINTMENTS + "/" + selectedReq.getId());
    			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    			conn.setRequestMethod("PUT");
    			conn.setRequestProperty("Content-Type", "application/json");
    			conn.setDoOutput(true);
    			
    			String json = "true";

    			conn.getOutputStream().write(json.getBytes());
    			conn.getOutputStream().flush();
    			conn.getOutputStream().close();
    			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                    throw new RuntimeException("HTTP PUT Request Failed with Error code : "
                            + conn.getResponseCode());
                }
    			conn.disconnect();
    		}
    		catch(Exception e) {
    			ServiceLogger.logger.severe(e.getMessage());
    		}
    	}
    	else {
    		reqInfoLabel.setText("You need to select the appointment.");
    	}
	}
	
	public void rejectAppointment(ActionEvent event) {
		if(selectedReq != null) {
			try {
    			URL url = new URL(Constants.BASE_URL_APPOINTMENTS + "/" + selectedReq.getId());
    			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    			conn.setRequestMethod("PUT");
    			conn.setRequestProperty("Content-Type", "application/json");
    			conn.setDoOutput(true);
    			
    			String json = "false";

    			conn.getOutputStream().write(json.getBytes());
    			conn.getOutputStream().flush();
    			conn.getOutputStream().close();
    			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                    throw new RuntimeException("HTTP PUT Request Failed with Error code : "
                            + conn.getResponseCode());
                }
    			conn.disconnect();
    		}
    		catch(Exception e) {
    			ServiceLogger.logger.severe(e.getMessage());
    		}
    	}
    	else {
    		reqInfoLabel.setText("You need to select the appointment.");
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
        	ServiceLogger.logger.severe(ex.getMessage());
        }
	}
}
