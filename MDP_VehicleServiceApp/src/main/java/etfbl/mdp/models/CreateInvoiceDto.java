package etfbl.mdp.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateInvoiceDto {

	private Appointment appointment;
	private ArrayList<VehiclePart> parts;
	private BigDecimal initialPrice;
	
	public CreateInvoiceDto() {
		super();
	}

	public CreateInvoiceDto(Appointment appointment, List<VehiclePart> parts, BigDecimal price) {
		super();
		this.appointment = appointment;
		this.parts = (ArrayList<VehiclePart>) parts;
		this.initialPrice = price;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public ArrayList<VehiclePart> getParts() {
		return parts;
	}

	public void setParts(ArrayList<VehiclePart> parts) {
		this.parts = parts;
	}

	public BigDecimal getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(BigDecimal initialPrice) {
		this.initialPrice = initialPrice;
	}
	
	
}
