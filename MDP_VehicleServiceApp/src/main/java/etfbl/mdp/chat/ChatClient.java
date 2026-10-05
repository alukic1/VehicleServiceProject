package etfbl.mdp.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.gui.controllers.ChatController;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.ChatMessage;

public class ChatClient {

	private SSLSocket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;
    public Map<String, List<String>> conversations = new HashMap<>();
    
    private static ChatClient instance = null;
    
    {
    	System.setProperty("javax.net.ssl.trustStore", Constants.KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", Constants.KEY_STORE_PASSWORD);
       
    }
    public static ChatClient getInstance() {
    	
    	if(instance == null)
    		instance = new ChatClient();
    	return instance;
    }
    
    private ChatClient(){
    	try {
    	SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		this.socket = (SSLSocket) sf.createSocket(Constants.CHAT_HOST, Constants.CHAT_PORT);

        this.username = "SERVICE";

        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        
        out.writeObject(username);
        out.flush();

        
        new Thread(() -> {
            try {
                while (true) {
                	 ChatMessage msg = (ChatMessage) in.readObject();
                	 handleIncomingMessage(msg);
                    System.out.println(msg.getFrom() + " -> " + msg.getTo() + ": " + msg.getText());
                }
            } catch (Exception e) {
            	ServiceLogger.logger.severe(e.getMessage());
         
            }
        }).start();
    	}
    	catch(Exception e)
    	{
    		ServiceLogger.logger.severe(e.getMessage());
    	}
    }
    
    private void handleIncomingMessage(ChatMessage msg) {
        String from = msg.getFrom();
        String text = msg.getText();

        conversations.putIfAbsent(from, new ArrayList<>());
        conversations.get(from).add(from + ": " + text);
        
    }
    
    public void sendMessage(String to, String text){
    	try {
        ChatMessage msg = new ChatMessage(username, to, text);
        out.writeObject(msg);
        out.flush();

        conversations.putIfAbsent(to, new ArrayList<>());
        conversations.get(to).add("Me: " + text);
    	}
    	catch(Exception e) {
    		ServiceLogger.logger.severe(e.getMessage());
    	}
    }
}
