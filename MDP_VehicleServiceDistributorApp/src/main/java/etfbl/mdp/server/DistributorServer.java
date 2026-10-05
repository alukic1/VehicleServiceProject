package etfbl.mdp.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.gui.controllers.LoginController;
import etfbl.mdp.logger.DistributorLogger;
import etfbl.mdp.models.Order;
import etfbl.mdp.models.VehiclePart;
import etfbl.mdp.orders.ConnectionFactoryUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class DistributorServer {
	
	private String name;
	private ServerSocket serverSocket;
	private ServerSocket updateServerSocket;
    private static final String GECKO_DRIVER_PATH = "./geckodriver.exe"; 
    private static final int NUM_ELEMENTS = 10;
    public static int UPDATE_PORT;
	public static List<VehiclePart> parts = new ArrayList<>();
	private Connection connection;
    private Channel channel;
	
	public DistributorServer() throws IOException, TimeoutException {
        this.name = LoginController.currentName;
        this.serverSocket = new ServerSocket(0);
        this.updateServerSocket = new ServerSocket(0);
        UPDATE_PORT = updateServerSocket.getLocalPort();
        parse();
        System.out.println("Parsirano : " + parts.size());
        System.out.println(parts.get(0));
        
        connection = ConnectionFactoryUtil.createConnection();
        channel = connection.createChannel();
        channel.queueDeclare(Constants.QUEUE_NAME + "_" + this.name, false, false, false, null);
    }
	
	public static void main(String[] args) {
		Constants constants = new Constants();
		DistributorServer dServer = null;
		try{
			dServer = new DistributorServer();
			//dServer.start();
		}
		catch(Exception e)
		{
			DistributorLogger.logger.severe(e.getMessage());
		}
		
	}

	public int getPort() {
        return serverSocket.getLocalPort();
    }
	
	public void start() {

        new Thread(() -> { 
        	while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleRequest(socket)).start();
            } catch (IOException e) {
            	DistributorLogger.logger.severe(e.getMessage());
            }
        }}).start();
       
        new Thread(() -> { 
        	while (true) {
            try {
                Socket socket = updateServerSocket.accept();
                new Thread(() -> updateSupplies(socket)).start();
            } catch (IOException e) {
            	DistributorLogger.logger.severe(e.getMessage());
            }
        }}).start();
    }
	
	private void handleRequest(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        		 ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream())) {

            String msg = in.readLine();
            //ORDER;CODE;QUANTITY
            if (msg != null && msg.startsWith("ORDER")) {
            	String parts[] = msg.split(";");
            	if(parts.length == 3) {
	            	String code = parts[1];
	            	int quantity = 0; 
	           		quantity = Integer.parseInt(parts[2]);
	            	if(quantity != 0) {
	            		System.out.println(name + " primio narudzbu: " + code + " " + quantity);
	            	}
	                
	                Order order = new Order(code, quantity);
	                                
	                ByteArrayOutputStream bos = new ByteArrayOutputStream();
	                ObjectOutputStream oos = new ObjectOutputStream(bos);
	                oos.writeObject(order);
	                oos.flush();
	                byte[] orderBytes = bos.toByteArray();
	                
	                String queueName = Constants.QUEUE_NAME + "_" + this.name;
	                channel.basicPublish("", queueName, null, orderBytes);
	                System.out.println("Order sent: " + order);
	                
	                out.println("ORDER_RECEIVED;" + code);
            	}
            } else if (msg != null && msg.equals("GET_OFFER")) {
            	objOut.writeObject(parts);
                objOut.flush();
            }
            else {
            	out.println("REQUEST_ERROR");
            }
        } catch (IOException e) {
        	DistributorLogger.logger.severe(e.getMessage());
        }
    }
	
	private void updateSupplies(Socket socket) {
		try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
	             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

	            Object obj = in.readObject();
	            if (obj instanceof VehiclePart order) {
	                System.out.println("Primio narudžbu: " + order);
	                parts.add(order);
	                out.writeObject("SUPPLIES_UPDATED: " + order.getCode());	                
	                out.flush();
	            }

	        } catch (Exception e) {
	        	DistributorLogger.logger.severe(e.getMessage());
	        }
	}
	
	private static void parse(){
		 System.setProperty("webdriver.gecko.driver", GECKO_DRIVER_PATH); 

	        FirefoxOptions options = new FirefoxOptions();
	       
	        options.addArguments("-headless"); 
	        options.addPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0");

	        WebDriver driver = null;
	        
		Random rand = new Random();
		try {
			driver = new FirefoxDriver(options);

            driver.get(Constants.PARSING_URL);

            Thread.sleep(8000);

            String pageSource = driver.getPageSource();

            Document document = Jsoup.parse(pageSource, Constants.PARSING_URL); 
		
            Elements products = document.select("div.brand-products[data-product-block]");
			

			for(Element p : products) {
				try {
					
					Element codeElement = p.selectFirst("div > div.description > div.nr");
					String code = (codeElement != null) ? codeElement.text() : "";
					
					Element nameElement = p.selectFirst("div > div.description > div.prod_link > a");
					
					String name = (nameElement != null) ? nameElement.text() : "";
					
					String names[] = name.split(" ");
					
					name = names[0] + " " + names[1] + " " + names[2];
					
	                String manufacturer = (names.length >= 3) ?  names[1] : "";	                 
	                 
	                Element priceElement = p.selectFirst("div > div.right_side > div.price_block > div.price");
	                 String priceStr = (priceElement != null) ? priceElement.text() : "";
	                    BigDecimal price = new BigDecimal(0.0);
	                    
	                 if (!priceStr.isEmpty()) {
	                        priceStr = priceStr.replace("€", "").replace(",", ".").trim();
	                        try {
	                            price = new BigDecimal(priceStr);
	                        } catch (NumberFormatException ignored) {}
	                 }
	                 
	                 Element imageElement = p.selectFirst("div > div.image.js-image-block > span");
	                    String imageUrl = (imageElement != null) ? imageElement.attr("data-original") : "";

	                    Element descElement = p.selectFirst("div > div.description > div.prod_link > a > span");
	                    String desc = (descElement != null) ? descElement.text() : "";
	                    
	                    int quantity = rand.nextInt(15) + 1;
	                    
	                    VehiclePart part = new VehiclePart(code, name, manufacturer, price, quantity , desc + " Image: " + imageUrl);
	                    parts.add(part);
				}
				catch(Exception e) {
					DistributorLogger.logger.severe(e.getMessage());
				}
			}
		}
		catch(Exception e) {
			DistributorLogger.logger.severe(e.getMessage());
		}
		
	}
}
