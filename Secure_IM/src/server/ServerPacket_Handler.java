package server;

import common.ServerPacket;

public class ServerPacket_Handler extends Thread {
	private String username;
	private ClientInfo information;
	
	public ServerPacket_Handler(ServerPacket packet, ClientInfo information){
		// Extract info
		username = packet.getUsername();
		this.information = information;
	}
	
	@Override
	public void run() {
		// handle packet
	}
}