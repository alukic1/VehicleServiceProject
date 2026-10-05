package mdp.invoice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import etfbl.mdp.models.Invoice;
import mdp.constants.Constants;
import mdp.logger.AccountingLogger;

public class InvoiceServer implements InvoiceInterface{

	private static final String PATH = "./resources";
	
	public InvoiceServer() throws RemoteException {

	}
	public void saveInvoice(Invoice invoice) throws RemoteException {
		
		try {
			 File folder = new File(Constants.INVOICE_FOLDER);
		        if (!folder.exists()) {
		            folder.mkdirs(); 
		        }
			
			String timestamp = LocalDateTime.now()
			        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			FileOutputStream fileOut = new FileOutputStream(
			        Constants.INVOICE_FOLDER + File.separator + "invoice_" + timestamp + ".out"
			);
		
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(invoice);
			out.close();
		} catch (Exception e) {
			AccountingLogger.logger.severe(e.getMessage());
			//e.printStackTrace();
		}
	}

	public static void main(String args[]){
		Constants constants = new Constants();
		System.setProperty("java.security.policy", PATH + File.separator + "server_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			InvoiceServer server = new InvoiceServer();
			InvoiceInterface stub = (InvoiceInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("AccountingService", stub);
			System.out.println("Server started.");
		} catch (RemoteException ex) {
			AccountingLogger.logger.severe(ex.getMessage());
			//ex.printStackTrace();
		}
	}
}
