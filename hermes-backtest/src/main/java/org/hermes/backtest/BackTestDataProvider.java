package org.hermes.backtest;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.filters.AcceptAllFilter;
import org.hermes.core.entities.filters.MarketBookFilter;
import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.Bar;

/**
 * Created by Almaz on 01.10.2015.
 */
public class BackTestDataProvider implements DataProvider {

    private QuoteHistory quoteHistory;
    private MarketBookFilter filter = new AcceptAllFilter();
    private int currentBarIndex;

    public BackTestDataProvider(QuoteHistory quoteHistory) {
        this.quoteHistory = quoteHistory;
    }

    public BackTestDataProvider(QuoteHistory quoteHistory, MarketBookFilter filter) {
        this.quoteHistory = quoteHistory;
        this.filter = filter;
    }

    @Override
    public boolean hasMoreData() {
        if(currentBarIndex >= quoteHistory.getHistory().size() - 1)
            return false;

        for (int i = currentBarIndex; i < quoteHistory.getHistory().size(); i++) {
            if(filter.accept(quoteHistory.getHistory().get(i)))
                return true;
        }

        return false;
    }

    @Override
    public void nextBar(){
        if(currentBarIndex >= quoteHistory.getHistory().size() - 1)
            return;

        do{
            boolean accept = this.filter.accept(quoteHistory.getHistory().get(currentBarIndex + 1));
            if(accept){
                currentBarIndex++;
                return;
            }
            currentBarIndex++;
        } while (currentBarIndex > quoteHistory.getHistory().size());
    }

    @Override
    public Bar getCurrentBar(){
        return quoteHistory.getHistory().get(currentBarIndex);
    }

    /***
     *  Returns bar in history list with
     *  actual index = (current index - param index)
     *
     * @param index
     * @return historical bar
     */
    @Override
    public Bar getBar(int index){
        if(index < 0)
            return null;

        // You cannot see the future
        if(index > currentBarIndex)
            return null;

        Bar bar = quoteHistory.getHistory().get(index);
        return this.filter.accept(bar) ? bar : null;
    }

    public Bar getLastBar(){
        for (int i = quoteHistory.getHistory().size() - 1; i >= 0; i--) {
            if(this.filter.accept(quoteHistory.getHistory().get(i)))
                return quoteHistory.getHistory().get(i);
        }

        return null;
    }

    public Bar getFirstBar(){
        for (int i = 0; i < quoteHistory.getHistory().size(); i++) {
            if(this.filter.accept(quoteHistory.getHistory().get(i)))
                return quoteHistory.getHistory().get(i);
        }
        return null;
    }

    @Override
    public QuoteHistory getQuoteHistory() {
        return quoteHistory;
    }

    @Override
    public Quote getQuote(){
        return quoteHistory.getQuote();
    }

    @Override
    public void setFilter(MarketBookFilter filter) {
        this.filter = filter;
    }

    @Override
    public int indexOf(Bar bar) {
        return this.quoteHistory.getHistory().indexOf(bar);
    }

    @Override
    public int getReadedBars() {
        return currentBarIndex;
    }
}
