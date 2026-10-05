package etfbl.mdp.api;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import etfbl.mdp.services.AppointmentService;
import etfbl.mdp.models.Appointment;
import etfbl.mdp.models.MakeAppointmentDto;

@Path("/appointments")
public class AppointmentAPI {

	AppointmentService service;
	
	public AppointmentAPI() {
		service = new AppointmentService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Appointment> getScheduledAppointments() {
		return service.getScheduledAppointments();
	}
	
	@GET
	@Path("/requests")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Appointment> getRequestedAppointments() {
		return service.getRequestedAppointments();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response acceptAppointment(@PathParam("id") int id, 	String accept) {
		System.out.println("accept " + accept);
		boolean accepted = "true".equals(accept);
		if(service.acceptAppointment(id, accepted))
			return Response.status(200).build();
		else
			return Response.status(404).build();
	}
	
	@PUT
	@Path("/{id}/comment")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response commentAppointment(@PathParam("id") int id, String comment) {
		if(service.addComment(id, comment))
			return Response.status(200).build();
		else
			return Response.status(404).build();
	}
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Appointment> getPastByUsername(@PathParam("username") String username) {
		return service.getPastAppointmentsByClient(username);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response makeAppointment(MakeAppointmentDto app) {
		System.out.println("Primljen DTO: " + app);
		if(service.makeAppointment(app.getUsername(), app.getDateTime(), app.getType()))
			return Response.status(200).build();
		else
			return Response.status(500).entity("Selected appointment is occupied").build();
	}
}
