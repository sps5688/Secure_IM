package client;

public class IMPacket {

	private String data;
	
	public IMPacket( String data ){
		this.data = data;
	}
	
	public String getData(){
		return data;
	}
	
	public void setData( String newData ){
		data = newData;
	}
	
	public String toString(){
		return data;
	}
}
