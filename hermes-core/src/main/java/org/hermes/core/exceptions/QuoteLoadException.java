package org.hermes.core.exceptions;

/**
 * Created by Almaz on 14.05.2017.
 */
public class QuoteLoadException extends ResourceLoadException {

    public QuoteLoadException(String message, Exception e) {
        super(message, e);
    }
}
