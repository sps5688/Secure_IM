package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.Socket;

import common.ServerPacket;

@SuppressWarnings("serial")
public class User implements Serializable{

	private ArrayList<String> buddies = new ArrayList<String>();
	private HashMap<String, String> messageLog = new HashMap<String, String>();
	private String userName;
	private String currentBuddy;
	
	public User(String userName){
		this.userName = userName;
		buddies.add("MySpace Tom");
	}
	
	public ArrayList<String> getBuddies(){ 
		return buddies;
	}
	
	public String getUsername(){
		return userName;
	}
		
	public void addBuddy(String buddyName) throws NoInternetException{
		buddies.add(buddyName);
		try {
			Socket serverConn = new Socket( Comm.SERVER, Comm.SERVER_PORT );
			ServerPacket addPacket = new ServerPacket( userName, buddyName, ServerPacket.add );
			ObjectOutputStream out = new ObjectOutputStream( serverConn.getOutputStream() );
			out.writeObject( addPacket );
			out.close();
		} catch (IOException e) {
			throw new NoInternetException( "Can't connect to the server." );
		}
	}
	
	public void removeBuddy(String buddyName) throws NoInternetException{
		buddies.remove(buddyName);
		try {
			Socket serverConn = new Socket( Comm.SERVER, Comm.SERVER_PORT );
			ServerPacket addPacket = new ServerPacket( userName, buddyName, ServerPacket.delete );
			ObjectOutputStream out = new ObjectOutputStream( serverConn.getOutputStream() );
			out.writeObject( addPacket );
			out.close();
		} catch (IOException e) {
			throw new NoInternetException( "Can't connect to the server." );
		}

	}
	
	public void addSentMessage(String buddyName, String message){
		if(messageLog.get(buddyName) == null){
			messageLog.put(buddyName, userName + ": " + message + "\n");
		}else{
			String history = messageLog.get(buddyName);
			history += (userName + ": " + message + "\n");
			messageLog.put(buddyName, history);
		}
		try {
			Client_Driver.getComm().sendMessage( new IMPacket( userName, buddyName, message ) );
		} catch (NoInternetException e) {
			System.err.println( e.getMessage() );
		}
	}
	
	public void addReceivedMessage(String buddyName, String message){
		if(messageLog.get(buddyName) == null){
			messageLog.put(buddyName, buddyName + ": " + message + "\n");
		}else{
			String history = messageLog.get(buddyName);
			history += (buddyName + ": " + message + "\n");
			messageLog.put(buddyName, history);
		}
	}
	
	public void setCurrentBuddy( String buddy ){
		currentBuddy = buddy;
	}
	
	public String getCurrentBuddy(){
		return currentBuddy;
	}
	
	public String getMessageHistory(String buddyName){
		return messageLog.get(buddyName);
	}
	
	public void deleteMessageHistory(String buddyName){
		messageLog.remove(buddyName);
	}
}