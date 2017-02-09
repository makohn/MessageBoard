package de.htwsaar.wirth.remote.exceptions;

import java.rmi.RemoteException;

public class AuthenticationException extends RemoteException {
	
	private static final long serialVersionUID = 584940483247442084L;

	public AuthenticationException() {
		super();
	}
	
	public AuthenticationException(String errorMsg) {
		super(errorMsg);
	}

}
