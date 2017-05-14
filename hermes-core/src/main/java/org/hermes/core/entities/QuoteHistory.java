package org.hermes.core.entities;

import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.exceptions.QuoteException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Almaz on 22.09.2015.
 */
public class QuoteHistory {

    private TimeFrame timeFrame;
    private Quote quote;
    private List<Bar> history;

    public QuoteHistory(Quote quote, TimeFrame timeFrame){
        this.timeFrame = timeFrame;
        this.quote = quote;
        this.history = new ArrayList<>();
    }

    public QuoteHistory(Quote quote, TimeFrame timeFrame, List<Bar> history) {
        this.timeFrame = timeFrame;
        this.quote = quote;
        this.history = history;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    public Quote getQuote() {
        return quote;
    }

    public List<Bar> getHistory() {
        return history;
    }
}
