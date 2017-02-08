package de.htwsaar.wirth.server;

import de.htwsaar.wirth.remote.ParentServer;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * Created by stefanschloesser1 on 08.02.17.
 */
public class Server {
    private String groupName;
    private int portParent;
    private int localPort;
    private String hostParent;
    private MessageBoardImpl messageBoard;

    public Server(String groupName, int localPort) throws RemoteException, AlreadyBoundException, NotBoundException {

        this.groupName = groupName;
        this.localPort = localPort;
        messageBoard = new MessageBoardImpl(groupName);
        createRegistry();
    }

    public Server(String groupName, int localPort, String hostParent, int portParent) throws RemoteException, NotBoundException, AlreadyBoundException {
        this(groupName, localPort);
        this.hostParent = hostParent;
        this.portParent = portParent;
        bindToParent();

    }


    public void createRegistry() throws RemoteException, AlreadyBoundException {
        Registry rg = LocateRegistry.createRegistry(localPort);
        rg.bind("server", messageBoard);
    }

    public void bindToParent() throws RemoteException, NotBoundException {
        Registry parentRegistry = LocateRegistry.getRegistry(hostParent, portParent);
        ParentServer server = (ParentServer) parentRegistry.lookup("server");
        server.registerServer(messageBoard);
    }

}
