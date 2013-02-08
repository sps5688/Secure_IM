package client;

public class IMPacket {

	private String data;
	private String srcUsername, destUsername;
	
	public IMPacket( String srcUsername, String destUsername, String data ){
		this.srcUsername = srcUsername;
		this.destUsername = destUsername;
		this.data = data;
	}
	
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
