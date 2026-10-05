package etfbl.mdp.main;

import etfbl.mdp.api.AppointmentAPI;
import etfbl.mdp.api.ClientAPI;
import etfbl.mdp.api.InvoiceAPI;
import etfbl.mdp.constants.Constants;
import etfbl.mdp.distributor.ConfirmedOrder;
import etfbl.mdp.distributor.DistributorManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage stage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/startPage-view.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		System.out.println("pocetak maina");
		Constants constants = new Constants();
		System.out.println("navodno ucitane constants");
		ClientAPI clientApi = new ClientAPI();
		AppointmentAPI appApi = new AppointmentAPI();
		InvoiceAPI invoiceApi = new InvoiceAPI();
	
		DistributorManager distributorManager = new DistributorManager();
		distributorManager.start();
		
		ConfirmedOrder confirmOrder = new ConfirmedOrder();
		confirmOrder.start();
		launch(args);
	}

}
