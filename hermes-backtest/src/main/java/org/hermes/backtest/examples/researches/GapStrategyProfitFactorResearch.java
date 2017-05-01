package org.hermes.backtest.examples.researches;

import org.hermes.backtest.BackTestDataProvider;
import org.hermes.backtest.BackTestResult;
import org.hermes.backtest.BackTestStrategy;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.examples.strategies.gap.GapStrategy;
import org.hermes.core.exceptions.ResearchException;
import org.hermes.core.research.Research;
import org.hermes.core.research.ResearchResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Подбор коэфициентов TAKE_PROFIT_FACTOR & SIGNAL_BAR_SIZE_FACTOR
 */
public class GapStrategyProfitFactorResearch implements Research {

    private static final Logger logger = Logger.getLogger(GapStrategyProfitFactorResearch.class.getCanonicalName());
    public static final String OPEN_HOUR = "openHour";
    public static final String CLOSE_HOUR = "closeHour";
    public static final String SIGNAL_BAR_SIZE_FACTOR= "signalBarSizeFactor";
    public static final String SIGNAL_BAR_SIZE_FACTOR_START = "signalBarSizeFactorStart";
    public static final String SIGNAL_BAR_SIZE_FACTOR_END = "signalBarSizeFactorEnd";
    public static final String SIGNAL_BAR_SIZE_FACTOR_STEP = "signalBarSizeFactorStep";
    public static final String TAKE_PROFIT_FACTOR = "takeProfitFactor";
    public static final String TAKE_PROFIT_FACTOR_START = "takeProfitFactorStart";
    public static final String TAKE_PROFIT_FACTOR_END = "takeProfitFactorEnd";
    public static final String TAKE_PROFIT_FACTOR_STEP = "takeProfitFactorStep";

    protected int openHour = 16;
    protected int closeHour = 22;

    protected double signalBarSizeFactorStart = 0.1;
    protected double signalBarSizeFactorEnd = 1;
    protected double signalBarSizeFactorStep = 0.1;
    protected double takeProfitFactorStart = 0.5;
    protected double takeProfitFactorEnd = 10;
    protected double takeProfitFactorStep = 0.5;

    protected Map<String, Object> properties;
    protected BackTestStrategy strategy;
    protected BackTestDataProvider dataProvider;

    public GapStrategyProfitFactorResearch(BackTestStrategy strategy, BackTestDataProvider dataProvider, Map<String, Object> properties) {
        this.properties = properties;
        this.strategy = strategy;
        this.dataProvider = dataProvider;
        setProperties();
    }

    private void setProperties() {
        for (String key : properties.keySet()) {
            Class<? extends GapStrategyProfitFactorResearch> aClass = this.getClass();
            try {
                Field field = aClass.getDeclaredField(key);
                field.setAccessible(true);
                field.set(this, properties.get(key));
                field.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.warning(e.getMessage());
            }
        }

        Object openHour = properties.get("openHour");
        if(openHour != null)
            this.openHour = (int) openHour;
        Object closeHour = properties.get("closeHour");
        if(closeHour != null)
            this.closeHour = (int) closeHour;
    }

    public ResearchResult research() throws ResearchException {
        try {

            ResearchResult result = new ResearchResult(dataProvider.getQuote());
            List<BackTestResult> backTestResults = new ArrayList<>();

            GapStrategy gapStrategy;
            BackTestDataProvider backTestDataProvider;
            BackTester backTester;

            long l = System.currentTimeMillis();

            for (double signalBarSizeFactor = signalBarSizeFactorStart;
                 signalBarSizeFactor <= signalBarSizeFactorEnd; signalBarSizeFactor += signalBarSizeFactorStep) {
                long timeMillis = System.currentTimeMillis();

                for (double takeProfitFactor = takeProfitFactorStart;
                     takeProfitFactor <= takeProfitFactorEnd; takeProfitFactor += takeProfitFactorStep) {
                    backTestDataProvider = new BackTestDataProvider(dataProvider.getQuoteHistory());
                    backTester = new BackTester(backTestDataProvider);

                    gapStrategy = new GapStrategy(strategy.getStrategyName(), backTester,
                            strategy.getProperties(), openHour, closeHour);

                    gapStrategy.setTakeProfitFactor(takeProfitFactor);
                    gapStrategy.setSignalBarSizeFactor(signalBarSizeFactor);


                    BackTestResult backTestResult = backTester.backTest();
                    backTestResult.getProperties().put(TAKE_PROFIT_FACTOR, takeProfitFactor);
                    backTestResult.getProperties().put(SIGNAL_BAR_SIZE_FACTOR, signalBarSizeFactor);
                    backTestResults.add(backTestResult);
                }

                logger.info("Signal bar size factor: " + String.format("%.2f", signalBarSizeFactor) + " finished" +
                        "\nTime: " + (System.currentTimeMillis() - timeMillis) + " mc");
            }

            logger.info("Total time: " + (System.currentTimeMillis() - l));
            backTestResults.sort((o1, o2) -> (int) (((double) o2.getProperty("profitFactor") - (double) o1.getProperty("profitFactor")) * 100));

            result.getProperties().put("backtestResults", backTestResults);
            return result;
        } catch (Exception ex){
            throw new ResearchException(ex);
        }
    }
}
