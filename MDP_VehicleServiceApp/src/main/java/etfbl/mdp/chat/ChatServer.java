package etfbl.mdp.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.ChatMessage;

public class ChatServer {
	
    private static Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    

    public static void main(String[] args) {
    	System.setProperty("javax.net.ssl.keyStore", Constants.KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", Constants.KEY_STORE_PASSWORD);
		
        try {
        	
    		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    		ServerSocket serverSocket = ssf.createServerSocket(Constants.CHAT_PORT);
    		
            while (true) {
            	SSLSocket socket = (SSLSocket) serverSocket.accept();
          
                new Thread(() -> {
                    try {
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                        
                        String username = (String) in.readObject();
                        ClientHandler handler = new ClientHandler(username, socket, in, out);

                        clients.put(username, handler);
                        ServiceLogger.logger.info("User logged in: " + username);
                     

                        handler.listen();
                    } catch (Exception e) {
                    	ServiceLogger.logger.severe(e.getMessage());
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler {
        String username;
        SSLSocket socket;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientHandler(String username, SSLSocket socket, ObjectInputStream in, ObjectOutputStream out) {
            this.username = username;
            this.socket = socket;
            this.in = in;
            this.out = out;
        }

        void listen() {
            try {
                while (true) {
                    ChatMessage msg = (ChatMessage) in.readObject();
                    routeMessage(msg);
                }
            } catch (Exception e) {
              //  System.out.println(username + " disconnected");
                clients.remove(username);
            }
        }

        void send(ChatMessage msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
            	ServiceLogger.logger.severe(e.getMessage());
            }
        }
    }

    private static void routeMessage(ChatMessage msg) {
        if (msg.getTo() == null || msg.getTo().equalsIgnoreCase("ALL")) {
            return;
        } else {
            
            ClientHandler receiver = clients.get(msg.getTo());
            if (receiver != null) {
                receiver.send(msg);
            }
        }
    }
}

