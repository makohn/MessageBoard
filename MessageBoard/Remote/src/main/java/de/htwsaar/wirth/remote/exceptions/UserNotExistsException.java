package de.htwsaar.wirth.remote.exceptions;

public class UserNotExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 7372645124047139717L;

	public UserNotExistsException() {
        super();
    }

    public UserNotExistsException(String errorMsg) {
        super(errorMsg);
    }
}
