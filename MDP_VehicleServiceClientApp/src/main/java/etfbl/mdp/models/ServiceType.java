package etfbl.mdp.models;

public enum ServiceType {
	
	REGULAR_SERVICE("REGULAR SERVICE"),
	REPAIR("REPAIR");
	
	private String value;
	
	private ServiceType(String type)
	{
		this.value = type;
	}
	
	public String getType() {
		return value;
	}
}
