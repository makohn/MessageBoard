package de.htwsaar.wirth.remote.exceptions;

public class NoPermissionException extends RuntimeException {
	
	private static final long serialVersionUID = 8634303583226178903L;

	public NoPermissionException() {
		super();
	}
	
	public NoPermissionException(String errorMsg) {
		super(errorMsg);
	}

}
