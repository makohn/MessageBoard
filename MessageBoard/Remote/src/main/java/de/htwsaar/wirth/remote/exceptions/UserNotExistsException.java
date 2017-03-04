package de.htwsaar.wirth.remote.exceptions;

public class UserNotExistsException extends RuntimeException {

    private static final long serialVersionUID = 8634303583226178903L;

    public UserNotExistsException() {
        super();
    }

    public UserNotExistsException(String errorMsg) {
        super(errorMsg);
    }

}