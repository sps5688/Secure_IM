package server;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
	public static ServerPacket packet;
	public static Socket connection = null;
	public static ObjectInputStream in;
	public static ServerSocket socket;
	
	public static HashMap<String, ClientInfo> activeUsers = new HashMap<String, ClientInfo>();
	
	public static void removeActiveUser(String username){
		activeUsers.remove(username);
	}
	
	public static void main(String[] args){
	    try{
	    	socket = new ServerSocket(8010);
	      
	    	while(true){
	    		// Accepts incoming connection
	    		connection  = socket.accept();
	    		
	    		// Retrieves packet
	    		in = new ObjectInputStream(connection.getInputStream());
	    		packet = (ServerPacket) in.readObject();
	    		
	    		// Add to activeUsers structure
	    		activeUsers.put(packet.getUsername(), packet.getInfo());
	    		
	    		// Services packet
	    		Thread clientThread = new Thread(new ServerPacket_Handler(packet)); 
	    		clientThread.start();
	    	}
	    }catch(Exception e){
	    	System.err.print("Gotta catch them all.");
	    } 
	  }
}