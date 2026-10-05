package etfbl.mdp.api;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import etfbl.mdp.services.ClientService;
import etfbl.mdp.models.Client;
import etfbl.mdp.models.LoginDataDto;

@Path("/clients")
public class ClientAPI {
	
	ClientService service;
	
	public ClientAPI() {
		service = new ClientService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Client> getAll() {
		return service.getClients();
	}
	
	@GET
	@Path("/usernames")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getAllUsernames(){
		return service.getAllUsernames();
	}
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByUsername(@PathParam("username") String username) {
		Client client = service.getByUsername(username);
		if(client != null)
			return Response.status(200).entity(client).build();
		else
			return Response.status(404).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(Client client) {
		if(service.add(client))
			return Response.status(200).entity(client).build();
		else
			return Response.status(500).entity("Username already in use").build();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(LoginDataDto data) {
		if(service.login(data))
			return Response.status(200).build();
		else
			return Response.status(404).build();
	}
	
	@PUT
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response toggleBlock(@PathParam("username") String username) {
		Client client = service.toggleBlock(username);
		if(client != null)
			return Response.status(200).entity(client).build();
		else
			return Response.status(404).build();
	}
	
	@DELETE
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeClient(@PathParam("username") String username) {
		if(service.remove(username))
			return Response.status(200).build();
		else
			return Response.status(404).build();
	}
}
