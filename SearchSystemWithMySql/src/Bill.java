import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.util.*;

public class Bill implements Serializable{
	
	private	ArrayList<BillRecord> recordList;
	public String billId;
	
	public double netTotalPrize;

	
	public Bill(){
		recordList = new ArrayList<BillRecord>();
		billId = getCurrentTimeStamp();
		netTotalPrize = 0;
	}
	
	public void addBillRecord(String itemName,int amount,double totalPrize){
		recordList.add(new BillRecord(itemName,amount,totalPrize));
		netTotalPrize += amount*totalPrize;
	}
	
	public void addBillRecord(BillRecord billRecord){
		recordList.add(billRecord);
	}
	
	public void clearBill(){
		recordList.clear();
	}
	
	public String showBill(){
		String s="";
		
		s+="------\n";
		System.out.println("------");
 
		s+="---billId: "+billId+" ---\n";
		System.out.println("---billId: "+billId+" ---");
		for(int i=0;i<recordList.size();i++){
			
			s+=recordList.get(i).getItemName() + "  " + recordList.get(i).getAmount() + "  " + recordList.get(i).getTotalPrize()+"\n";
			System.out.println(recordList.get(i).getItemName() + "  " + recordList.get(i).getAmount() + "  " + recordList.get(i).getTotalPrize());
			
			s+="------\n";
			System.out.println("------");
		}
		
		System.out.println("total amount of bill: " + netTotalPrize);
		s+="total amount of bill: " + netTotalPrize+"\n";
		
		s+="------\n";
		System.out.println("------");
		
		return s;
	}
	
	public static String getCurrentTimeStamp() {
		//to make billId
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}

}
