package client;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Client_Driver {
	public static User currentUser;
	
	public static void createUser(String userName, String IPAddress){
		currentUser = new User(userName, IPAddress);
		// Update Server with user name so there are no duplicates
	}
	
	public static User getCurrentUser(){
		return currentUser;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileInputStream f_in;
		ObjectInputStream obj_in;
		Object obj = null;
		
		// Determines if there is already a user to use
		try {
			f_in = new FileInputStream("user.ser");
			obj_in = new ObjectInputStream(f_in);
			obj = obj_in.readObject();
			currentUser = (User) obj;
		}catch (Exception e) { } // Can Ignore
		
		new GUI();
	}
}