import java.io.Serializable;
import java.util.ArrayList;


public class BillBook implements Serializable{
	
	public	ArrayList<Bill> billList;

	
	public BillBook(){
		billList = new ArrayList<Bill>();
	}
	
	public void addBill(Bill bill){
		billList.add(bill);
	}
	
	public void clearBill(){
		billList.clear();
	}
	
	public String showBill(){
		
		String s="";
		
		for(int i=0;i<billList.size();i++){
			
			s += billList.get(i).showBill();
			//billList.get(i).showBill();
			
			s += "***-------***\n";
			System.out.println("***-------***");
		}
		
		return s;
		
	}

}
