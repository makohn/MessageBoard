package de.htwsaar.wirth.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.UserImpl;
import de.htwsaar.wirth.remote.model.interfaces.User;
import de.htwsaar.wirth.server.service.Services;

/**
 * A Server binds
 */
public class Server {
	
    private static final String BIND_KEY = "server";
    
    private int parentPort;
    private int localPort;
    private String parentHost;
    private MessageBoardImpl messageBoard;
    

    public Server(String groupName, int localPort) throws RemoteException, AlreadyBoundException {
        this.localPort = localPort;
        messageBoard = new MessageBoardImpl(groupName);
        createRegistry();
        checkGroupLeader();
    }

    private void checkGroupLeader() {
    	if (!Services.getInstance().getUserService().existsGroupLeader()) {
    		
    		Scanner s = new Scanner(System.in);
    		String username = s.nextLine();
    		String password = s.nextLine();
    		s.close();
    		
    		User groupLeader = new UserImpl(username, "", "", password, true);
    		
    		Services.getInstance().getUserService().saveUser(groupLeader);
    	}
	}

	public Server(String groupName, int localPort, String parentHost, int parentPort) throws RemoteException, NotBoundException, AlreadyBoundException {
        this(groupName, localPort);
        this.parentHost = parentHost;
        this.parentPort = parentPort;
        bindToParent();
    }


    private void createRegistry() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(localPort);
        registry.bind(BIND_KEY, messageBoard);
    }

    public void bindToParent() throws RemoteException, NotBoundException {
        Registry parentRegistry = LocateRegistry.getRegistry(parentHost, parentPort);
        ParentServer parent = (ParentServer) parentRegistry.lookup(BIND_KEY);
        parent.registerServer(messageBoard);
        messageBoard.setParent(parent);
    }

}
