package etfbl.mdp.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.Appointment;

public class AppointmentDataSource {

	public ArrayList<Appointment> appointments = new ArrayList<>();
	
	private static AppointmentDataSource instance = null;
	
	private AppointmentDataSource() {
		super();
		loadAppointments();
	}
	
	public static AppointmentDataSource getInstance() {
		if(instance == null)
			instance = new AppointmentDataSource();
		return instance;
	}
	
	public void loadAppointments() {	
		try {
			File file = new File(Constants.FILE_PATH_APPOINTMENTS);
			
				
			if(!file.exists()) {
				file.createNewFile();
				return;
			}
			
			if (!file.isFile()) {
	            throw new IllegalStateException("FILE_PATH_CLIENTS points to a directory, not a file: " + file.getAbsolutePath());
	        }

	        
	        if (file.length() == 0L) {
	            appointments = new ArrayList<>();
	            return;
	        }
			
			FileInputStream fileIn = new FileInputStream(Constants.FILE_PATH_APPOINTMENTS);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			appointments = (ArrayList<Appointment>) in.readObject();
			in.close();
		}
		catch(Exception e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
	}
	
	public void saveAppointments() {
		try {
			FileOutputStream fileOut = new FileOutputStream(Constants.FILE_PATH_APPOINTMENTS);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(appointments);
			out.close();
		} catch (Exception e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
	}
}
