package etfbl.mdp.constants;

import java.io.FileInputStream;
import java.util.Properties;

import etfbl.mdp.logger.ClientLogger;

public class Constants {

	public static final String CONFIG_PATH = "./resources/config.properties";
	
	public static String BASE_URL_CLIENTS = "";
	public static String LOGIN_URL = "";
	public static String BASE_URL_APPOINTMENTS = "";
	public static String MULTICAST_HOST = "";
	public static int MULTICAST_PORT = 0;
	public static String KEY_STORE_PATH = "";
	public static String KEY_STORE_PASSWORD = "";
	public static String CHAT_HOST = "";
	public static int CHAT_PORT = 0;
	
	{
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(CONFIG_PATH));
			
			BASE_URL_CLIENTS = prop.getProperty("BASE_URL_CLIENTS");
			LOGIN_URL = prop.getProperty("LOGIN_URL");
			BASE_URL_APPOINTMENTS = prop.getProperty("BASE_URL_APPOINTMENTS");
			MULTICAST_HOST = prop.getProperty("MULTICAST_HOST");
			MULTICAST_PORT = Integer.parseInt(prop.getProperty("MULTICAST_PORT"));
			KEY_STORE_PATH = prop.getProperty("KEY_STORE_PATH");
			KEY_STORE_PASSWORD = prop.getProperty("KEY_STORE_PASSWORD");
			CHAT_HOST = prop.getProperty("CHAT_HOST");
			CHAT_PORT = Integer.parseInt(prop.getProperty("CHAT_PORT"));
			
			System.out.println("ucitano u constants: " + BASE_URL_CLIENTS);
			
			if("".equals(BASE_URL_CLIENTS) || "".equals(BASE_URL_APPOINTMENTS) || "".equals(LOGIN_URL)
					|| "".equals(MULTICAST_HOST) || MULTICAST_PORT == 0 || "".equals(KEY_STORE_PATH) ||
					"".equals(KEY_STORE_PASSWORD) || "".equals(CHAT_HOST) || CHAT_PORT == 0)
				throw new Exception("Missing data in properties file.");
		}
		catch(Exception e) {
			 ClientLogger.logger.severe(e.getMessage());
		}
	}
}
