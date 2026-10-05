package etfbl.mdp.constants;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import etfbl.mdp.logger.ServiceLogger;

public class Constants {
	
	public static final String CONFIG_PATH = "C:\\Users\\Ana\\eclipse-workspace\\MDP_VehicleServiceApp\\resources\\config.properties";
	
	public static String FILE_PATH_CLIENTS = "";
	public static String FILE_PATH_APPOINTMENTS = "";
	public static String REDIS_INSTANCE_NAME = "";
	public static String INVOICE_FOLDER = "";
	public static String REDIS_HOST = "";
	public static String BASE_URL_CLIENTS = "";
	public static String BASE_URL_APPOINTMENTS = "";
	public static String BASE_URL_VEHICLE_PARTS = "";
	public static String BASE_URL_INVOICE = "";
	public static int CHAT_PORT = 0;
	public static String CHAT_HOST = "";
	public static String KEY_STORE_PATH = "";
	public static String KEY_STORE_PASSWORD = "";
	public static int DISTRIBUTOR_REGISTER_PORT = 0;
	public static String ORDER_HOST = "";
	public static int CONFIRMED_ORDER_PORT = 0;
	
	static {
	    System.err.println(">>> static init of Constants loaded");
	}
	static {
		System.out.println("Static Constants");
		 try {
			 Properties prop = new Properties();
			prop.load(new FileInputStream(CONFIG_PATH));
			
			FILE_PATH_CLIENTS = prop.getProperty("FILE_PATH_CLIENTS");
			FILE_PATH_APPOINTMENTS = prop.getProperty("FILE_PATH_APPOINTMENTS");
			REDIS_INSTANCE_NAME = prop.getProperty("REDIS_INSTANCE_NAME");
			INVOICE_FOLDER = prop.getProperty("INVOICE_FOLDER");
			REDIS_HOST = prop.getProperty("REDIS_HOST");
			BASE_URL_CLIENTS = prop.getProperty("BASE_URL_CLIENTS");
			BASE_URL_APPOINTMENTS = prop.getProperty("BASE_URL_APPOINTMENTS");
			BASE_URL_VEHICLE_PARTS = prop.getProperty("BASE_URL_VEHICLE_PARTS");
			BASE_URL_INVOICE = prop.getProperty("BASE_URL_INVOICE");
			CHAT_PORT = Integer.parseInt(prop.getProperty("CHAT_PORT"));
			CHAT_HOST = prop.getProperty("CHAT_HOST");
			KEY_STORE_PATH = prop.getProperty("KEY_STORE_PATH");
			KEY_STORE_PASSWORD = prop.getProperty("KEY_STORE_PASSWORD");
			DISTRIBUTOR_REGISTER_PORT = Integer.parseInt(prop.getProperty("DISTRIBUTOR_REGISTER_PORT"));
			ORDER_HOST = prop.getProperty("ORDER_HOST");
			CONFIRMED_ORDER_PORT = Integer.parseInt(prop.getProperty("CONFIRMED_ORDER_PORT"));
			
			if("".equals(FILE_PATH_CLIENTS) || "".equals(FILE_PATH_APPOINTMENTS) || "".equals(REDIS_INSTANCE_NAME)
					|| "".equals(INVOICE_FOLDER) || "".equals(REDIS_HOST) 
					|| "".equals(BASE_URL_CLIENTS) || "".equals(BASE_URL_APPOINTMENTS)
					|| "".equals(BASE_URL_VEHICLE_PARTS) || "".equals(BASE_URL_INVOICE)
					|| CHAT_PORT == 0 || "".equals(CHAT_HOST) || "".equals(KEY_STORE_PATH) ||
					"".equals(KEY_STORE_PASSWORD) || DISTRIBUTOR_REGISTER_PORT == 0 ||
					"".equals(ORDER_HOST) || CONFIRMED_ORDER_PORT == 0)
				throw new Exception("Nedostaje podatak u properties fajlu.");

		}
		catch(Exception e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
	}
	
	private static String require(Properties p, String key) {
        String v = p.getProperty(key);
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("Missing or blank property: " + key);
        }
        return v;
    }

}
