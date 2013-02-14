package client;

/**
 * Custom exception class for socket issues
 * @author Steven Shaw
 * @author Keith Feldman
 */
public class NoInternetException extends java.net.UnknownHostException{

	private static final long serialVersionUID = -8013232072241975753L;

	/**
	 * Creates a new exception with a given message
	 * @param msg the message for this exception
	 */
	public NoInternetException( String msg ){
		super( msg );
	}
	
	/**
	 * Outputs the stack trace to std err
	 */
	public void printStackTrace(){
		super.printStackTrace();
	}
	
	/**
	 * returns the message for this exception
	 */
	public String getMessage(){
		return super.getMessage();
	}
	
}
