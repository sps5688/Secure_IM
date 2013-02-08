package server;

import java.net.InetAddress;
import java.util.HashSet;

import common.Status;

public class ClientInfo {
	private HashSet<String> toBeNotified;
	private Status status;
	private InetAddress ipAddr;
	
	public ClientInfo(){
		toBeNotified = new HashSet<String>();
		status = Status.online;
	}
	
	public void changeStatus(Status userStatus){
		status = userStatus;
		if( status == Status.offline ){
			ipAddr = null;
		}
	}
	
	public Status getStatus(){
		return status;
	}
	
	public void setIP( InetAddress newIP ){
		this.ipAddr = newIP;
	}
	
	public InetAddress getIP(){
		return ipAddr;
	}
	
	public void addUserToNotifyList(String username){
		toBeNotified.add(username);
	}
	
	public void removeUserFromNotifyList(String username){
		toBeNotified.remove(username);
	}
	
	public HashSet<String> getNotifyList(){
		return toBeNotified;
	}
	
}
