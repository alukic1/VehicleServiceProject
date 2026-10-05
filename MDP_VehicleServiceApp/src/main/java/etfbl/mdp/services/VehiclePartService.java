package etfbl.mdp.services;

import java.util.ArrayList;

import etfbl.mdp.data.VehiclePartDataSource;
import etfbl.mdp.models.VehiclePart;

public class VehiclePartService {

	VehiclePartDataSource data = VehiclePartDataSource.getInstance();
	
	public ArrayList<VehiclePart> getVehicleParts(){
		return data.parts;
	}
	
	public boolean savePart(VehiclePart part) {
		return data.save(part);
	}
	
	public boolean updatePart(VehiclePart part) {
		return data.update(part);
	}
	
	public boolean deletePart(String code) {
		return data.delete(code);
	}
}
