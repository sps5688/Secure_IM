package server;

import java.util.HashSet;

public class ClientInfo {
	private HashSet<String> toBeNotified;
	private Status status;
	private enum Status{ online, offline, away; }
	
	public void changeStatus(String userStatus){
		if(userStatus.equals("online")){
			status = Status.online;
		}else if(userStatus.equals("away")){
			status = Status.away;
		}else{
			status = Status.offline;
		}
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