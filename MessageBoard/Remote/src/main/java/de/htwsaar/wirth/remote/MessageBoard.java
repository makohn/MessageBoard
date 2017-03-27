package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.htwsaar.wirth.remote.model.Status;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;

/**
 * Class {@code MessageBoard} contains all methodes which the client can call on the server.
 */
public interface MessageBoard extends Remote {
	
	/* Messages */
	void publish(AuthPacket auth, UUID id) throws RemoteException;
	void newMessage(AuthPacket auth, String msg) throws RemoteException;
	void editMessage(AuthPacket auth, String msg, UUID id) throws RemoteException;
	void deleteMessage(AuthPacket auth, UUID id) throws RemoteException;
	List<Message> getMessages(AuthPacket auth) throws RemoteException;
	
	/* Users and UserStatus */
	AuthPacket login(LoginPacket login, NotifiableClient client) throws RemoteException;
	void logout(AuthPacket auth) throws RemoteException;
	
	void addUser(AuthPacket auth, String newUsername, String newPassword) throws RemoteException;
	void changeUserStatus(AuthPacket auth, Status status) throws RemoteException;
	Map<String, Status> getUserStatus(AuthPacket auth) throws RemoteException;
	void deleteUser(AuthPacket auth, String username) throws RemoteException;
}
