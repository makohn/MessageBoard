package de.htwsaar.wirth.remote.exceptions;

import java.rmi.RemoteException;

public class NoPermissionException extends RemoteException {
	
	private static final long serialVersionUID = 8634303583226178903L;

	public NoPermissionException() {
		super();
	}
	
	public NoPermissionException(String errorMsg) {
		super(errorMsg);
	}

}
