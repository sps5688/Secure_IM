package server;

public class ServerPacket {
	private String username;
	private ClientInfo information;
	
	public String getUsername(){
		return username;
	}
	
	public ClientInfo getInfo(){
		return information;
	}
}