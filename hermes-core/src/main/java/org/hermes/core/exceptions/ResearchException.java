package org.hermes.core.exceptions;

/**
 * Created by Almaz
 * Date: 13.10.2015
 */
public class ResearchException extends HermesException{

    public ResearchException(String message) {
        super(message);
    }

    public ResearchException(Exception cause) {
        super(cause);
    }
}
