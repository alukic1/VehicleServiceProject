package etfbl.mdp.services;

import java.time.LocalDateTime;
import java.util.ArrayList;

import etfbl.mdp.data.AppointmentDataSource;

import etfbl.mdp.models.Appointment;
import etfbl.mdp.models.ServiceStatus;
import etfbl.mdp.models.ServiceType;

public class AppointmentService {

	AppointmentDataSource data = AppointmentDataSource.getInstance();
	
	public ArrayList<Appointment> getAppointments(){
		return data.appointments;
	}
	
	public ArrayList<Appointment> getScheduledAppointments(){
		ArrayList<Appointment> scheduled = new ArrayList<>();
		for(Appointment a : data.appointments) {
			if(a.isApproved() && a.getStatus().equals(ServiceStatus.PENDING))
				scheduled.add(a);
		}
		return scheduled;
	}
	
	public ArrayList<Appointment> getRequestedAppointments(){
		ArrayList<Appointment> requests = new ArrayList<>();
		for(Appointment a : data.appointments) {
			if(!a.isApproved() && a.getStatus().equals(ServiceStatus.PENDING))
				requests.add(a);
		}
		return requests;
	}
	
	public Appointment getById(int id) {
		for(Appointment a : data.appointments)
			if(a.getId() == id)
				return a;
		return null;
	}
	
	public ArrayList<Appointment> getPastAppointmentsByClient(String username){
		ArrayList<Appointment> clientAppointments = new ArrayList<>();
		for(Appointment a : data.appointments)
			if(username.equals(a.getClientUsername()) && !a.getStatus().equals(ServiceStatus.PENDING)) {
				clientAppointments.add(a);
			}
		return clientAppointments;
	}
	
	public boolean acceptAppointment(int id, boolean accept) {
	
		for(Appointment a : data.appointments) {
			System.out.println("poslani id " + id);
			System.out.println("postojeci id "+ a.getId());
			if(a.getId() == id) {
				a.setApproved(accept);
				if(!accept)
					a.setStatus(ServiceStatus.CANCELED);
				
				data.saveAppointments();
				return true;
			}
		}
		return false;
	}
	
	public boolean addComment(int id, String comment) {
		for(Appointment a : data.appointments) {
			if(a.getId() == id) {
				a.setComment(comment);
				data.saveAppointments();
				return true;
			}
		}
		return false;
	}
	
	public boolean makeAppointment(String username, String dateTime, ServiceType type) {

		int id = data.appointments.size() + 1;
		
			if(data.appointments.add(new Appointment(id, username, type, dateTime))) {
				data.saveAppointments();
				return true;
			}
		
		return false;
	}
	
	public void finishAppointment(Appointment a) {
		for(Appointment app : data.appointments) {
			if(a.getId() == app.getId())
				app.setStatus(ServiceStatus.FINISHED);
		}
		a.setStatus(ServiceStatus.FINISHED);
		data.saveAppointments();
	}
}
