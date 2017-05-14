package org.hermes.backtest.examples.researches;

import org.hermes.backtest.BackTestDataProvider;
import org.hermes.backtest.BackTestResult;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.examples.strategies.ma.SimpleMovingAverageStrategy;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.backtest.filters.DateAfterFilter;
import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.exceptions.ResearchException;
import org.hermes.core.research.Research;
import org.hermes.core.research.ResearchResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Almaz
 * Date: 13.10.2015
 *
 * Try to identify fast & slow SMA periods
 */
public class SimpleMovingAverageResearch implements Research {
    private static final Logger logger = Logger.getLogger(SimpleMovingAverageResearch.class.getCanonicalName());
    private QuoteHistory quoteHistory;
    private int slowSMAMinPeriod;
    private int slowSMAMaxPeriod;
    private int fastSMAMaxPeriod;
    private int fastSMAMinPeriod;

    public SimpleMovingAverageResearch(QuoteHistory quoteHistory, int slowSMAMinPeriod,
                                       int slowSMAMaxPeriod, int fastSMAMinPeriod, int fastSMAMaxPeriod) {
        this.quoteHistory = quoteHistory;
        this.slowSMAMinPeriod = slowSMAMinPeriod;
        this.slowSMAMaxPeriod = slowSMAMaxPeriod;
        this.fastSMAMaxPeriod = fastSMAMaxPeriod;
        this.fastSMAMinPeriod = fastSMAMinPeriod;
    }

    public ResearchResult research()throws ResearchException {
        ResearchResult researchResult = new ResearchResult(quoteHistory.getQuote());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse("2010.01.01");
        } catch (ParseException e) {
            throw new ResearchException(e);
        }
        DateAfterFilter filter = new DateAfterFilter(date);

        int researchCount = 0;
        for (int i = slowSMAMinPeriod; i < slowSMAMaxPeriod; i++) {
            for (int j = fastSMAMinPeriod; j < fastSMAMaxPeriod; j++) {
                if(j >= i)
                    break;
                try {
                    logger.info("i: " + i + " j: " + j);
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("accountOwner", "Almaz Murzabekov");
                    properties.put("accountName", "Gold data D1");

                    properties.put("fastSMA", j);
                    properties.put("slowSMA", i);

                    BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory, filter);
                    BackTester tester = new BackTester(backTestDataProvider);

                    SimpleMovingAverageStrategy strategy = new SimpleMovingAverageStrategy(
                            "Simple sma strategy: buy if fast sma crossover slow sma UP, sell if fast sma crossover slow sma DOWN",
                            tester,
                            properties);
                    tester.setStrategy(strategy);

                    BackTestResult backTestResult = tester.backTest();
                    researchResult.getProperties().put("" + (researchCount++), backTestResult);
                } catch (BackTestException e) {
                    throw new ResearchException(e);
                }
            }
        }

        return researchResult;
    }
}
