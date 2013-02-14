package common;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * ServerPacket.java
 * 
 * Represents a packet that is sent from a client to the server.
 * 
 * @author Steven Shaw
 * @author Keith Feldman
 */
public class ServerPacket implements Serializable{

	private static final long serialVersionUID = 6561981141432776662L;

	// Static variables to indicate adding or removing
	public final static boolean delete = false;
	public final static boolean add = true;
		
	private ServerWorkflow sw;
	
	// Used in work flows 1,2,3
	private String username;
	
	// Used in work flow 2
	private Status newStatus;
	
	// Used in work flow 3
	private String buddyName;
	private boolean operation;
	
	// Returned in work flow 1
	private InetAddress IP;

	/**
	 * Constructs a new ServerPacket to be used
	 * in work flow #1 which retrieves a client's IP Address
	 * 
	 * @param username The client to be used for IP lookup
	 */
	public ServerPacket( String username ){
		sw = ServerWorkflow.getIP;
		this.username = username;
	}
	
	/**
	 * Constructs a new ServerPacket to be used
	 * in work flow #2 which changes a client's status.
	 * 
	 * @param username The client to whose status will be changed
	 * @param newStatus The client's new status
	 */
	public ServerPacket( String username, Status newStatus ){
		sw = ServerWorkflow.statusChange;
		this.username = username;
		this.newStatus = newStatus;
	}
	
	/**
	 * Constructs a new ServerPacket to be used
	 * in work flow #3 which adds or removes a buddy
	 * to/from a client's buddy list.
	 * 
	 * @param sendingClient The user name of the sending client
	 * @param buddyName The name of the buddy to add to the client's buddy list
	 * @param operation Indicates which operation the server should perform (Add/Remove)
	 */
	public ServerPacket( String sendingClient, String buddyName, boolean operation ){
		sw = ServerWorkflow.editBuddy;
		this.username = sendingClient;
		this.buddyName = buddyName;
		this.operation = operation;
	}
	
	/**
	 * Retrieves the name of the packet sender.
	 * Used in work flow 1,2, and 3.
	 * 
	 * @return the client who sent the packet
	 */
	public String getUsername(){
		return username;
	}
	
	/**
	 * Retrieves the client's status contained in the packet.
	 * Used in work flow #2
	
	 * @return the client's status
	 */
	public Status getStatus(){
		return newStatus;
	}
	
	/**
	 * Retrieves the name of the buddy a client is altering.
	 * Used in work flow #3
	
	 * @return the name of the buddy
	 */
	public String getBuddyName(){
		return buddyName;
	}
	
	/**
	 * Retrieves the operation that will be performed (Add/Remove).
	 * Used in work flow #3
	
	 * @return the operation value
	 */
	public boolean getOperation(){
		return operation;
	}
	
	/**
	 * Sets the IP that the packet will contain.
	 * Returned in work flow #1
	
	 * @param IP the IP Address to set
	 */
	public void setIP( InetAddress IP ){
		this.IP = IP;
	}
	
	/**
	 * Gets the IP that the packet contains.
	 * Returned in work flow #1
	
	 * @return the IP Address
	 */
	public InetAddress getIP(){
		return IP;
	}
	
	/**
	 * Sets the status of a client.
	 * 
	 * @param newStatus the new status
	 */
	public void setStatus( Status newStatus ){
		this.newStatus = newStatus;
	}
	
	/**
	 * Retrieves the work flow type for the packet.
	 * 
	 * @return the work flow type
	 */
	public ServerWorkflow getWorkflowType(){
		return sw;
	}

}