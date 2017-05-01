package org.hermes.core.exceptions;


public class HermesException extends Exception{

    public HermesException(String message, Exception cause) {
        super(message, cause);
    }

    public HermesException(Exception cause) {
        super(cause);
    }

    public HermesException(String message) {
        super(message);
    }
}
