package de.htwsaar.wirth.remote.exceptions;

import java.rmi.RemoteException;

public class NotLoggedInException extends RemoteException {

	private static final long serialVersionUID = -2351205831297789107L;

	public NotLoggedInException() {
		super();
	}
	
	public NotLoggedInException(String errorMsg) {
		super(errorMsg);
	}

}
