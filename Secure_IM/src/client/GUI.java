package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.*;

/**
 * GUI.java
 * 
 * Graphical User Interface for the client program.
 * 
 * @author Steven Shaw
 * @author Keith Feldman
 */
public class GUI implements KeyListener, WindowListener, ActionListener, MouseListener{
	// GUI components
	private JFrame mainFrame;
	private JTextArea messageInputArea;
	private JTextArea messageHistoryArea;
	private JList buddyListArea;
	private JButton addBuddyButton, removeBuddyButton;
	private JPanel leftPanel, topPanel, bottomPanel, middlePanel;
	private final DefaultListModel model = new DefaultListModel();
	
	private String selectedBuddy;
	
	/**
	 * Constructs a new GUI for a client.
	 * 
	 * @param exists If the user already exsists on the system or not
	 */
	public GUI( boolean exists ){
		// If first time running program, prompt for user name
		String userName;
		if( !exists ){
			userName = JOptionPane.showInputDialog(null, "Username:", null);
			Client_Driver.createUser( userName );
			
			// Valid user name check
			if(userName == null || userName.equals("")){
				System.exit(0);
			}
		}else{
			Client_Driver.initComms();
			userName = Client_Driver.getCurrentUser().getUsername();
		}
		
		// Initial frame creation
		mainFrame = new JFrame( "Secure IM - " + userName );
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(800, 715);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setResizable(false);
		
		// Set screen location
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		mainFrame.setLocation(width/4, height/5);
		
		// Bottom panel creation
		bottomPanel = new JPanel();
		messageInputArea = new JTextArea(10,65);
		messageInputArea.setLineWrap(true);
		messageInputArea.addKeyListener(this);
		messageInputArea.setBackground(Color.lightGray);
		bottomPanel.add(messageInputArea, BorderLayout.SOUTH);
		
		// Top panel creation
		topPanel = new JPanel();
		addBuddyButton = new JButton("Add Buddy");
		removeBuddyButton = new JButton("Remove Buddy");
		
		addBuddyButton.addActionListener(this);
		removeBuddyButton.addActionListener(this);
		
		topPanel.setLayout(new GridLayout());
		JLabel buddyLabel = new JLabel("Buddy List");
		topPanel.add(buddyLabel, BorderLayout.WEST);
		topPanel.add(addBuddyButton);
		topPanel.add(removeBuddyButton);
		
		// Center panel creation
		middlePanel = new JPanel();
		messageHistoryArea = new JTextArea(30, 54);
		messageHistoryArea.setBackground(Color.lightGray);
		messageHistoryArea.setEditable(false);
		
		// Handles scroll bar
		JScrollPane scroll = new JScrollPane(messageHistoryArea);
		middlePanel.add(scroll);
		
		// Left panel creation
		leftPanel = new JPanel();
		leftPanel.setSize(50, 600);
		
		// Inits buddy list
		buddyListArea = new JList(model);
		ArrayList<String> buddies =  Client_Driver.getCurrentUser().getBuddies();
		for(String curBuddy : buddies){
			model.addElement(curBuddy);
		}
		
		// Buddy list configuration
		buddyListArea.setLayoutOrientation(JList.VERTICAL);
		buddyListArea.setBackground(Color.white);
		buddyListArea.addMouseListener(this);
		leftPanel.add(buddyListArea, BorderLayout.WEST);
		
		// Add panels to frame and display frame
		mainFrame.add(bottomPanel, BorderLayout.SOUTH);
		mainFrame.add(topPanel, BorderLayout.NORTH);
		mainFrame.add(leftPanel, BorderLayout.WEST);
		mainFrame.add(middlePanel, BorderLayout.CENTER);
		mainFrame.addWindowListener(this);
		mainFrame.setVisible(true);
	}
	
