package client;

import java.io.Serializable;

/**
 * Class for sending IM data
 * @author Keith
 */
public class IMPacket implements Serializable{

	private static final long serialVersionUID = -7684885567046505017L;

	private String data;
	private String srcUsername, destUsername;
	
	private byte[] byteData;
	
	/**
	 * Creates a new IMPacket with unencrypted data
	 * @param srcUsername the username from which this was sent
	 * @param destUsername the username for which this will be sent to
	 * @param data the unencrypted data
	 */
	public IMPacket( String srcUsername, String destUsername, String data ){
		this.srcUsername = srcUsername;
		this.destUsername = destUsername;
		this.data = data;
	}
	
	/**
	 * Creates a new IMPacket with encrypted byte data
	 * @param srcUsername the username from which this was sent
	 * @param destUsername the username for which this will be sent to
	 * @param data the encrypted byte array
	 */
	public IMPacket( String srcUsername, String destUsername, byte[] data ){
		this.srcUsername = srcUsername;
		this.destUsername = destUsername;
		this.byteData = data;
	}
	
	/**
	 * returns the unencrypted data
	 * @return the unencrypted data
	 */
	public String getData(){
		return data;
	}
	
	/**
	 * returns the encrypted byte data
	 * @return the encrypted byte data
	 */
	public byte[] getByteData(){
		return byteData;
	}
	
	/**
	 * sets the byte data
	 * @param newData the new byte data
	 */
	public void setByteData( byte[] newData ){
		this.data = "";
		this.byteData = newData;
	}
		
	/**
	 * sets the unencrypted data
	 * @param newData the new string data
	 */
	public void setData( String newData ){
		data = newData;
	}
	
	/**
	 * returns the source
	 * @return the source
	 */
	public String getSrcUsername(){
		return srcUsername;
	}
	
	/**
	 * returns the destination
	 * @return the destination
	 */
	public String getDestUsername(){
		return destUsername;
	}
	
	/**
	 * output the source, destination, and data for this packet
	 */
	public String toString(){
		return srcUsername + " sent to " + destUsername + 
				" the following message:\n" + data;
	}
}
