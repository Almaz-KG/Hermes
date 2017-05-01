package org.hermes.backtest.exceptions;

import org.hermes.core.exceptions.HermesException;

/**
 * Created by Almaz
 * Date: 16.10.2015
 */
public class ReportBuilderException extends HermesException {

    public ReportBuilderException(String message) {
        super(message);
    }

    public ReportBuilderException(Exception cause) {
        super(cause);
    }
}

