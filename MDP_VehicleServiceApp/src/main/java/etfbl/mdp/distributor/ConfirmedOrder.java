package etfbl.mdp.distributor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.DistributorInfo;
import etfbl.mdp.models.VehiclePart;
import etfbl.mdp.services.VehiclePartService;

public class ConfirmedOrder {
	
	private VehiclePartService service = new VehiclePartService();
	
	public void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(Constants.CONFIRMED_ORDER_PORT)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    
                    new Thread(() -> handleClient(socket)).start();
                }
            } catch (IOException e) {
            	ServiceLogger.logger.severe(e.getMessage());
            }
        }).start();
    }
	
	private void handleClient(Socket socket) {
		 try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
      
	            Object obj = ois.readObject();
	            if (obj instanceof VehiclePart) {
	                VehiclePart part = (VehiclePart) obj;
	                service.savePart(part);
	                System.out.println("Primljen VehiclePart: " + part);
	            } 

	        } catch (IOException | ClassNotFoundException e) {
	        	ServiceLogger.logger.severe(e.getMessage());
	        } finally {
	            try { socket.close(); } catch (IOException ignored) {}
	        }
	}
}
