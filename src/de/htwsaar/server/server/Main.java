package de.htwsaar.server.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import de.htwsaar.remote.remote.MessageBoard;

public class Main {

	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		
		System.out.print("Port: ");
		int port = s.nextInt();
		
		try {
			MessageBoardImpl rmbi = new MessageBoardImpl();
			Registry reg = LocateRegistry.createRegistry(port);
			reg.bind("MessageBoard", rmbi);
			
			System.out.println("An Oberabteilung haengen? (y/n)");
			String answ = s.next();
			if(answ.equals("y")) {
				System.out.print("Port: ");
				port = s.nextInt();
				Registry rmiRegistry = LocateRegistry.getRegistry(port);
				MessageBoard msgBoard = (MessageBoard) rmiRegistry.lookup("MessageBoard");
				msgBoard.register(rmbi);
			}
			
			System.out.println("Server up and running");
		} catch(Exception e) { e.printStackTrace();}
		
		s.close();
		
	}
	
}
