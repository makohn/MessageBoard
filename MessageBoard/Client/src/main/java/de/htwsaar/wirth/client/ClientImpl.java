package de.htwsaar.wirth.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class ClientImpl extends UnicastRemoteObject implements Notifiable {
	
    private static final String BIND_KEY = "server";
		
//	private MainViewController gui;
	private MessageBoard parent;
	private AuthPacket auth;
//	private String username;	// sollte das ein attribut sein ?
//	private String group;	// sollte das ein attribut sein ?
//	private String password; // sollte das ein attribut sein ?

	protected ClientImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
		// gui einstellen
		// parent-stub laden ? oder auch beim login ?
		// 
	}

	private static final long serialVersionUID = -7940206816319176143L;
	
	public void login(String username, String password, String parentHost, int parentPort) throws RemoteException, NotBoundException {
		
		// TODO:
//		if (parent != null && auth != null)
//			parent.logout(auth);
		
		// beim Server anmelden
		Registry parentRegistry = LocateRegistry.getRegistry(parentHost, parentPort);
        parent = (MessageBoard) parentRegistry.lookup(BIND_KEY);
		
        // User einloggen
		LoginPacket login = new LoginPacket(username, password);
		auth = parent.registerClient(login, this);
		
		// Nachrichten und User holen
		// TODO: insertMessages(List<Message> messages) für die GUI implementieren etc
		// gui.insertMessages(parent.getMessages(auth));
		// gui.insertUsers(parent.getUsers(auth));
	}
	
	public void logout() {
		// TODO:
//		if (parent != null)
//			parent.logout(auth);
	}
	
	public void addUser(String newUsername, String newPassword) throws RemoteException {
		// TODO: muss hier nicht noch das Passwort verschlüsselt werden ?
		// im Moment wird ein Userobjekt beim Server erstellt, der das Passwort dann wieder in Klartext der DB gibt
		// entweder könnte ja das Passwort nur noch verschlüsselt rausgegeben werden (hab ich das so richtig verstanden, siehe UserImpl)
		// oder wir könnten gleich hier schon das Passwort hashen, so dass der server den klartext nicht mitkriegt
		// beim login kriegt er natürlich klartext und muss in selber zerhacken und schauen ob derselbe hash dabei herauskommt.
		if (parent != null)
			parent.addUser(auth, newUsername, newPassword);
	}
	
	// TODO: User löschen
	public void deleteUser(String username) throws RemoteException {
		if (parent != null)
			parent.deleteUser(auth, username);
	}
	
	public void sendMessage(String msg) throws RemoteException {
		if (parent != null)
			parent.newMessage(auth, msg);
	}
	
	public void editMessage(Message msg, String text) throws RemoteException {		
		if (parent != null) {
			msg.changeMessage(text);
			parent.editMessage(auth, msg);
		}
	}
	
	public void publishMessage(Message msg) throws RemoteException {
		if (parent != null)
			parent.publish(auth, msg);
	}
	
	public void deleteMessage(Message msg) throws RemoteException {
		if (parent != null)
			parent.deleteMessage(auth, msg);
	}
	
	// ----------------------- Notifiable ----------------------------------

	public void notifyNew(Message msg) throws RemoteException {
		// TODO: s.o. bei login
//		gui.insertMessage(msg);
	}

	public void notifyDelete(Message msg) throws RemoteException {
		// TODO: s.o.
//		gui.deleteMessage(msg);		
	}

	public void notifyEdit(Message msg) throws RemoteException {
		// TODO: s.o.
//		gui.editMessage(msg);
	}

	public void notifyNewUser(String username) throws RemoteException {
		// TODO:
//		gui.insertUser(username)		
	}

	@Override
	public void notifyDeleteUser(String username) throws RemoteException {
		// TODO
//		gui.removeUser(username);		
	}
}
