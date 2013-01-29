package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

	private boolean ping(String ipAddress){
		boolean canReach = true;
		try {
			canReach = InetAddress.getByAddress(ipAddress.getBytes()).isReachable(1000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return canReach;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}