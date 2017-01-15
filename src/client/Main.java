package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

import api.MessageBoard;

public class Main {
	
	public static void main(String[] args) {
		try {
			Scanner s = new Scanner(System.in);
			System.out.print("Port: ");
			int port = s.nextInt();
			Registry rmiRegistry = LocateRegistry.getRegistry(port);
			MessageBoard msgBoard = (MessageBoard) rmiRegistry.lookup("MessageBoard");
			System.out.print("Name: ");
			String name = s.next();
			Client thisOne = new Client(name);
			msgBoard.register(thisOne);
		
		int input = -1;
		
		while(input != 0) {
			System.out.println("Waehler Option: ");
			System.out.println("1: Nachricht schreiben");
			System.out.println("2: Nachrichten lesen");
			
			input = s.nextInt();
			
			switch (input) {
			case 1:
				try {
				msgBoard.newMessage(thisOne.name + ": Hallo");
				} catch(Exception e){ e.printStackTrace();};
				break;
			case 2:
				try {
				List<String> msgs = msgBoard.listMessages();
				for(String msg : msgs) {
					System.out.println(msg);
				}
				} catch(Exception e){};
				break;
			default:
				break;
			}
		}
		s.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
}
