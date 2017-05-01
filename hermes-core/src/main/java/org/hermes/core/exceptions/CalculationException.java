package org.hermes.core.exceptions;

/**
 * Exception class for indicate indicator calculation exception
 *
 * Created by Almaz
 * Date: 13.10.2015
 */
public class CalculationException extends HermesException {

    public CalculationException(String message) {
        super(message);
    }

    public CalculationException(Exception cause) {
        super(cause);
    }
}
