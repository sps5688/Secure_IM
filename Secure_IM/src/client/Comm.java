package client;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import common.Status;
import common.ServerPacket;

public class Comm extends Thread{

	public static final int SERVER_PORT = 8010;
	public static final int COMM_PORT = 8011;
	
	public static InetAddress SERVER;
	
	private static Socket clientToServer;

	private static OutputStream ServerOS;
	private static InputStream ServerIS;
	private static ObjectOutputStream ServerOOS;
	private static ObjectInputStream ServerOIS;
	
	public static void initComm() throws NoInternetException{
		try {
			SERVER = InetAddress.getByName("192.168.1.105");
			startServerSocket();
			ServerPacket signingOn = new ServerPacket( Client_Driver.getCurrentUser().getUsername(), Status.online );
			sendServerPacket( signingOn );
			//stopServerSocket();
		} catch (UnknownHostException e) {
			throw new NoInternetException("Cannot find server");
		}
	}
	
	private static void startServerSocket() throws NoInternetException{
		try{
			clientToServer = new Socket( SERVER, SERVER_PORT );
			clientToServer.setKeepAlive( true );
			ServerOS = clientToServer.getOutputStream();
			ServerIS = clientToServer.getInputStream();
		} catch (UnknownHostException e) {
			throw new NoInternetException("Cannot find server");
		} catch (IOException io ){
			io.printStackTrace();
		}
	}
	
	public static void stopServerSocket() throws NoInternetException{
		try {
			ServerPacket signingOff = new ServerPacket( Client_Driver.getCurrentUser().getUsername(), Status.offline );
			sendServerPacket( signingOff );
			
			if( ServerOOS != null ){
				ServerOOS.close();
				if( ServerOS != null ){
					ServerOS.close();	
				}
			}
			
			if( ServerOIS != null ){
				ServerOIS.close();
				if( ServerIS != null ){
					ServerIS.close();
				}
			}
			
			if( clientToServer != null && !clientToServer.isClosed() ){
				clientToServer.close();
			}
		} catch (IOException e) {
			throw new NoInternetException( "Can't close socket" );
		}
		
	}
	
	public static void sendServerPacket( ServerPacket sp ) throws NoInternetException{
		try {
			if( ServerOOS == null ){
				ServerOOS = new ObjectOutputStream( ServerOS );
			}
			ServerOOS.writeObject( sp );

		} catch (IOException e) {
			throw new NoInternetException( "Can't send server packet" );
		}
	}
	
	private static ServerPacket receiveServerPacket() throws NoInternetException{
		ServerPacket sp = null;
		try{
			if( ServerOIS == null ){
				ServerOIS = new ObjectInputStream( ServerIS );				
			}
			sp = (ServerPacket) ServerOIS.readObject();
		} catch (IOException e) {
			throw new NoInternetException( "Can't receive server packet" );
		} catch (ClassNotFoundException e) {
			throw new NoInternetException( "Can't receive server packet" );
		}
		return sp;
	}

