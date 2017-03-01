package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface ParentServer extends Remote {

	void registerServer(Notifiable childServer) throws RemoteException;
	void publish(Message msg)  throws RemoteException;
	// TODO: void notifyServerEdit(String msg, UUID id) throws RemoteException
	// TODO:	void notifyServerDelete(UUID id) throws RemoteException
	void notifyServerEdit (Message msg) throws RemoteException;
	void notifyServerDelete (Message msg) throws RemoteException;
	List<Message> getMessages() throws RemoteException;
	
}
