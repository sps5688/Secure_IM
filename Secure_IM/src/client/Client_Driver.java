
package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import common.Status;
import java.util.HashMap;

/**
 * @author Steven Shaw
 * @author Keith Feldman
 * Client Driver is where the client is started from
 * Called with the server hostname as an argument
 */
public class Client_Driver {
	private static User currentUser;
	private static GUI g;
	private static boolean isOpened;
	
	/**
	 * Map of buddy names to comm objects
	 * Used for each open active conversation
	 */
	public static HashMap<String, Comm> comms;
	
	/**
	 * Creates the user and initializes the comm object
	 * @param userName the username of this client
	 */
	public static void createUser(String userName ){
		initComms();
		currentUser = new User(userName );
		// Update Server with user name so there are no duplicates
	}
	
	/**
	 * Updates the opened status to newOpened
	 * @param newOpened whether opened or not
	 */
	public static void updateOpened( boolean newOpened ){
		isOpened = newOpened;
	}
	
	/**
	 * Creates the comms map and sets opened to true
	 */
	public static void initComms(){
		comms = new HashMap<String, Comm>();
		isOpened = true;
	}
	
	/**
	 * returns the current user
	 * @return the current user
	 */
	public static User getCurrentUser(){
		return currentUser;
	}
		
	/**
	 * Updates the status of a buddy to a new status
	 * @param buddyName the buddy to update
	 * @param buddyStatus the new status
	 */
	public static void updateBuddyStatus( String buddyName, Status buddyStatus ){
		g.changeBuddyStatus(buddyName, buddyStatus == Status.offline ? false : true );
	}
	
	/**
	 * Updates the message history area for a given buddy
	 * @param buddyName the buddy name to refresh the message history area for
	 */
	public static void updateMessageHistory( String buddyName ){
		g.refreshMessageHistoryArea( buddyName );
	}
	
	/**
	 * instantiates the GUI, Comm, and then listens for messages
	 * @param args 0th arg is the hostname
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
			if( args.length != 1 ){
				System.err.println( "usage: java client/Client_Driver hostname" );
				System.exit( 1 );
			}else{
				Comm.initComm( args[0] );				
			}
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
