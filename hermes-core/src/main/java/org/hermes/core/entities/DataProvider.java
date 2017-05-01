package org.hermes.core.entities;


import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.filters.MarketBookFilter;
import org.hermes.core.entities.price.Bar;

/**
 * Created by Almaz
 * Date: 31.10.2015
 */
public interface DataProvider {

    void nextBar();

    Bar getCurrentBar();

    Bar getBar(int index);

    boolean hasMoreData();

    QuoteHistory getQuoteHistory();

    Quote getQuote();

    void setFilter(MarketBookFilter filter);

    int indexOf(Bar bar);

    int getReadedBars();

    default int size(){
        return getQuoteHistory().getHistory().size();
    }

}
