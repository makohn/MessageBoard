package de.htwsaar.wirth.remote.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 3147629818918857395L;

	public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(String errorMsg) {
        super(errorMsg);
    }

}
