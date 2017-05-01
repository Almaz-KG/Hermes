package org.hermes.backtest.examples.researches;

import org.hermes.backtest.BackTestDataProvider;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.examples.strategies.BarSizeStrategy;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.exceptions.ResearchException;
import org.hermes.core.exceptions.ResourceLoadException;
import org.hermes.core.research.Research;
import org.hermes.core.research.ResearchResult;
import org.hermes.data.provider.integration.ResourceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Almaz
 * Date: 20.10.2015
 */
public class BarSizeResearch implements Research {

    private Quote quote;
    private TimeFrame timeFrame;
    private int startSignalBarPeriod;
    private int finishSignalBarPeriod;
    private double startSignalBarSize;
    private double finishSignalBarSize;

    public BarSizeResearch(Quote quote, TimeFrame timeFrame,
                           int startSignalBarPeriod, int finishSignalBarPeriod,
                           double startSignalBarSize, double finishSignalBarSize) {
        this.quote = quote;
        this.timeFrame = timeFrame;
        this.startSignalBarPeriod = startSignalBarPeriod;
        this.finishSignalBarPeriod = finishSignalBarPeriod;
        this.startSignalBarSize = startSignalBarSize;
        this.finishSignalBarSize = finishSignalBarSize;
    }

    @Override
    public ResearchResult research() throws ResearchException {
        try {
            ResearchResult result = new ResearchResult(quote);
            QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(quote, timeFrame);

            int researchCount = 0;
            for (int i = startSignalBarPeriod; i <= finishSignalBarPeriod; i++) {
                for (double j = startSignalBarSize; j <= finishSignalBarSize; j+= 0.1) {
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("accountOwner", "Almaz Murzabekov");

                    properties.put("signalBarSize", j);
                    properties.put("signalBarPeriod", i);

                    DataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
                    BackTester tester = new BackTester(backTestDataProvider);

                    BarSizeStrategy strategy = new BarSizeStrategy(tester, properties);
                    strategy.setSignalBarPeriod(i);
                    strategy.setSignalBarSize(j);
                    tester.setStrategy(strategy);

                    result.getProperties().put("" + (researchCount++), tester.backTest());
                }
            }

            return result;
        } catch (ResourceLoadException | BackTestException  e) {
            throw new ResearchException(e);
        }
    }
}