

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


import java.sql.*;
import java.util.Vector;

public class SearchWindow extends JFrame{
	
	//frame parameters
	private static final int FRAME_WIDTH = 515;
	private static final int FRAME_HEIGHT = 630;
	private static final int FRAME_X_ORIGIN = 370;
	private static final int FRAME_Y_ORIGIN = 70;
	
	private JTextField searchText;
	private JLabel searchLabel,picture;
	private JButton itemSearchButton,supplierSearchButton,addToCartButton,printBillButton,updateButton,insertButton;
	
	
	private JLabel item_idLabel, typeLabel, nameLabel, supplierLabel, locationLabel, prizeLabel, amountLabel, timeStampLabel;
	private JTextField item_idText, typeText, nameText, supplierText, locationText, prizeText, amountText, timeStampText;
	
	
	private JList list,cart,itemList;
	private TextArea billTextArea,descriptionTextArea;
	private Container contentPane;
	
	private Connection connection;
	
	
	//bill
	private Bill bill;
	private BillBook billBook;
	private BillBookStorage billBookStorage;
	
	//menu items
	private JMenu showMenu;
	private JMenu editMenu;
	
	//billBook window
	private BillBookWindow bbw;
	
	
	//for the total prize of bill
	double totalPrize;
	
	public SearchWindow(){
		//initialize bill book window
		bbw = new BillBookWindow();
		

		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(Exception e){
			System.err.println("unable to connect to driver");
		}
		
		bill = new Bill();
		billBook = new BillBook();
		billBookStorage = new BillBookStorage("billBook_Test_1.data");
		
		
		//create bill initially
		//billBook.addBill(bill);
		
		try{
			//billBookStorage.write(billBook);
			billBook = billBookStorage.read();
		}
		catch(IOException ioException){
			System.out.println("error in writing bill book");
		}
		
		
		//menu creating
		//create show menu and its menu item
		createShowMenu();
		createEditMenu();
		

		//and add it to the menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(showMenu);
		menuBar.add(editMenu);
		
		//initilize textArea in billBookWindow 
		bbw.getBillBookTextArea().setText(billBook.showBill());
	}
	
	//after loging purposes
	public void adminAuthority(){
		editMenu.setEnabled(true);
	}
	public void standardUserAuthority(){
		editMenu.setEnabled(false);
	}
	
	private void connectToDB(){
		try{
			connection = DriverManager.getConnection("jdbc:mysql://localhost/userAccounts?user=root&password=mysql");
		}catch(SQLException e){
			System.out.printf("SQLException: " + e.getMessage() + "\n");
			System.out.printf("SQLState: " + e.getSQLState() + "\n");
			System.out.printf("VendorError: " + e.getErrorCode() + "\n");
		}
	}
	
	public void init(){
		connectToDB();
	}
	
