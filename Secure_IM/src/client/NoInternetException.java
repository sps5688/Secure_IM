package client;

public class NoInternetException extends java.net.UnknownHostException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8013232072241975753L;

	public NoInternetException( String msg ){
		super( msg );
	}
	
}
