package de.htwsaar.wirth.server.util;

import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingDeque;

import de.htwsaar.wirth.server.util.command.Command;

public class CommandRunner extends Thread {

	private LinkedBlockingDeque<Command> queue;

	public CommandRunner() {
		queue = new LinkedBlockingDeque<>();
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
				toExecute = queue.take();
				toExecute.execute();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			} catch (RemoteException rEx) {
				// Wenn eine RemoteException geworfen wurde, wurde das Command
				// nicht vollständig ausgeführt
				// schreibe es zurück in die Queue
				queue.addFirst(toExecute);
			} catch (Exception otherExc) {
				// Die CommandQueue darf nicht durch irgendeine unerwartete Exception beendet werden
				// Vlt sollte später irgendeine Instanz überwachen, 
				// ob alle CommandQueues noch laufen und falls nicht diese gegebenenfalls neustarten
			}
		}
	}

}
