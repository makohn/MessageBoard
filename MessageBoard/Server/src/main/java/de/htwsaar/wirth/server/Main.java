package de.htwsaar.wirth.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
		
		// usage aktualisieren mit username und passwort des koordinators
	private static final String USAGE = "Usage:\n"
			+ "to start as child-server:\t server groupName localPort parentHostname parentPort username password\n"
			+ "to start as a root-server:\t server groupName localPort username password";

	private static final String ALREADY_IN_USE = "The specified port is already in use.";
	private static final String PARENT_NOT_AVAILABLE = "The parent is not responding.";

	public static void main(String[] args) {
		System.out.println("Server is alive,running on Port "+args[1]+" and kicking.");
		String groupName;
		int localPort;
		String parentHostname = null;
		int parentPort = 0;
		String username;
		String password;
		boolean startAsRoot = false;
		boolean isValid = true;

		// start as root-server
		if (args.length == 4) {
			startAsRoot = true;
		} else if (args.length == 6) { // username und password später optional
			startAsRoot = false;
		} else {
			isValid = false;
		}

		if (!isValid) {
			System.out.println(USAGE);
			System.exit(1);
		}

		// read arguments
		groupName = args[0];
		localPort = Integer.parseInt(args[1]);
		
//		if (!startAsRoot) {
//			parentHostname = args[2];			
//			parentPort = Integer.parseInt(args[3]);
//			username = args[4];
//			password = args[5];
//		}

		// start server
		if (startAsRoot) {
			try {
				username = args[2];
				password = args[3];
				new Server(groupName, localPort, username, password);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				System.out.println(ALREADY_IN_USE);
			}
		} else {
			try {
				parentHostname = args[2];			
				parentPort = Integer.parseInt(args[3]);
				username = args[4];
				password = args[5];
				new Server(groupName, localPort, parentHostname, parentPort, username, password);
				System.out.println("Server is connected to "+parentHostname+" : "+parentPort);


			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				System.out.println(ALREADY_IN_USE);
			} catch (NotBoundException e) {
				System.out.println(PARENT_NOT_AVAILABLE);
			}
		}
	}

}
