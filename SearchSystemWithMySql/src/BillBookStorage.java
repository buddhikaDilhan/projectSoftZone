import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BillBookStorage {
	
	private String fileName;
	
	public BillBookStorage(String fileName){
		this.fileName = fileName;
	}
	
	public void write(BillBook billBook) throws IOException{
		
		File outFile = new File(fileName);
		FileOutputStream outFileStream = new FileOutputStream(outFile);
		ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
		
		outObjectStream.writeObject(billBook);
		
		outObjectStream.close();
	}
	
	public BillBook read() throws IOException{
		
		BillBook billBook;
		
		File inFile = new File(fileName);
		FileInputStream inFileStream = new FileInputStream(inFile);
		ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
		
		
		try{
			
			billBook = (BillBook) inObjectStream.readObject();
			
		}
		catch(ClassNotFoundException e){
			System.out.println("BillBook class is not found");
			billBook = null;
		}
		
		return billBook;
		
	}

}
