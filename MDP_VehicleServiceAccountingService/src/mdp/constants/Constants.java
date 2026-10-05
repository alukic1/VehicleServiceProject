package mdp.constants;

import java.io.FileInputStream;
import java.util.Properties;

public class Constants {

	public static final String CONFIG_PATH = "./resources/config.properties";
	
	public static String INVOICE_FOLDER = "";
	{
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(CONFIG_PATH));
			
			INVOICE_FOLDER = prop.getProperty("INVOICE_FOLDER");
			
			if("".equals(INVOICE_FOLDER));
				throw new Exception("Nedostaje podatak u properties fajlu.");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