	private static byte[] encrypt( String text, long key ){
		
		byte[] keyBytes = null;
		byte[] encrypted = null;
		
		try {
			keyBytes = String.valueOf( key ).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			keyBytes = sha.digest( keyBytes );
			keyBytes = Arrays.copyOf(keyBytes, 16 );
			SecretKeySpec aes = new SecretKeySpec( keyBytes, "AES" );
			Cipher cipher = Cipher.getInstance("AES");
		    cipher.init(Cipher.ENCRYPT_MODE, aes);

		    encrypted = cipher.doFinal( text.getBytes() );

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return encrypted;
	}
	
	private static String decrypt( byte[] encrypted, long key ){

		byte[] keyBytes = null;
		byte[] decrypted = null;
		
		try {
			keyBytes = String.valueOf( key ).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			keyBytes = sha.digest( keyBytes );
			keyBytes = Arrays.copyOf(keyBytes, 16 );		
			SecretKeySpec aes = new SecretKeySpec( keyBytes, "AES" );
			Cipher cipher = Cipher.getInstance( "AES" );
	
			cipher.init( Cipher.DECRYPT_MODE, aes );
			decrypted = cipher.doFinal( encrypted );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return new String( decrypted );
	}

	private Socket meToOther;
	
	private OutputStream ClientOS;
	private InputStream ClientIS;
	private ObjectOutputStream ClientOOS;
	private ObjectInputStream ClientOIS;
	
	private Diffie d;
	private long key;
	
	private boolean started;
	
	public Comm() throws NoInternetException{
		d = new Diffie();
		key = -1;
	}
	
	public Comm( Socket received ) throws NoInternetException{
		started = false;
		key = -1;
		meToOther = received;
		startClientStreams();
		
	}
	
	public void initDiffie(){
		IMPacket pgv;
		try {
			pgv = receiveIMPacket();
			d = new Diffie( pgv.getData() );
			sendIMPacket( new IMPacket( pgv.getSrcUsername(), pgv.getDestUsername(), String.valueOf( d.sendU() ) ) );
			key = d.getKey();

		} catch (NoInternetException e) {
			e.printStackTrace();
		}
		
	}
	
	private void startClientStreams(){
		try{
			ClientOS = meToOther.getOutputStream();
			ClientIS = meToOther.getInputStream();
		} catch (IOException io ){
			io.printStackTrace();
		}
	}
	
	public void stopClientStreams(){
		try {
			if( ClientOOS != null ){
				ClientOOS.close();
				if( ClientOS != null ){
					ClientOS.close();
				}
			}
			if( ClientOIS != null ){
				ClientOIS.close();
				if( ClientIS != null ){
					ClientIS.close();
				}
			}			
			if( meToOther != null && !meToOther.isClosed() ){
				meToOther.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage( IMPacket toSend ) throws NoInternetException{
		try {
			if( meToOther == null ){
			
				String curBuddy = toSend.getDestUsername();
				ServerPacket senderIP = new ServerPacket( curBuddy );
				sendServerPacket( senderIP );
				senderIP = receiveServerPacket();
				InetAddress dest = senderIP.getIP();
				
				meToOther = new Socket( dest, COMM_PORT );
				startClientStreams();
			}
			if( key == -1 ){
				sendIMPacket( new IMPacket( toSend.getSrcUsername(), toSend.getDestUsername(), d.sendPGU() ) );
				d.getV( Long.parseLong( receiveIMPacket().getData() ) );
				key = d.getKey();
			}
			
			toSend.setByteData( encrypt( toSend.getData(), key ) );
			sendIMPacket( toSend );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasStarted(){
		return started;
	}
	
	public void run(){
		started = true;
		IMPacket received = null;
		
		try {
			while( !meToOther.isClosed() ){
					received = receiveIMPacket();
					if( received != null ){
						Client_Driver.getCurrentUser().addReceivedMessage( 
								received.getSrcUsername(), received.getData() );
						Client_Driver.updateMessageHistory( received.getSrcUsername() );
					}else{
						break;
					}
					/*if( ServerIS.available() > 0 ){
						ServerPacket received = receiveServerPacket();
						Client_Driver.updateBuddyStatus( received.getUsername(), received.getStatus() );	
					}*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
		
	private void sendIMPacket( IMPacket i ) throws NoInternetException{
		try {
			if( ClientOOS == null ){
				ClientOOS = new ObjectOutputStream( ClientOS );
			}
			ClientOOS.writeObject( i );
		} catch (IOException e) {
			throw new NoInternetException( "Can't send IM" );
		}
	}
	
	public IMPacket receiveIMPacket() throws NoInternetException{
		IMPacket im = null;
		try{
			if( ClientOIS == null ){
				ClientOIS = new ObjectInputStream( ClientIS );
			}
			im = (IMPacket) ClientOIS.readObject();
			if( key != -1 ){
				im.setData( decrypt( im.getByteData(), key ) );
			}
		}catch( EOFException eof ){
			//Connection closed unexpectedly, can ignore
		}catch( IOException e ) {
			//Connection closed unexpectedly, can ignore
		} catch (ClassNotFoundException e) {
			throw new NoInternetException( "Can't receive IM packet" );
		}
		return im;
	}

}
