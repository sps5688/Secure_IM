package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


import common.ServerPacket;
import common.ServerWorkflow;
import common.Status;

/**
 * Server.java
 * 
 * @author Steven Shaw
 * @author Keith Feldman
 */
public class Server extends Thread{
	public static ServerSocket socket;
	
	// Active User storage structure
	public static HashMap<String, ClientInfo> activeUsers = new HashMap<String, ClientInfo>();
	
	/**
	 * Removes a user from the active users list.
	 * 
	 * @param username The username to remove from the active
	 *  user storage structure.
	 */
	public static void removeActiveUser(String username){
		activeUsers.remove(username);
	}
	
	/**
	 * Main method for server execution.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args){
	    try{
	    	socket = new ServerSocket( 8010 );
	      
	    	while(true){ 		
	    		// Accepts unique incoming connection
	    		Socket connection  = socket.accept();
	    		Server s = new Server( connection );
	    		s.start();
	    	}
	    }catch(Exception e){
	    	System.err.print("Gotta catch them all.");
	    	try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    }
	}
	
	// Client communication
	private ServerPacket sp;
	private Socket clientConn;
	private String username;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	/**
	 * Represents a connection from a client.
	 * 
	 * @param connection The client's socket connection
	 */
	public Server( Socket connection ){
		this.clientConn = connection;
		
		// Communicates with a client
		try {
			ois = new ObjectInputStream(clientConn.getInputStream() );
			oos = new ObjectOutputStream( clientConn.getOutputStream() );
			sp = (ServerPacket) ois.readObject();
		} catch( ClassNotFoundException cnfe ){
			cnfe.printStackTrace();
		} catch( IOException e ) {
			e.printStackTrace();
		}
		this.username = sp.getUsername();		
		ClientInfo information;
		if( activeUsers.containsKey( username ) ){
			information = activeUsers.get( username );
		}else{
			information = new ClientInfo();
			activeUsers.put( username, information);	
		}
		
	}
	
	/**
	 * Creates a new thread for each connected user.
	 */
	public void run(){
		try {
			// Processes user's requests until they sign off
			while( sp.getWorkflowType() != ServerWorkflow.statusChange || sp.getStatus() != Status.offline ){
				switch( sp.getWorkflowType() ){
					case getIP:
						// Retrieves a buddies IP address
						if( activeUsers.containsKey( sp.getUsername() ) ){
							System.out.println("Getting IP for " + sp.getUsername());
							System.out.println("IP: " + activeUsers.get( sp.getUsername() ).getIP() );
							sp.setIP( activeUsers.get( sp.getUsername() ).getIP() );							
						}else{
							sp.setIP( null );
						}
						oos.writeObject( sp );
						break;
					
					case editBuddy:
						// Adds or removes a buddy from the user's buddy list
						if( activeUsers.get( sp.getBuddyName() ) != null ){
							if( sp.getOperation() == ServerPacket.add ){
								System.out.println("Adding " + sp.getBuddyName() + " to " + sp.getUsername() + "'s buddy list");
								activeUsers.get( sp.getBuddyName() ).addUserToNotifyList( sp.getUsername() );
							}else{
								System.out.println("Removing " + sp.getBuddyName() + " to " + sp.getUsername() + "'s buddy list");
								activeUsers.get( sp.getBuddyName() ).removeUserFromNotifyList( sp.getUsername() );
							}
						}else{
							System.out.println( "Buddy " + sp.getBuddyName() + " does not exist." );
						}
						break;
						
					case statusChange:
						// Changes a user's status
						ClientInfo info = activeUsers.get( username );
						info.setIP( clientConn.getInetAddress() );
						activeUsers.put( username, info );
						System.out.println("Changing " + sp.getUsername() + " status to " + sp.getStatus());
						
						activeUsers.get( username ).changeStatus( sp.getStatus() );
						break;
				}
				sp = (ServerPacket) ois.readObject();
			}
		} catch (IOException e) {
			//Connection closed unexpectedly
			System.err.println( "Connection closed");
			sp.setStatus( Status.offline );
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println( "Changing " + sp.getUsername() + " status to " + sp.getStatus() + ", run method is done." );
		activeUsers.get( username ).changeStatus( sp.getStatus() );
		try {
			clientConn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}