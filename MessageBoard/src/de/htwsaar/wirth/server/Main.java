package de.htwsaar.wirth.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

	private static final String USAGE = "Usage:\n"
			+ "to start as root-server:\t server groupName localPort parentHostname parentPort\n"
			+ "to start as a child-server:\t server groupName localPort";

	private static final String ALREADY_IN_USE = "The specified port is already in use.";
	private static final String PARENT_NOT_AVAILABLE = "The parent is not responding.";

	public static void main(String[] args) {
		String groupName;
		int localPort;
		String parentHostname = null;
		int parentPort = 0;
		boolean startAsRoot = false;
		boolean isValid = true;

		// start as root-server
		if (args.length == 2) {
			startAsRoot = true;
		} else if (args.length == 4) {
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
		if (!startAsRoot) {
			parentHostname = args[2];
			parentPort = Integer.parseInt(args[3]);
		}

		// start server
		if (startAsRoot) {
			try {
				new Server(groupName, localPort);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				System.out.println(ALREADY_IN_USE);
			}
		} else {
			try {
				new Server(groupName, localPort, parentHostname, parentPort);
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
