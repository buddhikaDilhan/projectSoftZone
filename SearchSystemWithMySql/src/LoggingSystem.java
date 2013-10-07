
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;


public class LoggingSystem extends JFrame{
	
	private static final int FRAME_WIDTH=300;
	private static final int FRAME_HEIGHT=150;
	private static final int X_ORIGIN=500;
	private static final int Y_ORIGIN=200;

	private JButton logButton;
	private JLabel userLabel,passwordLabel;
	private JTextField userText;
	private JPasswordField passwordText;
	
	private Container contentPane;
	
	private boolean adminAuthority;
	private boolean loggedSuccess;

	//mySql database handeling
	private Connection connection;
	
	
	//search system
	private SearchWindow searchWindow;
	
	//load to jdbc driver 
	public LoggingSystem(){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(Exception e){
			System.err.println("unable to find and load driver");
			System.exit(1);
		}
		adminAuthority=false;
		loggedSuccess = false;
		
		searchWindow = new SearchWindow();
		
	}
	
	public boolean getAdminAuthority(){
		return adminAuthority;
	}
	
	public boolean isLoggedSuccess(){
		return loggedSuccess;
	}
	
	
	//build gui
	public void buildWindow(){
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setLocation(X_ORIGIN,Y_ORIGIN);
		setTitle("LoginSystem");
		setResizable(false);
		
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		
		userLabel = new JLabel("User Name : ");
		userLabel.setPreferredSize(new Dimension(80,20));
		contentPane.add(userLabel);
		
		userText = new JTextField();
		userText.setPreferredSize(new Dimension(150,20));
		contentPane.add(userText);
		
		passwordLabel = new JLabel("Password   : ");
		passwordLabel.setPreferredSize(new Dimension(80,20));
		contentPane.add(passwordLabel);
		
		passwordText = new JPasswordField();
		passwordText.setPreferredSize(new Dimension(150,20));
		contentPane.add(passwordText);
		
		logButton = new JButton("Login");
		contentPane.add(logButton);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		logButton.addActionListener(new loginHandler());
		
		setVisible(true);
	}
	
	
	private void connectToDB(){
		try{
			connection = DriverManager.getConnection("jdbc:mysql://localhost/accounts?user=root&password=mysql");
		}
		catch(SQLException e){
			System.out.printf("SQLException: " + e.getMessage() + "\n");
			System.out.printf("SQLState: " + e.getSQLState() + "\n");
			System.out.printf("VendorError: " + e.getErrorCode() + "\n");
		}
	}
	
	public void init(){
		connectToDB();
	}
	
	private class loginHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			String tempUser = userText.getText();
			String tempPwd = passwordText.getText();
			
			try{
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM userTable WHERE username = '"+ tempUser + "'");
				
				if(rs.next() && (tempPwd.equals(rs.getString("password")))){
					
					String dbAuthority = rs.getString("authority");
					
					//if logging successfull
					System.out.println("loged successfully as " + dbAuthority);
					loggedSuccess = true;
					
					if(dbAuthority.equals("admin")){
						adminAuthority=true;
						searchWindow.adminAuthority();
					}
					else{
						adminAuthority=false;
						searchWindow.standardUserAuthority();
					}
					
					System.out.println("admin authority: "+adminAuthority);
				}
				else{
					//if logging unsuccessful
					System.out.println("username or password incorrect");
				}
				
			}catch(SQLException s){
				System.out.printf("SQLException: " + s.getMessage() + "\n");
				System.out.printf("SQLState: " + s.getSQLState() + "\n");
				System.out.printf("VendorError: " + s.getErrorCode() + "\n");
			}
			
			if(isLoggedSuccess()){
				searchWindow.init();
				searchWindow.buildSearchWindow();
				//close logging window
				setVisible(false);
			}
			
		}
		
	}
	
	
	
	
	
	public static void main(String[] args){
		LoggingSystem loggingSystem = new LoggingSystem();
		
		loggingSystem.init();
		loggingSystem.buildWindow();
	}

}
