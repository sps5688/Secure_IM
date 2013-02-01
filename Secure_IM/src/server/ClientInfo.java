package server;

import java.util.HashSet;

import common.Status;

public class ClientInfo {
	private HashSet<String> toBeNotified;
	private Status status;
	
	public ClientInfo(){
		toBeNotified = new HashSet<String>();
		status = Status.online;
	}
	
	public void changeStatus(Status userStatus){
		status = userStatus;
	}
	
	public String getStatus(){
		if(status.equals(Status.online)){
			return "online";
		}else if(status.equals(Status.away)){
			return "away";
		}else{
			return "offline";
		}
	}
	
	public void addUserToNotifyList(String username){
		toBeNotified.add(username);
	}
	
	public void removeUserFromNotifyList(String username){
		toBeNotified.remove(username);
	}
	
	public void notifyUsers(){
		// Notify user
		for(String user : toBeNotified){
			System.out.println("Notifying " + user);
		}
	}
}