package mdp.invoice;

import java.rmi.Remote;
import java.rmi.RemoteException;

import etfbl.mdp.models.Invoice;


public interface InvoiceInterface extends Remote{

	public void saveInvoice(Invoice invoice) throws RemoteException;
}
