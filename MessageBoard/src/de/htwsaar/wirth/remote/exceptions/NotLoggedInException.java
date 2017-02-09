package de.htwsaar.wirth.remote.exceptions;

public class NotLoggedInException extends RuntimeException {

	private static final long serialVersionUID = -2351205831297789107L;

	public NotLoggedInException() {
		super();
	}
	
	public NotLoggedInException(String errorMsg) {
		super(errorMsg);
	}

}
