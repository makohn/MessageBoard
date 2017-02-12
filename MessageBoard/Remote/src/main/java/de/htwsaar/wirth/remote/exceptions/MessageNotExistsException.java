package de.htwsaar.wirth.remote.exceptions;

public class MessageNotExistsException extends RuntimeException {


    private static final long serialVersionUID = 2045668584374429040L;

    public MessageNotExistsException() {
        super();
    }

    public MessageNotExistsException(String errorMsg) {
        super(errorMsg);
    }

}