package de.htwsaar.wirth.remote.exceptions;

public class AuthenticationException extends RuntimeException {
	
	private static final long serialVersionUID = 584940483247442084L;

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String errorMsg) {
        super(errorMsg);
    }

}