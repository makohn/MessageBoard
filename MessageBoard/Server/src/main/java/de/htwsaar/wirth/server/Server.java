package de.htwsaar.wirth.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.UserImpl;
import de.htwsaar.wirth.remote.model.interfaces.User;
import de.htwsaar.wirth.remote.util.HashUtil;
import de.htwsaar.wirth.server.dao.PersistenceManager;
import de.htwsaar.wirth.server.service.Services;

/**
 * Class {@code Server} serves as a initializer for all the server-side services.
 * As such, it initializes the database access, creates the registry and binds 
 * a {@code MessageBoard} instance to it. 
 * If the created MessageBoard instance is a new one, a group leader has to be created.
 * If the created MessageBoard instance is child of another one, {@code Server} tries
 * to establish the connection between both.
 * 
 */
public class Server {

	private static final Logger logger = LogManager.getLogger(Server.class);
	
	private String parentGroupName;
	private String groupName;
	private String parentHost;
	private MessageBoardImpl messageBoard;

	private static final int REGISTRY_PORT = 1099;
	private static final String HORIZONTAL_LINE = "**********************************************";

	public Server(String groupName, int localPort, boolean isRoot) throws RemoteException, AlreadyBoundException {
		PersistenceManager.setDatabaseNameSuffix(groupName);
		this.groupName = groupName;
		messageBoard = new MessageBoardImpl(groupName, localPort, isRoot);
		createRegistry();
		checkGroupLeader();
	}
	
	/**
	 * Creates a Server instance that is bound to a parent server.
	 * @param groupName - the group name of this instance
	 * @param localPort - the port this instance is running on
	 * @oaram isRoot - if this Server is the root server
	 * @param parentHost - the address of the parent server
	 * @param parentGroupName - the name of the parent server's group
	 * @throws RemoteException, NotBoundException, AlreadyBoundException
	 */
	public Server(String groupName, int localPort, boolean isRoot, String parentHost, String parentGroupName)
			throws RemoteException, NotBoundException, AlreadyBoundException {
		this(groupName, localPort, isRoot);
		this.parentHost = parentHost;
		this.parentGroupName = parentGroupName;
		bindToParent();
	}

	/**
	 * Checks whether the instantiated {@code MessageBoard} instance has a group leader. 
	 * Unless this is complied, the user is prompted to put in a username and a password
	 * to become the group leader of this instance.
	 */
	private void checkGroupLeader() {
		if (!Services.getInstance().getUserService().existsGroupLeader()) {
			logger.info("No groupleader found. Prompting user for input.");
			Scanner s = new Scanner(System.in);
			System.out.println(HORIZONTAL_LINE);
			System.out.print("Username of Groupleader: ");
			String username = s.nextLine();
			System.out.print("Password: ");
			String password = s.nextLine();
			s.close();
			System.out.println(HORIZONTAL_LINE);
			password = HashUtil.hashSha512(password);

			User groupLeader = new UserImpl(username, "", "", password, true);

			Services.getInstance().getUserService().saveUser(groupLeader);
		}
	}

	/**
	 * Creates the RMI registry and binds a {@code MessageBoard} instance to it.
	 * @throws RemoteException, AlreadyBoundException
	 */
	private void createRegistry() throws RemoteException, AlreadyBoundException {
		Registry registry;
		try {
			registry = LocateRegistry.createRegistry(REGISTRY_PORT);
			logger.info("Registry running on port "  + REGISTRY_PORT);
			registry.bind(groupName, messageBoard);
			logger.info("MessageBoard successfully bound to registry");
		} catch (ExportException e) {
			registry = LocateRegistry.getRegistry();
			registry.bind(groupName, messageBoard);
		}
	}

	/**
	 * Binds a {@code MessageBoard} instance to a parent server instance and
	 * vice versa.
	 * @throws RemoteException, NotBoundException
	 */
	public void bindToParent() throws RemoteException, NotBoundException {
		Registry parentRegistry = LocateRegistry.getRegistry(parentHost);
		ParentServer parent = (ParentServer) parentRegistry.lookup(parentGroupName);
		parent.registerServer(messageBoard);
		messageBoard.setParent(parent);
	}
}
