package etfbl.mdp.models;

import java.io.Serializable;
import java.util.Objects;

public class Client implements Serializable{
	
	private String name;
	private String lastName;
	private String vehicleModel;
	private String address;
	private String phoneNumber;
	private String email;
	private String username;
	private String password;
	private boolean active;
	
	public Client() {
		super();
	}
	
	public Client(String username) {
		super();
		this.username = username;
	}

	public Client(String name, String lastName, String vehicleModel, String address, String phoneNumber, String email,
			String username, String password) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.vehicleModel = vehicleModel;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.username = username;
		this.password = password;
		this.active = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	

	@Override
	public String toString() {
		return "Client [name=" + name + ", lastName=" + lastName + ", vehicleModel=" + vehicleModel + ", address="
				+ address + ", phoneNumber=" + phoneNumber + ", email=" + email + ", username=" + username + ", active="
				+ active + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hash(username);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(username, other.username);
	}
	
	
}
