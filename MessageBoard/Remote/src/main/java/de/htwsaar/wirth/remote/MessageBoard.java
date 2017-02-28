package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface MessageBoard extends Remote {
	
	AuthPacket registerClient(LoginPacket login, Notifiable client) throws RemoteException;
	void addUser(AuthPacket auth, String newUsername, String newPassword) throws RemoteException;
	void publish(AuthPacket auth, Message msg) throws RemoteException;
	void newMessage(AuthPacket auth, String msg) throws RemoteException;
	void editMessage(AuthPacket auth, Message msg) throws RemoteException;
	void deleteMessage(AuthPacket auth, Message msg) throws RemoteException;
	List<Message> getMessages(AuthPacket auth) throws RemoteException;
	List<String> getUsers(AuthPacket auth) throws RemoteException;
	void deleteUser(AuthPacket auth, String username) throws RemoteException;
	
}
