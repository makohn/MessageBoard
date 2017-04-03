package de.htwsaar.wirth.server;


import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.Scanner;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.UserImpl;
import de.htwsaar.wirth.remote.model.interfaces.User;
import de.htwsaar.wirth.remote.util.HashUtil;
import de.htwsaar.wirth.server.dao.PersistenceManager;
import de.htwsaar.wirth.server.service.Services;

/**
 * A Server binds
 */
public class Server {
 
    private String parentGroupName;
    private String groupName;
    private String parentHost;
    private MessageBoardImpl messageBoard;
    

    public Server(String groupName, int localPort, boolean isRootServer) throws RemoteException, AlreadyBoundException {
        PersistenceManager.setDatabaseNameSuffix(groupName);
        this.groupName = groupName;
        messageBoard = new MessageBoardImpl(groupName, localPort, isRootServer);
        createRegistry();
        checkGroupLeader();
    }

    private void checkGroupLeader() {
    	if (!Services.getInstance().getUserService().existsGroupLeader()) {
    		
    		Scanner s = new Scanner(System.in);
    		System.out.print("Username of Groupleader: ");
    		String username = s.nextLine();
    		System.out.print("Password: ");
    		String password = s.nextLine();
    		s.close();
    		
    		password = HashUtil.hashSha512(password);
    		    		
    		User groupLeader = new UserImpl(username, "", "", password, true);
    		
    		Services.getInstance().getUserService().saveUser(groupLeader);
    	}
	}

	public Server(String groupName, int localPort, boolean isRootServer, String parentHost, String parentGroupName) throws RemoteException, NotBoundException, AlreadyBoundException {
        this(groupName, localPort, isRootServer);
        this.parentHost = parentHost;
        this.parentGroupName = parentGroupName;
        bindToParent();
    }


    private void createRegistry() throws RemoteException, AlreadyBoundException {
    	Registry registry;
    	try {
    		registry = LocateRegistry.createRegistry(1099);
            registry.bind(groupName, messageBoard);
    	} catch (ExportException e) {
    		registry = LocateRegistry.getRegistry();
            registry.bind(groupName, messageBoard);
    	}
    }

    public void bindToParent() throws RemoteException, NotBoundException {
        Registry parentRegistry = LocateRegistry.getRegistry(parentHost);
        ParentServer parent = (ParentServer) parentRegistry.lookup(parentGroupName);
        parent.registerServer(messageBoard);
        messageBoard.setParent(parent);
    }
}
