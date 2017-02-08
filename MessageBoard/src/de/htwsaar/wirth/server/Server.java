package de.htwsaar.wirth.server;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * A Server binds
 */
public class Server {
    private String groupName;
    private int parentPort;
    private int localPort;
    private String parentHost;
    private MessageBoardImpl messageBoard;

    public Server(String groupName, int localPort) throws RemoteException, AlreadyBoundException, NotBoundException {

        this.groupName = groupName;
        this.localPort = localPort;
        messageBoard = new MessageBoardImpl(groupName);
        createRegistry();
    }

    public Server(String groupName, int localPort, String parentHost, int parentPort) throws RemoteException, NotBoundException, AlreadyBoundException {
        this(groupName, localPort);
        this.parentHost = parentHost;
        this.parentPort = parentPort;
        bindToParent();
    }


    private void createRegistry() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(localPort);
        registry.bind("server", messageBoard);
    }

    public void bindToParent() throws RemoteException, NotBoundException {
        Registry parentRegistry = LocateRegistry.getRegistry(parentHost, parentPort);
        ParentServer parent = (ParentServer) parentRegistry.lookup("server");
        parent.registerServer(messageBoard);
        messageBoard.addParent(parent);
    }

}
