package client;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;

import common.Status;
import common.ServerPacket;

public class Comm extends Thread{

	public static final int SERVER_PORT = 8010;
	public static final int COMM_PORT = 8011;
	public static InetAddress SERVER;
	
	private Socket clientToServer;
	private Socket meToOther;
	private OutputStream os;
	private InputStream is;
	
	public Comm( String username ) throws NoInternetException{
		try {
			SERVER = InetAddress.getByName("192.168.1.107");
			startServerSocket();
			ServerPacket signingOn = new ServerPacket( Client_Driver.getCurrentUser().getUsername(), Status.online );
			sendServerPacket( signingOn );
			
		} catch (UnknownHostException e) {
			throw new NoInternetException("Cannot find server");
		}
	}
	
	private void startServerSocket() throws NoInternetException{
		try{
			clientToServer = new Socket( SERVER, SERVER_PORT );
			clientToServer.setKeepAlive( true );
			os = clientToServer.getOutputStream();
			is = clientToServer.getInputStream();
		} catch (UnknownHostException e) {
			throw new NoInternetException("Cannot find server");
		} catch (IOException io ){
			io.printStackTrace();
		}
	}

	public void sendMessage( IMPacket toSend ) throws NoInternetException{
		try {
			if( meToOther == null ){
			
				String curBuddy = toSend.getDestUsername();
				ServerPacket senderIP = new ServerPacket( curBuddy );
				sendServerPacket( senderIP );
				senderIP = receiveServerPacket( is );
				InetAddress dest = senderIP.getIP();
				
				meToOther = new Socket( dest, COMM_PORT );
			}
			
			sendIMPacket( toSend, meToOther.getOutputStream() );
			Client_Driver.getCurrentUser().addSentMessage( 
					toSend.getDestUsername(), toSend.getData() );
			
		} catch (IOException e) {
			throw new NoInternetException("Message not delivered");
		}
	}
	
	public void run(){
		while( true ){
			try {
				InputStream clientIS = meToOther.getInputStream();
				if( clientIS.available() > 0 ){
					IMPacket received = receiveIMPacket( clientIS );
					Client_Driver.getCurrentUser().addReceivedMessage( 
							received.getSrcUsername(), received.getData() );
				}
				if( is.available() > 0 ){
					ServerPacket received = receiveServerPacket( is );
					Client_Driver.updateBuddyStatus( received.getUsername(), received.getStatus() );	
				}
			} catch (IOException e) {
				System.err.println( e.getMessage() );
			}
		}
	}
	
	private void sendServerPacket( ServerPacket sp ) throws NoInternetException{
		try {
			if( clientToServer.isClosed() == true ){
				startServerSocket();
			}
			ObjectOutputStream out = new ObjectOutputStream( os );
			out.writeObject( sp );
			out.close();
		} catch (IOException e) {
			throw new NoInternetException( "Can't send server packet" );
		}
	}
	
	private void sendIMPacket( IMPacket i, OutputStream os ) throws NoInternetException{
		try {
			ObjectOutputStream out = new ObjectOutputStream( os );
			out.writeObject( i );
			out.close();
		} catch (IOException e) {
			throw new NoInternetException( "Can't send IM" );
		}
	}
	
	private IMPacket receiveIMPacket( InputStream is ) throws NoInternetException{
		IMPacket im = null;
		try{
			ObjectInputStream ois = new ObjectInputStream( is );
			Object obj = ois.readObject();
			im = (IMPacket) obj;
		} catch (IOException e) {
			throw new NoInternetException( "Can't receive server packet" );
		} catch (ClassNotFoundException e) {
			throw new NoInternetException( "Can't receive server packet" );
		}
		return im;
	}

	private ServerPacket receiveServerPacket( InputStream is ) throws NoInternetException{
		ServerPacket sp = null;
		try{
			ObjectInputStream ois = new ObjectInputStream( is );
			Object obj = ois.readObject();
			sp = (ServerPacket) obj;
		} catch (IOException e) {
			throw new NoInternetException( "Can't receive server packet" );
		} catch (ClassNotFoundException e) {
			throw new NoInternetException( "Can't receive server packet" );
		}
		return sp;
	}
}
