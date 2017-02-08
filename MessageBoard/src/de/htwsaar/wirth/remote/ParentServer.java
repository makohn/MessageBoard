package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface ParentServer extends Remote {
	
	public void notifyServerDelete (Message msg) throws RemoteException;
	public void notifyServerEdit (Message msg) throws RemoteException;
	public void publish(Message msg)  throws RemoteException;
	public void registerServer(Notifiable childServer) throws RemoteException;
	public List<Message> getMessages() throws RemoteException;
	
}
