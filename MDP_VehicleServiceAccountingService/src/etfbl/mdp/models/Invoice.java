package etfbl.mdp.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class Invoice implements Serializable{
	private String codePart;
	int quantity;
	private BigDecimal codePrice;
	private BigDecimal totalPrice;
	private BigDecimal tax;
	
	public Invoice() {
		super();
	}

	public Invoice(String codePart, BigDecimal codePrice, int quantity) {
		super();
		this.codePart = codePart;
		this.codePrice = codePrice;	
		this.quantity = quantity;
		BigDecimal price = codePrice.multiply(new BigDecimal(quantity));
		this.tax = new BigDecimal(17.0/100.0).multiply(price);
		
		this.totalPrice = price.add(this.tax);
	}

	public String getCodePart() {
		return codePart;
	}

	public void setCodePart(String codePart) {
		this.codePart = codePart;
	}

	public BigDecimal getCodePrice() {
		return codePrice;
	}

	public void setCodePrice(BigDecimal codePrice) {
		this.codePrice = codePrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Invoice [codePart=" + codePart + ", codePrice=" + codePrice + ", totalPrice=" + totalPrice + ", tax="
				+ tax + "]";
	}

	
	
	
}