package org.hermes.core.exceptions;

/**
 * Created by Almaz
 * Date: 01.11.2015
 */
public class IndicatorInitializeException extends HermesException{

    public IndicatorInitializeException(Exception cause) {
        super(cause);
    }

    public IndicatorInitializeException(String message) {
        super(message);
    }

}
