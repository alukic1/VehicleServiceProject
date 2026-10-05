package etfbl.mdp.api;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import etfbl.mdp.models.VehiclePart;
import etfbl.mdp.services.VehiclePartService;

@Path("/parts")
public class VehiclePartAPI {

	VehiclePartService service;
	
	public VehiclePartAPI() {
		service = new VehiclePartService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<VehiclePart> getParts(){
		return service.getVehicleParts();
	}
}
