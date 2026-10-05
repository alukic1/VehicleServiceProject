package etfbl.mdp.data;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.Client;

public class ClientDataSource {
	
	public ArrayList<Client> clients = new ArrayList<>();
	
	private static ClientDataSource instance = null;
	
	
	private ClientDataSource() {
		loadClients();
	}
	
	public void loadClients() {
	
		File file = new File(Constants.FILE_PATH_CLIENTS);
		

		try {

		      
		        if (!file.exists()) {
		            file.createNewFile();
	
		            return;
		        }
		        if (!file.isFile()) {
		            throw new IllegalStateException("FILE_PATH_CLIENTS points to a directory, not a file: " + file.getAbsolutePath());
		        }

		        if (file.length() == 0L) {
		            clients = new ArrayList<>();
		            return;
		        }
		      
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
			clients = (ArrayList<Client>) decoder.readObject();		
			decoder.close();
			
			}
			catch(Exception e) {
				ServiceLogger.logger.severe(e.getMessage());
			}
	}
	
	public void saveClients() {
		try {
			File file = new File(Constants.FILE_PATH_CLIENTS);
			if(!file.exists())
				file.createNewFile();
			XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
			encoder.writeObject(clients);
			encoder.close();
		} catch (Exception e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
		
	}
	
	public static ClientDataSource getInstance() {
		if(instance == null) {
			instance = new ClientDataSource();
		}
		return instance;
	}
	

}
