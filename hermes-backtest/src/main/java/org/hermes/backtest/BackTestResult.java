package org.hermes.backtest;

import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.position.Order;
import org.hermes.core.research.ResearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Almaz on 27.09.2015.
 */
public class BackTestResult extends ResearchResult {

    private BackTestStrategy strategy;
    private List<Order> orders;
    private QuoteHistory quoteHistory;

    public BackTestResult(BackTestStrategy strategy) {
        super(strategy.getBackTester().getBackTestDataProvider().getQuoteHistory().getQuote());
        this.strategy = strategy;
        this.properties = new HashMap<>();
        this.properties.putAll(strategy.getProperties());
        this.orders = new ArrayList<>();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setQuoteHistory(QuoteHistory quoteHistory) {
        this.quoteHistory = quoteHistory;
    }

    public QuoteHistory getQuoteHistory() {
        return quoteHistory;
    }

    public BackTestStrategy getStrategy() {
        return strategy;
    }
}
