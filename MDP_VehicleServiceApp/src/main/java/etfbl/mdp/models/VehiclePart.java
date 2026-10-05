package etfbl.mdp.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VehiclePart implements Serializable{
	
	private String code;
	private String name;
	private String manufacturer;
	private BigDecimal price;
	private int availableQuantity;
	private String description;
	
	public VehiclePart() {
		super();
	}

	public VehiclePart(String code, String name, String manufacturer, BigDecimal price, int availableQuantity,
			String description) {
		super();
		this.code = code;
		this.name = name;
		this.manufacturer = manufacturer;
		this.price = price;
		this.availableQuantity = availableQuantity;
		this.description = description;
	}
	
	public VehiclePart(String code) {
		super();
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hash(code);
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
		VehiclePart other = (VehiclePart) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return "VehiclePart [code=" + code + ", name=" + name + ", manufacturer=" + manufacturer + ", price=" + price
				+ ", availableQuantity=" + availableQuantity + ", description=" + description + "]";
	}
	
	public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("name", name);
        map.put("manufacturer", manufacturer);
        map.put("price", String.valueOf(price));
        map.put("availableQuantity", String.valueOf(availableQuantity));
        map.put("description", description);
        return map;
    }
	
	public static VehiclePart fromMap(Map<String, String> map) {
        if (map == null || map.isEmpty()) return null;
        return new VehiclePart(
                map.get("code"),
                map.get("name"), 
                map.get("manufacturer"), 
                new BigDecimal(map.get("price")),
                Integer.parseInt(map.get("availableQuantity")), 
                map.get("description")
                );
  
    }
	
}
