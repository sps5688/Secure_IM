package client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class User implements Serializable{

	private ArrayList<String> buddies = new ArrayList<String>();
	private HashMap<String, String> messageLog = new HashMap<String, String>();
	private String userName;
	private String IPAddress;
	
	public User(String userName, String IPAddress){
		this.userName = userName;
		this.IPAddress = IPAddress;
		buddies.add("MySpace Tom");
	}
	
	public ArrayList<String> getBuddies(){ 
		return buddies;
	}
	
	public String getUsername(){
		return userName;
	}
	
	public String getIPAddress(){
		return IPAddress;
	}
	
	public void addBuddy(String buddyName){
		buddies.add(buddyName);
	}
	
	public void removeBuddy(String buddyName){
		buddies.remove(buddyName);
	}
	
	public void addSentMessage(String buddyName, String message){
		if(messageLog.get(buddyName) == null){
			messageLog.put(buddyName, userName + ": " + message + "\n");
		}else{
			String history = messageLog.get(buddyName);
			history += (userName + ": " + message + "\n");
			messageLog.put(buddyName, history);
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
	
	public String getMessageHistory(String buddyName){
		return messageLog.get(buddyName);
	}
	
	public void deleteMessageHistory(String buddyName){
		messageLog.remove(buddyName);
	}
}