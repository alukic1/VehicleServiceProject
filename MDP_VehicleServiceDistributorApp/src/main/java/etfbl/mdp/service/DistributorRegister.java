package etfbl.mdp.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.DistributorLogger;

public class DistributorRegister {
	private String name;
    private int port;

    public DistributorRegister(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public void registerAtService() {
        try (Socket socket = new Socket(Constants.REGISTER_HOST, Constants.REGISTER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("REGISTER;" + name + ";" + port);
            DistributorLogger.logger.info("Distributor " + name + " registered at service.");
           
        } catch (IOException e) {
        	DistributorLogger.logger.severe(e.getMessage());
        }
    }
}
