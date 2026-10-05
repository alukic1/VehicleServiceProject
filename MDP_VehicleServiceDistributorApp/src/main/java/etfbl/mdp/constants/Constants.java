package etfbl.mdp.constants;

import java.io.FileInputStream;
import java.util.Properties;

import etfbl.mdp.logger.DistributorLogger;

public class Constants {

	public static final String CONFIG_PATH = "./resources/config.properties";
	
	public static String PARSING_URL = "";
	public static int REGISTER_PORT = 0;
	public static String REGISTER_HOST = "";
	public static int UPDATE_SUPPLY_PORT = 0;
	public static String UPDATE_SUPPLY_HOST = "";
	public static String MQ_HOST = "";
	public static String MQ_USERNAME = "";
	public static String MQ_PASSWORD = "";
	public static String QUEUE_NAME = "";
	public static int CONFIRMED_ORDER_PORT = 0;
	
	public static boolean initialized = false;
	{
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(CONFIG_PATH));
			
			PARSING_URL = prop.getProperty("PARSING_URL");
			REGISTER_PORT = Integer.parseInt(prop.getProperty("REGISTER_PORT"));
			REGISTER_HOST = prop.getProperty("REGISTER_HOST");
			UPDATE_SUPPLY_PORT = Integer.parseInt(prop.getProperty("UPDATE_SUPPLY_PORT"));
			UPDATE_SUPPLY_HOST = prop.getProperty("UPDATE_SUPPLY_HOST");
			MQ_HOST = prop.getProperty("MQ_HOST");
			MQ_USERNAME = prop.getProperty("MQ_USERNAME");
			MQ_PASSWORD = prop.getProperty("MQ_PASSWORD");
			QUEUE_NAME = prop.getProperty("QUEUE_NAME");
			CONFIRMED_ORDER_PORT = Integer.parseInt(prop.getProperty("CONFIRMED_ORDER_PORT"));
			
			if("".equals(PARSING_URL) || REGISTER_PORT == 0 || "".equals(REGISTER_HOST) || UPDATE_SUPPLY_PORT == 0 ||
					"".equals(UPDATE_SUPPLY_HOST) || "".equals(MQ_HOST) || "".equals(MQ_USERNAME) ||
					"".equals(MQ_PASSWORD) || "".equals(QUEUE_NAME) || CONFIRMED_ORDER_PORT == 0);
				throw new Exception("Missing data in properties file.");
		}
		catch(Exception e) {
			DistributorLogger.logger.severe(e.getMessage());
		}
	}
}
