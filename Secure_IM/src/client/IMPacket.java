package client;

import java.io.Serializable;

public class IMPacket implements Serializable{

	private static final long serialVersionUID = -7684885567046505017L;

	private String data;
	private String srcUsername, destUsername;
	
	public IMPacket( String srcUsername, String destUsername, String data ){
		this.srcUsername = srcUsername;
		this.destUsername = destUsername;
		this.data = data;
	}
	
	/*public IMPacket( String srcUsername, String destUsername ){
		this( srcUsername, destUsername, "" );
		bye = true;
		data = null;
	}*/
	
	public String getData(){
		return data;
	}
		
	public void setData( String newData ){
		data = newData;
	}
	
	public String getSrcUsername(){
		return srcUsername;
	}
	
	public String getDestUsername(){
		return destUsername;
	}
	
	public String toString(){
		return srcUsername + " sent to " + destUsername + 
				" the following message:\n" + data;
	}
}
