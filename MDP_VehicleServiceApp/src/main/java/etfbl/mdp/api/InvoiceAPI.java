package etfbl.mdp.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import etfbl.mdp.models.CreateInvoiceDto;
import etfbl.mdp.services.AppointmentService;
import etfbl.mdp.services.InvoiceService;

@Path("/invoice")
public class InvoiceAPI {


	InvoiceService service;
	AppointmentService appService;
	
	public InvoiceAPI() {
		service = new InvoiceService();
		appService = new AppointmentService();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendInvoice(CreateInvoiceDto data) {
		appService.finishAppointment(data.getAppointment());
		if(service.sendInvoice(data))
			return Response.status(200).build();
		else
			return Response.status(500).build();
	}
}
