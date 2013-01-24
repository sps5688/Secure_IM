package client;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class User implements Serializable{

	private ArrayList<String> buddies = new ArrayList<String>();
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
}