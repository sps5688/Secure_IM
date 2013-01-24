package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.swing.*;

public class GUI implements KeyListener, WindowListener, ActionListener{
	private String message;
	private JFrame mainFrame;
	private JTextArea messageInputArea;
	private JTextArea messageHistoryArea;
	private JList buddyListArea;
	private JButton addBuddyButton, removeBuddyButton;
	private JPanel leftPanel, topPanel, bottomPanel, middlePanel;
	
	public GUI(){
		// If first time running program, prompt for user name
		if(Client_Driver.getCurrentUser() == null){
			String userName = JOptionPane.showInputDialog(null, "Username:", null);
			Client_Driver.createUser(userName, "192.168.1.1");
			
			// Valid user name check
			if(userName == null || userName.equals("")){
				System.exit(0);
			}
		}
		
		// Initial frame creation
		mainFrame = new JFrame("Secure IM");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(700, 700);
		mainFrame.setLayout(new BorderLayout());
		
		// Set screen location
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		mainFrame.setLocation(width/4, height/5);
		
		// Bottom panel creation
		bottomPanel = new JPanel();
		messageInputArea = new JTextArea(10,61);
		messageInputArea.setLineWrap(true);
		messageInputArea.addKeyListener(this);
		bottomPanel.add(messageInputArea, BorderLayout.SOUTH);
		
		// Top panel creation
		topPanel = new JPanel();
		addBuddyButton = new JButton("Add Buddy");
		removeBuddyButton = new JButton("Remove Buddy");
		
		addBuddyButton.addActionListener(this);
		removeBuddyButton.addActionListener(this);
		
		topPanel.add(addBuddyButton, BorderLayout.WEST);
		topPanel.add(removeBuddyButton, BorderLayout.WEST);
		
		// Center panel creation
		middlePanel = new JPanel();
		messageHistoryArea = new JTextArea(40, 100);
		middlePanel.add(messageHistoryArea);
		
		// Left panel creation
		// ADD MOUSE EVENT FOR WHEN A NEW BUDDY IS CLICKED, WILL UPDATE MESSAGEHISTORY (STORE MESSAGE HISTORY NOT IN THIS CLASS)
		leftPanel = new JPanel();
		leftPanel.setSize(50, 600);
		
		buddyListArea = new JList(Client_Driver.getCurrentUser().getBuddies().toArray());
		buddyListArea.setLayoutOrientation(JList.VERTICAL);
		leftPanel.add(buddyListArea, BorderLayout.WEST);
		
		// Add panels to frame and display frame
		mainFrame.add(bottomPanel, BorderLayout.SOUTH);
		mainFrame.add(topPanel, BorderLayout.NORTH);
		mainFrame.add(leftPanel, BorderLayout.WEST);
		mainFrame.add(middlePanel, BorderLayout.CENTER);
		mainFrame.addWindowListener(this);
		mainFrame.setVisible(true);
	}

	public void updateMessageInputArea(String message){
		messageInputArea.setText(message);
	}
	
	public void updateMessageHistoryArea(String history){
		messageHistoryArea.setText(history);
	}
	
	public void updateBuddyList(){
		// NOT WORKING
		buddyListArea.revalidate();
		buddyListArea.repaint();
		
		leftPanel.revalidate();
		leftPanel.repaint();
		
		mainFrame.validate();
		mainFrame.repaint();
	}
	
	// KeyListener Methods
	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar() == '\n'){
			// Get message and clear area
			message = messageInputArea.getText();
			messageInputArea.setText("");
			
			System.out.println(message); // for testing
			
			if(message != null || message != ""){
				// Send Message
			}
			
			message = "";
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) { }

	@Override
	public void keyReleased(KeyEvent arg0) { }
	
	// WindowListener Methods
	@Override
	public void windowClosing(WindowEvent arg0) {
		// Serialize data object to a file
		try {
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream("user.ser"));
			out.writeObject(Client_Driver.getCurrentUser());
			out.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

	// ActionListener Methods
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("Add Buddy")){
			String buddyName = JOptionPane.showInputDialog(null, "Buddy Name:", null);
			Client_Driver.getCurrentUser().addBuddy(buddyName);
		}else if(arg0.getActionCommand().equals("Remove Buddy")){
			String buddyName = JOptionPane.showInputDialog(null, "Buddy Name:", null);
			Client_Driver.getCurrentUser().removeBuddy(buddyName);
		}
		
		updateBuddyList();
	}
}