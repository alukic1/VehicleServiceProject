package etfbl.mdp.models;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.json.bind.annotation.JsonbTypeAdapter;

public class MakeAppointmentDto {

	String username;
	
	
	String dateTime;
	
	ServiceType type;
	
	public MakeAppointmentDto() {
		super();
	}
	public MakeAppointmentDto(String username, String dateTime, ServiceType type) {
		super();
		this.username = username;
		this.dateTime = dateTime;
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public ServiceType getType() {
		return type;
	}
	public void setType(ServiceType type) {
		this.type = type;
	}
	
}