	/**
	 * Handles keyboard keyTyped event that occurs when user
	 * types in the messageInputArea box.
	 * 
	 * @param arg0 The key that was entered
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar() == '\n'){
			String receiver = (String) buddyListArea.getSelectedValue();
			
			if(receiver != null){
				// Get message and clear area
				String message = messageInputArea.getText();
				message = message.trim();
				
				if(!message.equals("") || message == null){
					// Send Message and update message history area
					Client_Driver.getCurrentUser().addSentMessage(receiver, message);
					messageHistoryArea.setText(Client_Driver.getCurrentUser().getMessageHistory(receiver));
				}
			}
			
			// Resets message input box
			messageInputArea.setText("");
		}
	}

	/**
	 * Handles window windowClosing event that occurs when
	 * the user closes the program by exiting the window.
	 * 
	 * @param arg0 The WindowEvent that occurred
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		// Serialize data object to a file
		try {
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream("user.ser"));
			out.writeObject(Client_Driver.getCurrentUser());
			out.close();
			for( String thisUser : Client_Driver.comms.keySet() ){
				Comm c = Client_Driver.comms.get( thisUser );
				c.stopClientStreams();
			}
			
			// Stop network connections
			Comm.stopServerSocket();
			Client_Driver.updateOpened( false );
			
			// Exit program
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles Action actionPerformed event that occurs
	 * when the Add or Remove Buttons are clicked.
	 * 
	 * @param arg0 The ActionEvent that occurred
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Adding Buddy
		if(arg0.getActionCommand().equals("Add Buddy")){
			String buddyName = JOptionPane.showInputDialog(null, "Buddy Name:", null);
			try {
				Client_Driver.getCurrentUser().addBuddy(buddyName);
			} catch (NoInternetException e) {
				System.err.println( e.getMessage() );
			}
			
			// Add and refresh GUI
			model.addElement(buddyName);
			mainFrame.validate();
		}else if(arg0.getActionCommand().equals("Remove Buddy")){
			// Removing Buddy
			String buddyName = (String) buddyListArea.getSelectedValue();
			
			if(buddyName != null){
				try{
					Client_Driver.getCurrentUser().removeBuddy(buddyName);
				} catch (NoInternetException e) {
					System.err.println( e.getMessage() );
				}

				Client_Driver.getCurrentUser().deleteMessageHistory(buddyName);
				
				// Remove and refresh GUI
				model.remove(model.indexOf(buddyName));
				mainFrame.validate();	
				messageHistoryArea.setText("");
			}
		}
	}
	
	/**
	 * Handles Mouse mouseReleased event that occurs
	 * when the user's buddy list is clicked.
	 * 
	 * @param arg0 The ActionEvent that occurred
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		selectedBuddy = (String) buddyListArea.getSelectedValue();
		
		// Updates message history area
		if(selectedBuddy != null){
			messageHistoryArea.setText(Client_Driver.getCurrentUser().getMessageHistory(selectedBuddy));
			Client_Driver.getCurrentUser().setCurrentBuddy(selectedBuddy);
		}
	}
	
	/**
	 * Retrieves the message history for a buddy and
	 * updates the messageHistoryArea in the GUI.
	 * 
	 * @param buddyName The name of the buddy to get the message history for.
	 */
	public void refreshMessageHistoryArea( String buddyName ){
		messageHistoryArea.setText( Client_Driver.getCurrentUser().getMessageHistory( buddyName ) );
	}
	
	/**
	 * Changes the status of a buddy.
	 * 
	 * @param buddy The name of the buddy whose status is changed.
	 * @param enable If the buddy is available or not.
	 */
	public void changeBuddyStatus( String buddy, boolean enable ){
		int i = model.indexOf( buddy );
		buddyListArea.getComponent( i ).setEnabled( enable );
		mainFrame.validate();
	}
	
	/* Unused methods */
	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }
	
	@Override
	public void mousePressed(MouseEvent arg0) { }
	
	@Override
	public void windowActivated(WindowEvent arg0) { }

	@Override
	public void windowClosed(WindowEvent arg0) { }

	@Override
	public void windowDeactivated(WindowEvent arg0) { }

	@Override
	public void windowDeiconified(WindowEvent arg0) { }

	@Override
	public void windowIconified(WindowEvent arg0) { }

	@Override
	public void windowOpened(WindowEvent arg0) { }
	
	@Override
	public void keyPressed(KeyEvent arg0) { }

	@Override
	public void keyReleased(KeyEvent arg0) { }
}