package etfbl.mdp.models;

import java.io.Serializable;

public class Order implements Serializable{
	
	private String partCode;
	private int quantity;
	private OrderStatus status;
	
	public Order(String partCode, int quantity) {
		super();
		this.partCode = partCode;
		this.quantity = quantity;
		this.status = OrderStatus.PENDING;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order [partCode=" + partCode + ", quantity=" + quantity + ", status=" + status + "]";
	}
	
	
}
