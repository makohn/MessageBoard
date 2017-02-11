package de.htwsaar.wirth.server.service;

import de.htwsaar.wirth.server.service.interfaces.MessageService;
import de.htwsaar.wirth.server.service.interfaces.UserService;

/**
 * This is a Service-singleton, which can be used to access the database-services
 */
public class Services {

	private static Services instance;

	private MessageService messageService;
	private UserService userService;

	/**
	 * private singleton-constructor
	 */
	private Services() {
		messageService = new MessageServiceImpl();
		userService = new UserServiceImpl();
	}

	/**
	 * synchronized getInstance Method, which creates a new Instance of the singleton if none exists
	 * otherwise it just returns the instance
	 * @return the singleton instance
	 */
	public synchronized static Services getInstance() {
		if (instance == null) {
			instance = new Services();
		}
		return instance;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public UserService getUserService() {
		return userService;
	}

}
