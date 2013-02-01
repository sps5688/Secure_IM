package client;

import java.io.IOException;
import java.net.*;

public class Comm {

	private Socket s;
	private ServerSocket ss;
	private InetAddress SERVER;
	private int port = 8010;
	
	public Comm( String username ) throws NoInternetException{
		try {
			SERVER = InetAddress.getLocalHost();
			s = new Socket( SERVER, port );
		} catch (UnknownHostException e) {
			throw new NoInternetException("Cannot find server");
		} catch (IOException io ){
			io.printStackTrace();
		}
				
	}
	
}
