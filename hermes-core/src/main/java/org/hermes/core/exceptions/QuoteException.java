package org.hermes.core.exceptions;

import org.hermes.core.entities.constants.Quote;

/**
 * Created by Almaz
 * Date: 17.10.2015
 */
public class QuoteException extends HermesException {

    private Quote quote;

    public QuoteException(String message, Quote quote) {
        super(message);

        this.quote = quote;
    }

    public QuoteException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return
            quote != null ?
           "UnknownQuoteException for: " + quote + super.getMessage()
                    : super.getMessage();
    }
}
