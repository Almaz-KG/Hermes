package org.hermes.core.exceptions;

/**
 * Created by Almaz
 * Date: 15.10.2015
 */
public class TradingException extends HermesException{

    public TradingException(String message) {
        super(message);
    }

    public TradingException(Exception cause) {
        super(cause);
    }
}
