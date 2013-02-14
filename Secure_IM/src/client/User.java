package client;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import common.ServerPacket;

/**
 * Class for user
 * @author Steve
 */
@SuppressWarnings("serial")
public class User implements Serializable{

	private ArrayList<String> buddies = new ArrayList<String>();
	private HashMap<String, String> messageLog = new HashMap<String, String>();
	private String userName;
	private String currentBuddy;
	
	/**
	 * Creates a new user with the given name
	 * @param userName the name for this user
	 */
	public User(String userName){
		this.userName = userName;
		buddies.add("MySpace Tom");
	}
	
	/**
	 * Gets all the buddies for this user
	 * @return an arraylist containing all the buddies for this user
	 */
	public ArrayList<String> getBuddies(){ 
		return buddies;
	}
	
	/**
	 * returns the username
	 * @return the username
	 */
	public String getUsername(){
		return userName;
	}
		
	/**
	 * Adds a new buddy with the given name
	 * @param buddyName the name to add for this user
	 * @throws NoInternetException if there are problems informing the server this buddy was added
	 */
	public void addBuddy(String buddyName) throws NoInternetException{
		buddies.add(buddyName);
		try {
			ServerPacket addPacket = new ServerPacket( userName, buddyName, ServerPacket.add );
			Comm.sendServerPacket( addPacket );
		} catch (IOException e) {
			throw new NoInternetException( "Can't connect to the server." );
		}
	}

	/**
	 * Removes a buddy with the given name
	 * @param buddyName the name to remove for this user
	 * @throws NoInternetException if there are problems informing the server this buddy was removed
	 */
	public void removeBuddy(String buddyName) throws NoInternetException{
		buddies.remove(buddyName);
		try {
			ServerPacket removePacket = new ServerPacket( userName, buddyName, ServerPacket.delete );
			Comm.sendServerPacket( removePacket );
		} catch (IOException e) {
			throw new NoInternetException( "Can't connect to the server." );
		}
	}
	
	/**
	 * Add a sent message to the messageLog
	 * @param buddyName the buddy name for this message
	 * @param message the message that will be sent
	 */
	public void addSentMessage(String buddyName, String message){
		if(messageLog.get(buddyName) == null){
			messageLog.put(buddyName, userName + ": " + message + "\n");
		}else{
			String history = messageLog.get(buddyName);
			history += (userName + ": " + message + "\n");
			messageLog.put(buddyName, history);
		}
		try {
			if( !Client_Driver.comms.containsKey( buddyName ) ){
				Client_Driver.comms.put( buddyName, new Comm() );				
			}
			Client_Driver.comms.get( buddyName ).sendMessage( 
					new IMPacket( userName, buddyName, message ) );
			if( !Client_Driver.comms.get( buddyName ).hasStarted() ){
				Client_Driver.comms.get( buddyName ).start();				
			}
		} catch (NoInternetException e) {
			System.err.println( e.getMessage() );
		}
	}
	
	/**
	 * Receive a message and add this to the messageLog
	 * @param buddyName the buddy name for this message
	 * @param message the message that was received
	 */
	public void addReceivedMessage(String buddyName, String message){
		if(messageLog.get(buddyName) == null){
			messageLog.put(buddyName, buddyName + ": " + message + "\n");
		}else{
			String history = messageLog.get(buddyName);
			history += (buddyName + ": " + message + "\n");
			messageLog.put(buddyName, history);
		}
	}
	
	/**
	 * set the currently selected buddy
	 * @param buddy the new currently selected buddy
	 */
	public void setCurrentBuddy( String buddy ){
		currentBuddy = buddy;
	}
	
	/**
	 * get the currently selected buddy
	 * @return the currently selected buddy
	 */
	public String getCurrentBuddy(){
		return currentBuddy;
	}
	
	/**
	 * get the message history for a given buddy name
	 * @param buddyName the buddy for which to get the history
	 * @return the history for the buddy
	 */
	public String getMessageHistory(String buddyName){
		return messageLog.get(buddyName);
	}
	
	/**
	 * delete message history for a given buddy
	 * @param buddyName the buddy name for which to remove the history
	 */
	public void deleteMessageHistory(String buddyName){
		messageLog.remove(buddyName);
	}
}