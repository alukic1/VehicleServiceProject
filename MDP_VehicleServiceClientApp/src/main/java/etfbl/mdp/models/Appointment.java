package etfbl.mdp.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment implements Serializable{
	
	private static int num = 0;
	
	private int id;
	private String clientUsername;
	private ServiceType type;
	private String dateTime;
	private boolean approved;
	private String comment;
	private ServiceStatus status;
	
	public Appointment() {
		super();
		this.id = ++num;
	}

	public Appointment(String clientUsername, ServiceType type, String dateTime) {
		super();
		this.id = ++num;
		this.clientUsername = clientUsername;
		this.type = type;
		this.dateTime = dateTime;
		this.status = ServiceStatus.PENDING;
		this.approved = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientUsername() {
		return clientUsername;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}

	public ServiceType getType() {
		return type;
	}

	public void setType(ServiceType type) {
		this.type = type;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	

	public ServiceStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceStatus status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hash(clientUsername) + id;
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
		Appointment other = (Appointment) obj;
		return Objects.equals(clientUsername, other.clientUsername) && id == other.id;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", clientUsername=" + clientUsername + ", type=" + type + ", dateTime="
				+ dateTime + ", approved=" + approved + ", comment=" + comment + ", status=" + status + "]";
	}

}
