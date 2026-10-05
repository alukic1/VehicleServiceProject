package etfbl.mdp.main;

import etfbl.mdp.chat.MulticastMessage;
import etfbl.mdp.constants.Constants;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	@Override
	public void start(Stage stage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/login-view.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Constants constants = new Constants();
		MulticastMessage multicast = new MulticastMessage();
		multicast.start();
		launch(args);

	}

}
