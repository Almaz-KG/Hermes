package org.hermes.backtest.exceptions;

import org.hermes.core.exceptions.HermesException;

/**
 * Created by Almaz
 * Date: 15.10.2015
 */
public class BackTestException extends HermesException {


    public BackTestException(Exception cause) {
        super(cause);
    }

    public BackTestException(String message) {
        super(message);
    }
}
