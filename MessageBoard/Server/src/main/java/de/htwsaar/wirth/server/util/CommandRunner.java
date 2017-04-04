package de.htwsaar.wirth.server.util;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.server.Server;
import de.htwsaar.wirth.server.util.command.Command;
import de.htwsaar.wirth.server.util.command.parent.ParentCommand;

public class CommandRunner extends Thread {

	private static final Logger logger = LogManager.getLogger(CommandRunner.class);
	
	private LinkedBlockingDeque<Command> queue;
	
	private Server thisServer;
	private ParentServer parent;
	private boolean connected;
	
	private final CommandRunner self;
	
	private ExecutorService exec = Executors.newSingleThreadExecutor();

	public CommandRunner() {
		this(null);
	}
	
	public CommandRunner(Server server) {
		this.queue = new LinkedBlockingDeque<>();
		this.thisServer = server;
		self = this;
	}

	/**
	 * Versuche Command c in der Queue abzulegen Falls die BlockingQueue mit
	 * Int.MaxValue-Commands belegt ist, blockiert dieser Call
	 * 
	 * @param c
	 */
	public void addCommand(Command c) {
		try {
			queue.put(c);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void run() {
		Command toExecute = null;
		while (true) {
			try {
				System.out.println(queue.getFirst());
				toExecute = queue.take();
				toExecute.execute();
			
			} catch (RemoteException rEx) {
				// Wenn eine RemoteException geworfen wurde, wurde das Command
				// nicht vollständig ausgeführt
				// schreibe es zurück in die Queue
				if(toExecute instanceof ParentCommand) {
					logger.error("Connection to parent lost.");
					exec.execute(reconnector);
					waitWhileNotConnected();
					((ParentCommand) toExecute).setNewParent(parent);
				}
				queue.addFirst(toExecute); 

			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			} catch (Exception otherExc) {
				// Die CommandQueue darf nicht durch irgendeine unerwartete Exception beendet werden
				// Vlt sollte später irgendeine Instanz überwachen, 
				// ob alle CommandQueues noch laufen und falls nicht diese gegebenenfalls neustarten
			}
		}
	}

	public void waitWhileNotConnected() {
		try {
			synchronized (self) {
				self.wait();
			}
		} catch (InterruptedException ignore) {
			// log.debug("interrupted: " + ignore.getMessage());
		}
	}
	
	private Thread reconnector = new Thread() {
		public void run() {
			while (!connected) {
				try {
					parent = thisServer.bindToParent();
					connected = true;
				} catch (RemoteException | NotBoundException e) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
					}
				}
			}
			synchronized (self) {
				self.notify();
			}
		}
	};
}
