package etfbl.mdp.services;

import java.util.ArrayList;

import etfbl.mdp.data.ClientDataSource;
import etfbl.mdp.models.Client;
import etfbl.mdp.models.LoginDataDto;

public class ClientService {

	ClientDataSource clientDataSource = ClientDataSource.getInstance();
	
	public ArrayList<Client> getClients(){
		return clientDataSource.clients;
	}
	
	public ArrayList<String> getAllUsernames(){
		ArrayList<String> usernames = new ArrayList<>();
		for(Client c : clientDataSource.clients) {
			if(c.isActive())
				usernames.add(c.getUsername());
		}
		return usernames;
	}
	
	public Client getByUsername(String username) {
		for(Client c : clientDataSource.clients) {
			if(c.getUsername().equals(username))
				return c;
		}
		return null;
	}
	
	public boolean add(Client client) {
		Client exists = getByUsername(client.getUsername());
		if(exists == null) {
			client.setPassword(EncryptionService.encrypt(client.getPassword()));
			if(clientDataSource.clients.add(client)) {
				clientDataSource.saveClients();
					return true;
			}
		}
		return false;
	}
	
	public boolean login(LoginDataDto data) {
		data.setPassword(EncryptionService.encrypt(data.getPassword()));
		Client client = getByUsername(data.getUsername());
		if(client != null) {
			if(client.getPassword().equals(data.getPassword()) && client.isActive())
				return true;
		}
		return false;
	}
	
	public Client toggleBlock(String username) {
		Client client = getByUsername(username);
		if(client != null) {
			client.setActive(!client.isActive());
			clientDataSource.saveClients();
			return client;
		}
		return null;
	}
	
	public boolean remove(String username) {
		int index = clientDataSource.clients.indexOf(new Client(username));
		if(index >= 0) {
			clientDataSource.clients.remove(index);
			clientDataSource.saveClients();
			return true;
		}
		return false;
	}
}

