package server;

public class ServerPacket_Handler extends Thread {
	private String username;
	private ClientInfo information;
	
	public ServerPacket_Handler(ServerPacket packet){
		// Extract info
		username = packet.getUsername();
		information = packet.getInfo();
	}
	
	@Override
	public void run() {
		// handle packet
	}
}