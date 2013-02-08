package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import client.Comm;

import common.ServerPacket;
//import common.ServerWorkflow;
import common.Status;

public class Server extends Thread{
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
	    		ClientInfo information;
	    		if( activeUsers.containsKey(packet.getUsername()) ){
	    			information = activeUsers.get( packet.getUsername() );
	    		}else{
	    			information = new ClientInfo();
	    			activeUsers.put(packet.getUsername(), information);
	    		}
	    		
	    		Server s = new Server( packet, connection );
	    		s.start();

	    	}
	    }catch(Exception e){
	    	System.err.print("Gotta catch them all.");
	    }
	}
	
	private ServerPacket sp;
	private Socket clientConn;
	private String username;
	
	public Server( ServerPacket sp, Socket connection ){
		this.sp = sp;
		this.clientConn = connection;
		this.username = sp.getUsername();
	}
	
	public void run(){
		switch( sp.getWorkflowType() ){
			case getIP:
				sp.setIP( activeUsers.get( username ).getIP() );
				ObjectOutputStream out;
				try {
					out = new ObjectOutputStream( clientConn.getOutputStream() );
					out.writeObject( sp );
					out.close();
				} catch (IOException e) {
					System.err.println( e.getMessage() );
				}
				
				break;
			
			case editBuddy:
				if( sp.getOperation() == ServerPacket.add ){
					activeUsers.get( sp.getBuddyName() ).addUserToNotifyList( sp.getUsername() );
				}else{
					activeUsers.get( sp.getBuddyName() ).removeUserFromNotifyList( sp.getUsername() );
				}
				break;
				
			case statusChange:
				if ( sp.getStatus() == Status.online ||
						sp.getStatus() == Status.away ){
					activeUsers.get( username ).setIP( clientConn.getInetAddress() );
				}
				activeUsers.get( username ).changeStatus( sp.getStatus() );
				for( String thisUser : activeUsers.get( username ).getNotifyList() ){
					if( activeUsers.get( thisUser ).getStatus() != Status.offline ){
						try {
							Socket toNotify = new Socket( activeUsers.get( thisUser ).getIP(), Comm.SERVER_PORT );
							out = new ObjectOutputStream( toNotify.getOutputStream() );
							out.writeObject( sp );
							out.close();
						} catch (IOException e) {
							System.err.println( e.getMessage() );
						}
						
					}
				}
				break;
		}
	}
}