package common;

import server.ClientInfo;

public class ServerPacket {
	private String username;
	private final boolean delete = false;
	private final boolean add = true;
	
	
	//1st workflow -- get IP for a given uname
	public ServerPacket( String username ){
		
	}
	
	//2nd workflow -- client changes status to newStatus
	public ServerPacket( String username, Status newStatus ){
		
	}
	
	//3rd workflow -- client adds or removes a buddy
	public ServerPacket( String sendingClient, String buddyName, boolean operation ){
		
	}
	
	public String getUsername(){
		return username;
	}
	
}