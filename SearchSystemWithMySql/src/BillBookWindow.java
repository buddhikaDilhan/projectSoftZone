import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.*;


public class BillBookWindow extends JFrame{
	
	//frame parameters
	private static final int FRAME_WIDTH = 400;
	private static final int FRAME_HEIGHT = 400;
	private static final int FRAME_X_ORIGIN = 350;
	private static final int FRAME_Y_ORIGIN = 125;
	
	private TextArea billBookTextArea;
	
	private Container contentPane;
	
	public BillBookWindow(){
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
		setTitle("Bill Book");
		setResizable(false);
		
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		
		JPanel first = new JPanel(new GridLayout(1,1));
		
		billBookTextArea = new TextArea();
		billBookTextArea.setPreferredSize(new Dimension(300, 300));
		billBookTextArea.setEditable(false);
		first.add(billBookTextArea);
		
		contentPane.add(first);
	}
	
	public void buildBillBookWindow(){
		setVisible(true);	
	}
	
	public TextArea getBillBookTextArea(){
		return billBookTextArea;
	}
	
	/*public static void main(String[] args){
    	BillBookWindow billBookWindow = new BillBookWindow();
    	billBookWindow.buildBillBookWindow();
    }*/

}
