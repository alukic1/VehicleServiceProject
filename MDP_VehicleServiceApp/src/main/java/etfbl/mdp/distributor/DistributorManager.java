package etfbl.mdp.distributor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.DistributorInfo;

public class DistributorManager {

	private static List<DistributorInfo> distributors = new ArrayList<>();
	
	public void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(Constants.DISTRIBUTOR_REGISTER_PORT)) {
              
                while (true) {
                    Socket socket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String request = in.readLine();

                    //REGISTER;NAZIV;PORT
                    if (request != null && request.startsWith("REGISTER")) {
                        String[] parts = request.split(";");
                        if (parts.length == 3) {
                            String name = parts[1];
                            int port = Integer.parseInt(parts[2]);
                            String ip = socket.getInetAddress().getHostAddress();

                            DistributorInfo info = new DistributorInfo(name, ip, port);
                            distributors.add(info);

                            ServiceLogger.logger.info("Registered new distributor: " + info);
                        }
                    }
                    socket.close();
                }
            } catch (IOException e) {
            	ServiceLogger.logger.severe(e.getMessage());
            }
        }).start();
    }

    public static List<DistributorInfo> getDistributors() {
        return distributors;
    }
}
