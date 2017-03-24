package de.htwsaar.wirth.remote.exceptions;

public class UserAlreadyLoggedInException extends RuntimeException {

	private static final long serialVersionUID = 584940483247442084L;

    public UserAlreadyLoggedInException() {
        super();
    }

    public UserAlreadyLoggedInException(String errorMsg) {
        super(errorMsg);
    }

}