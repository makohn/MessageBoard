package de.htwsaar.wirth.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.htwsaar.wirth.client.controller.MainViewController;
import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.MessageImpl;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class Impl extends UnicastRemoteObject implements Notifiable {
	
	private AuthPacket auth;
	private MainViewController gui;
	private MessageBoard parent;
	private String username;	// sollte das ein attribut sein ?
	private String group;	// sollte das ein attribut sein ?
	private String password; // sollte das ein attribut sein ?

	protected Impl() throws RemoteException {
		// TODO Auto-generated constructor stub
		// parent-stub laden ? oder auch beim login ?
		// 
	}

	private static final long serialVersionUID = -7940206816319176143L;
	
	public void login(String username, String password) throws RemoteException {
		LoginPacket login = new LoginPacket(username, password);
		auth = parent.registerClient(login, this);
		// TODO: insertMessages(List<Message> messages) für die GUI implementieren
		// gui.insertMessages(parent.getMessages(auth));
	}
	
	public void addUser(String newUsername, String newPassword) throws RemoteException {
		// TODO: muss hier nicht noch das Passwort verschlüsselt werden ?
		// im Moment wird ein Userobjekt beim Server erstellt, der das Passwort dann wieder in Klartext der DB gibt
		// entweder könnte ja das Passwort nur noch verschlüsselt rausgegeben werden (hab ich das so richtig verstanden, siehe UserImpl)
		// oder wir könnten gleich hier schon das Passwort hashen, so dass der server den klartext nicht mitkriegt
		// beim login kriegt er natürlich klartext und muss in selber zerhacken und schauen ob derselbe hash dabei herauskommt.
		parent.addUser(auth, newUsername, newPassword);
	}
	
	public void sendMessage(String msg) throws RemoteException {
		parent.newMessage(auth, msg);
	}
	
	public void editMessage(Message msg, String text) throws RemoteException {		
		msg.changeMessage(text);
		parent.editMessage(auth, msg);
	}
	
	public void publishMessage(Message msg) throws RemoteException {
		parent.publish(auth, msg);
	}
	
	public void deleteMessage(Message msg) throws RemoteException {
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
}
