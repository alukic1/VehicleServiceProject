package etfbl.mdp.main;

import java.io.File;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.gui.controllers.LoginController;
import etfbl.mdp.logger.DistributorLogger;
import etfbl.mdp.server.DistributorServer;
import etfbl.mdp.service.DistributorRegister;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	private static final String PATH = "./resources";
	
	 public static void main(String[] args) throws Exception {
		 	Constants constants = new Constants();
		 	System.setProperty("java.security.policy", PATH + File.separator + "client_policyfile.txt");
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}

		 	launch(args);    
	    }

	@Override
	public void start(Stage stage) throws Exception {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/etfbl/mdp/gui/resources/login-view.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			DistributorLogger.logger.severe(e.getMessage());
		}
	}
}
