package de.htwsaar.wirth.server;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.NotifiableClient;

public interface ClientNotifyHandler {

    public void handle(NotifiableClient client) throws RemoteException;

}