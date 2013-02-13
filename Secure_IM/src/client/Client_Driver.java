package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import common.Status;
import java.util.HashMap;

public class Client_Driver {
	private static User currentUser;
	private static GUI g;
	private static boolean isOpened;
	
	public static HashMap<String, Comm> comms;
	
	public static void createUser(String userName ){
		initComm();
		currentUser = new User(userName );
		// Update Server with user name so there are no duplicates
	}
	
	public static void updateOpened( boolean newOpened ){
		isOpened = newOpened;
	}
	
	public static void initComm(){
		comms = new HashMap<String, Comm>();
		isOpened = true;
	}
	
	public static User getCurrentUser(){
		return currentUser;
	}
		
	public static void updateBuddyStatus( String buddyName, Status buddyStatus ){
		g.changeBuddyStatus(buddyName, buddyStatus == Status.offline ? false : true );
	}
	
	public static void updateMessageHistory( String buddyName ){
		g.refreshMessageHistoryArea( buddyName );
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileInputStream f_in;
		ObjectInputStream obj_in;
		Object obj = null;
		
		// Determines if there is already a user to use
		boolean exists = false;
		try {
			if( new File( "user.ser" ).exists() ){
				f_in = new FileInputStream("user.ser");
				obj_in = new ObjectInputStream(f_in);
				obj = obj_in.readObject();
				currentUser = (User) obj;
				exists = true;
			}
		}catch (Exception e) {
			System.err.println( e.getMessage() );
		}
		
		g = new GUI( exists );
		
		try{
			Comm.initComm();
		} catch (NoInternetException e) {
			System.err.println( e.getMessage() );
		}
		
		
		ServerSocket commListened = null;
		try {
			commListened = new ServerSocket( Comm.COMM_PORT );
		} catch (IOException e) {
			e.printStackTrace();
		}
		IMPacket received = null;
		while( isOpened ){
			try {
				Socket connection = commListened.accept();
	    		Comm c = new Comm( connection );
	    		c.initDiffie();
	    		received = c.receiveIMPacket();
	    		if( received != null ){
		    		currentUser.addReceivedMessage( 
							received.getSrcUsername(), received.getData() );
		    		updateMessageHistory( received.getSrcUsername() );
		    		comms.put( received.getSrcUsername(), c );
		    		c.start();	    			
	    		}
			}catch( NoInternetException nie ){
				nie.printStackTrace();
			}catch( IOException e ){
				e.printStackTrace();
			}
		}
		try {
			commListened.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