	private void loadItems(/*String itemName*/){
		Vector v = new Vector();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT name FROM itemTable");
			while(rs.next()) {
				v.addElement(rs.getString("name"));
			}
			rs.close();
		} catch(SQLException e) {
			displaySQLErrors(e);
		}
		itemList.setListData(v);
	
	}
	
	private void loadSimilarItems(String itemName){
		Vector v = new Vector();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM itemTable WHERE name LIKE '"+itemName.substring(0, 1)+"%'");
			while(rs.next()) {
				v.addElement(rs.getString("name"));
			}
			rs.close();
		} catch(SQLException e) {
			displaySQLErrors(e);
		}
		itemList.removeAll();
		itemList.setListData(v);
	}
	

	public void buildSearchWindow(){
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
		setTitle("SearchingSystem");
		setResizable(false);
		
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		

		
		//searchLabel = new JLabel("Name of Item ");
		//first.add(searchLabel);
		
		/*searchText = new JTextField("",15);
		first.add(searchText);
		
		itemSearchButton = new JButton("Item Search");
		first.add(itemSearchButton);*/
		
		JPanel first = new JPanel(new GridLayout(1,2));
		
		JPanel second = new JPanel(new GridLayout(4,1));
		

		JPanel third = new JPanel(new GridLayout(20,2));
		
		searchText = new JTextField("",15);
		third.add(searchText);
		
		itemSearchButton = new JButton("Item Search");
		third.add(itemSearchButton);
		
		item_idLabel = new JLabel("item_id");
		item_idText = new JTextField(15);		
		item_idText.setEditable(false);
		
		typeLabel = new JLabel("type");
		typeText = new JTextField(15);			
		typeText.setEditable(false);
		
		nameLabel = new JLabel("name");
		nameText = new JTextField(15);			
		nameText.setEditable(false);
		
		supplierLabel = new JLabel("supplier");
		supplierText = new JTextField(15);		
		supplierText.setEditable(false);
		
		locationLabel = new JLabel("location");
		locationText = new JTextField(15);		
		locationText.setEditable(false);
		
		prizeLabel = new JLabel("prize");
		prizeText = new JTextField(15);			
		prizeText.setEditable(false);
		
		amountLabel = new JLabel("amount");
		amountText = new JTextField(15);		
		amountText.setEditable(false);
		
		timeStampLabel =  new JLabel("timeStamp");
		timeStampText = new JTextField(15);		
		timeStampText.setEditable(false);
		
		//update button
		updateButton = new JButton("Update");
		updateButton.setVisible(false);
		
		//insert buttton
		insertButton = new JButton("Insert");
		insertButton.setVisible(false);
		
		third.add(item_idLabel);
		third.add(item_idText);
		third.add(typeLabel);
		third.add(typeText);
		third.add(nameLabel);
		third.add(nameText);
		third.add(supplierLabel);
		third.add(supplierText);
		third.add(locationLabel);
		third.add(locationText);
		third.add(prizeLabel);
		third.add(prizeText);
		third.add(amountLabel);
		third.add(amountText);
		third.add(timeStampLabel);
		third.add(timeStampText);
		//add update button
		third.add(updateButton);
		//add insert button
		third.add(insertButton);
		
		billTextArea = new TextArea();
		billTextArea.setPreferredSize(new Dimension(300, 100));
		billTextArea.setEditable(false);
		second.add(billTextArea);
		
		picture = new JLabel(createImageIcon("images/no_preview.png"));
		picture.setPreferredSize(new Dimension(177, 100));
		add(picture, BorderLayout.CENTER);
		
		second.add(picture);
		
		/*JLabel tempLabel = new JLabel();
		tempLabel.setPreferredSize(new Dimension(177, 5));
		second.add(tempLabel);*/
		
		descriptionTextArea = new TextArea();
		descriptionTextArea.setPreferredSize(new Dimension(300, 100));
		descriptionTextArea.setEditable(false);
		second.add(descriptionTextArea);
		
		itemList = new JList();
		loadItems();
		itemList.setVisibleRowCount(3);
		JScrollPane itemListScrollPane = new JScrollPane(itemList);
		second.add(itemListScrollPane);
		
		addToCartButton = new JButton("Add to Cart");
		first.add(addToCartButton);
		
		printBillButton = new JButton("Print Bill");
		first.add(printBillButton);
		
		
		contentPane.add(third);
		contentPane.add(second);
		
		contentPane.add(first);

		show();
		
		
		
		/*list = new JList();
		list.setPreferredSize(new Dimension(177,122));
		add(list, BorderLayout.CENTER);
		
		//cart operations
		
		cart = new JList();
		cart.setPreferredSize(new Dimension(177,122));
		add(cart, BorderLayout.CENTER);
		
		addToCartButton = new JButton("AddToCart");
		contentPane.add(addToCartButton);
	
        
        
        

		itemSearchButton.addActionListener(new ItemHandlerClass());
		supplierSearchButton.addActionListener(new SupplierHandlerClass());
		addToCartButton.addActionListener(new CartHandlerClass());*/
		
		itemSearchButton.addActionListener(new ItemHandlerClass());
		addToCartButton.addActionListener(new CartHandlerClass());
		printBillButton.addActionListener(new PrintBillHandlerClass());
		updateButton.addActionListener(new UpdateHandlerClass());
		insertButton.addActionListener(new InsertHandlerClass());
		
		itemList.addListSelectionListener(
				new ListSelectionListener(){
					public void valueChanged(ListSelectionEvent event){
						String itemName = (String)itemList.getSelectedValue();
						searchText.setText(itemName);
						
						try{
							
							Statement statement = connection.createStatement();
							ResultSet rs = statement.executeQuery("SELECT * FROM itemTable WHERE name = '"+itemName+"'");
							
							if(rs.next()){
								item_idText.setText(rs.getString("item_id"));
								typeText.setText(rs.getString("type"));
								nameText.setText(rs.getString("name"));
								supplierText.setText(rs.getString("supplier"));
								locationText.setText(rs.getString("location"));
								prizeText.setText(rs.getString("prize"));
								amountText.setText(rs.getString("amount"));
								timeStampText.setText(rs.getString("ts"));
								
								descriptionTextArea.setText(rs.getString("description"));
								
								picture.setIcon(createImageIcon("images/"+rs.getString("imageName")+ ".gif"));
							}
							else{
								System.out.println("there is no such item");
							}
							
						}catch(SQLException exception){
							displaySQLErrors(exception);
						}
						

					}
				}
				);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	private void clearTexts(){
		item_idText.setText("");
		typeText.setText("");
		nameText.setText("");
		supplierText.setText("");
		locationText.setText("");
		prizeText.setText("");
		amountText.setText("");
		timeStampText.setText("");
	}
	
	private void setTextsEditable(){
		typeText.setEditable(true);
		nameText.setEditable(true);
		supplierText.setEditable(true);
		locationText.setEditable(true);
		prizeText.setEditable(true);
		amountText.setEditable(true);
	}
	
	private void setTextsUnEditable(){
		typeText.setEditable(false);
		nameText.setEditable(false);
		supplierText.setEditable(false);
		locationText.setEditable(false);
		prizeText.setEditable(false);
		amountText.setEditable(false);
	}
	
	private void whenUpdateMenuClicked(){
		addToCartButton.setVisible(false);
		printBillButton.setVisible(false);
		//itemSearchButton.setVisible(false);
		updateButton.setVisible(true);
		insertButton.setVisible(false);
		
		searchText.setEnabled(true);
		itemSearchButton.setEnabled(true);
		item_idText.setEditable(false);
		
	}
	
	private void whenUpdateButtonClicked(){
		addToCartButton.setVisible(true);
		printBillButton.setVisible(true);
		//itemSearchButton.setVisible(false);
		updateButton.setVisible(false);
		
		setTextsUnEditable();
	}
	
	private void whenInsertMenuClicked(){
		addToCartButton.setVisible(false);
		printBillButton.setVisible(false);
		
		searchText.setEnabled(false);
		itemSearchButton.setEnabled(false);
		
		updateButton.setVisible(false);
		
		insertButton.setVisible(true);
	}
	
	private void whenInsertButtonClicked(){
		addToCartButton.setVisible(true);
		printBillButton.setVisible(true);
		//itemSearchButton.setVisible(false);
		insertButton.setVisible(false);
		
		setTextsUnEditable();
		item_idText.setEditable(false);
		
		searchText.setEnabled(true);
		itemSearchButton.setEnabled(true);

	}
	
	
	//Returns an ImageIcon, or null if the path was invalid. 
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SearchWindow.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private class PrintBillHandlerClass implements ActionListener{
    	
    	
    	
    	public void actionPerformed(ActionEvent e){
    		
    		billTextArea.setText("");
    		
    		try {
				billBook = billBookStorage.read();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    		billBook.addBill(bill);
    		
    		try{
    			billBookStorage.write(billBook);
    		}
    		catch(IOException ioException){
    			System.out.println("error in writing bill book");
    		}
  
    		
    		
    		
    		//bill.showBill();
    		
    		//update bill database
    		
    		//System.out.println("--bill book---");
    		
    		/*String sss =billBook.showBill();
    		
    		billTextArea.setText(sss);*/
    		
    		
    		
    		
    		billTextArea.append("total Prize: " + bill.netTotalPrize);
    		
    		bill = new Bill();
    		bbw.getBillBookTextArea().setText(billBook.showBill());
    		totalPrize=0;
    		
    		//bill.clearBill();
    	}
    	
    }
    
    private class CartHandlerClass implements ActionListener{
    	
    	String s="";
	
        public void actionPerformed(ActionEvent e){
        	String amountStr = JOptionPane.showInputDialog("Enter amount: ");
    		int amountInt = Integer.parseInt(amountStr);
    		
    		String tempItem = searchText.getText();
    		double tempPrize=0;
    	
    		
    		try{
				
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM itemTable WHERE name = '"+tempItem+"'");
				int currentAmount=0;
				int buyAmount=0;
				if(rs.next()){
					tempPrize = Double.parseDouble(rs.getString("prize"));
					currentAmount = Integer.parseInt(rs.getString("amount"));
					
					
					//create bill
					bill.addBillRecord(tempItem, amountInt, tempPrize);
					
					rs.close();
					int i = statement.executeUpdate("UPDATE itemTable " +"SET amount = " + (currentAmount-amountInt) + " WHERE name = '"+ tempItem +"'");
				}
				else{
					System.out.println("there is no such item");
				}
				
			}catch(SQLException exception){
				displaySQLErrors(exception);
			}
    		
    		
    		//upadate text box when amount is less
    		try{		
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM itemTable WHERE name = '"+tempItem+"'");
				if(rs.next()){
					amountText.setText(rs.getString("amount"));
					rs.close();
					
					totalPrize += amountInt*tempPrize;
		    		s = tempItem + "  " + amountInt + "  " + amountInt*tempPrize + "\n";
		    		billTextArea.append(s);
		    		System.out.println(s);
					
				}
				else{
					System.out.println("there is no such item");
				}
				
			}catch(SQLException exception){
				displaySQLErrors(exception);
			}
    		
    	}
    }
	
	
    private class ItemHandlerClass implements ActionListener{

		public void actionPerformed(ActionEvent e){
			
			String tempItem = searchText.getText();
			System.out.println(tempItem);
			
			try{
				
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM itemTable WHERE name = '"+tempItem+"'");
				
				if(rs.next()){
					item_idText.setText(rs.getString("item_id"));
					typeText.setText(rs.getString("type"));
					nameText.setText(rs.getString("name"));
					supplierText.setText(rs.getString("supplier"));
					locationText.setText(rs.getString("location"));
					prizeText.setText(rs.getString("prize"));
					amountText.setText(rs.getString("amount"));
					timeStampText.setText(rs.getString("ts"));
					
					descriptionTextArea.setText(rs.getString("description"));
					
					picture.setIcon(createImageIcon("images/"+rs.getString("imageName")+ ".gif"));
				}
				else{
					loadSimilarItems(tempItem);
					System.out.println("there is no such item");
				}
				
			}catch(SQLException exception){
				displaySQLErrors(exception);
			}
			
			
			itemList.addListSelectionListener(
					new ListSelectionListener(){
						public void valueChanged(ListSelectionEvent event){
							String itemName = (String)itemList.getSelectedValue();
							//otherwise doesnt work
							searchText.setText(itemName);
							
							try{
								
								Statement statement = connection.createStatement();
								ResultSet rs = statement.executeQuery("SELECT * FROM itemTable WHERE name = '"+itemName+"'");
								
								if(rs.next()){
									item_idText.setText(rs.getString("item_id"));
									typeText.setText(rs.getString("type"));
									nameText.setText(rs.getString("name"));
									supplierText.setText(rs.getString("supplier"));
									locationText.setText(rs.getString("location"));
									prizeText.setText(rs.getString("prize"));
									amountText.setText(rs.getString("amount"));
									timeStampText.setText(rs.getString("ts"));
									
									descriptionTextArea.setText(rs.getString("description"));
									
									picture.setIcon(createImageIcon("images/"+rs.getString("imageName")+ ".gif"));
								}
								else{
									System.out.println("there is no such item");
								}
								
							}catch(SQLException exception){
								displaySQLErrors(exception);
							}
							

						}
					}
					);
			
			
			
			
		}
		
		
		
	
	}
    

    
    
    /*public static void main(String[] args){
    	SearchWindow searchWindow = new SearchWindow();
    	searchWindow.init();
    	searchWindow.buildSearchWindow();
    	
    }*/
	
    private void displaySQLErrors(SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLState: " + e.getSQLState());
		System.out.println("VendorError: " + e.getErrorCode());
	}
    
    
    //making menu items
    
    private void createShowMenu( ) {
		JMenuItem item;
		showMenu = new JMenu("Show");
		item = new JMenuItem("Bill Book"); //Bill Book
		item.addActionListener(new MenuHandlerClass());
		showMenu.add(item);
	}
    
    private void createEditMenu(){
    	JMenuItem item;
    	editMenu = new JMenu("Edit");
    	
    	item = new JMenuItem("Insert");
    	item.addActionListener(new MenuHandlerClass());
    	editMenu.add(item);
    	
    	item = new JMenuItem("Update");
    	item.addActionListener(new MenuHandlerClass());
    	editMenu.add(item);
    	
    	item = new JMenuItem("Delete");
    	item.addActionListener(new MenuHandlerClass());
    	editMenu.add(item);
    }
    

    
    private class MenuHandlerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			String menuName;
			menuName = event.getActionCommand();
			if (menuName.equals("Bill Book")) {
				System.out.println("Show Bill book");
				bbw.buildBillBookWindow();
			} 
			else if(menuName.equals("Delete")){
				try{
					Statement statement = connection.createStatement();
					int i = statement.executeUpdate("DELETE FROM itemTable WHERE name = '"+ searchText.getText()+"'");
					descriptionTextArea.setText("");
					descriptionTextArea.setText("Deleted " + i + "rows successfully");
					clearTexts();
					itemList.removeAll();
					loadItems();
				}
				catch(SQLException deleteException){
					displaySQLErrors(deleteException);
				}
			}
			
			else if(menuName.equals("Update")){
				setTextsEditable();
				whenUpdateMenuClicked();
			}
			
			else if(menuName.equals("Insert")){
				setTextsEditable();
				item_idText.setEditable(true);
				whenInsertMenuClicked();
			}
			
			
			else {
				
			}
			
		}
    	
    }
    
    private class UpdateHandlerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try{
				Statement statement = connection.createStatement();
				int i = statement.executeUpdate("UPDATE itemTable " +"SET type='" + typeText.getText() + "', "+ "name='" + nameText.getText() + "'," +	"supplier='" + supplierText.getText() + "'," + 	"location='" + locationText.getText() + "'," +	"prize='" + prizeText.getText() + "'," + "amount='" + amountText.getText() + "'," +	"ts = now() " +	"WHERE item_id = '"+ item_idText.getText()+"'");
				descriptionTextArea.setText("");
				descriptionTextArea.setText("Updated " + i + "rows successfully");
				clearTexts();
				itemList.removeAll();
				loadItems();
				whenUpdateButtonClicked();
			}
			catch(SQLException updateException){
				displaySQLErrors(updateException);
			}
		}
    	
    }
    
    private class InsertHandlerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try{
				Statement statement = connection.createStatement();
				int i = statement.executeUpdate("INSERT INTO itemTable VALUES(" + item_idText.getText() +", " + "'"+ typeText.getText() + "', "+ "'" + nameText.getText() + "'," +	"'" + supplierText.getText() + "'," + 	"'" + locationText.getText() + "'," + prizeText.getText() + "," + amountText.getText() + ",NULL," +"'' ,"+" now(),"+	"'"+typeText.getText()+"_"+nameText.getText()+"' )");
				descriptionTextArea.setText("");
				
				descriptionTextArea.setText("Deleted " + i + "rows successfully");
				clearTexts();
				itemList.removeAll();
				loadItems();
				whenInsertButtonClicked();
			}
			catch(SQLException insertException){
				displaySQLErrors(insertException);
			}
		}
    	
    }
    
    
    
}
