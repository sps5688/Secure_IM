package server;

import java.net.InetAddress;
import java.util.HashSet;

import common.Status;

/**
 * ClientInfo.java
 * 
 * Represents a client's information
 * 
 * @author Steven Shaw
 * @author Keith Feldman
 */
public class ClientInfo {
	private HashSet<String> toBeNotified;
	private Status status;
	private InetAddress ipAddr;
	
	/**
	 * Constructs new ClientInfo
	 */
	public ClientInfo(){
		toBeNotified = new HashSet<String>();
		status = Status.online;
	}
	
	/**
	 * Changes the client's status.
	 * 
	 * @param userStatus The new client's status
	 */
	public void changeStatus(Status userStatus){
		status = userStatus;
		if( status == Status.offline ){
			ipAddr = null;
		}
	}
	
	/**
	 * Retrieves a client's status.
	 * 
	 * @return the client's status
	 */
	public Status getStatus(){
		return status;
	}
	
	/**
	 * Retrieves a client's IP Address.
	 * 
	 * @return the client's IP Address
	 */
	public void setIP( InetAddress newIP ){
		this.ipAddr = newIP;
	}
	
	/**
	 * Sets a client's IP Address.
	 * 
	 */
	public InetAddress getIP(){
		return ipAddr;
	}
	
	/**
	 * Adds a user to the client's notification list.
	 * 
	 * @param The username to add to the client's notification list.
	 */
	public void addUserToNotifyList(String username){
		toBeNotified.add(username);
	}
	
	/**
	 * Removes a user to the client's notification list.
	 * 
	 * @param The username to remove from the client's notification list.
	 */
	public void removeUserFromNotifyList(String username){
		toBeNotified.remove(username);
	}
	
	/**
	 * Retrieves the client's notification list.
	 * 
	 * @param The client's notification list.
	 */
	public HashSet<String> getNotifyList(){
		return toBeNotified;
	}
	
}
