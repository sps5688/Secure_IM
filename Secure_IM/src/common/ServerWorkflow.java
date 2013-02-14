package common;

/**
 * ServerWorkflow.java
 * 
 * Represents the three different work flows that the server
 * can process for a client.
 * 
 * getIP: Retrieves a client's IP Address
 * statusChange: Changes a client's status.
 * editBuddy: Adds/Removes a buddy from a client's buddy list.
 * 
 * @author Steven Shaw
 * @author Keith Feldman
 */
public enum ServerWorkflow {
	getIP, statusChange, editBuddy
}
