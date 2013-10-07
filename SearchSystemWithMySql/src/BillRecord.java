import java.io.Serializable;


public class BillRecord implements Serializable{
	
	private String itemName;
	private int amount;
	private double totalPrize;
	
	public BillRecord(String itemName,int amount,double totalPrize){
		this.itemName = itemName;
		this.amount = amount;
		this.totalPrize = totalPrize;
	}
	
	
	
	public String getItemName() {
		return itemName;
	}
	public int getAmount() {
		return amount;
	}
	public double getTotalPrize() {
		return totalPrize;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setTotalPrize(float totalPrize) {
		this.totalPrize = totalPrize;
	}
	
	

}
