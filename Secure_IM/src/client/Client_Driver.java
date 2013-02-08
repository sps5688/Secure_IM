package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import common.Status;

public class Client_Driver {
	private static User currentUser;
	private static GUI g;
	private static Comm c;
	
	public static void createUser(String userName ){
		currentUser = new User(userName );
		// Update Server with user name so there are no duplicates
	}
	
	public static User getCurrentUser(){
		return currentUser;
	}
	
	public static Comm getComm(){
		return c;
	}
	
	public static void updateBuddyStatus( String buddyName, Status buddyStatus ){
		g.refreshBuddy(buddyName, buddyStatus == Status.offline ? false : true );
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileInputStream f_in;
		ObjectInputStream obj_in;
		Object obj = null;
		
		// Determines if there is already a user to use
		boolean exists = false;
		try {
			if( new File( "user.ser" ).exists() ){
				f_in = new FileInputStream("user.ser");
				obj_in = new ObjectInputStream(f_in);
				obj = obj_in.readObject();
				currentUser = (User) obj;
				exists = true;
			}
			System.out.println ("about to create some comm" );
			System.out.println ("created some comm" );
		}catch (Exception e) {
			System.err.println( e.getMessage() );
		}
		g = new GUI( exists );
		try{
			c = new Comm( currentUser.getUsername() );
		} catch (NoInternetException e) {
			System.err.println( e.getMessage() );
		}



	}
}