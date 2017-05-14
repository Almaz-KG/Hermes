package org.hermes.backtest.examples;

import org.hermes.backtest.*;
import org.hermes.backtest.examples.strategies.BarSizeStrategy;
import org.hermes.backtest.examples.strategies.gap.*;
import org.hermes.backtest.examples.strategies.gap.larrywillams.MondayGapStrategy;
import org.hermes.backtest.examples.strategies.ma.SimpleMovingAverageStrategy;
import org.hermes.backtest.examples.strategies.oscillators.SimpleRSIStrategy;
import org.hermes.backtest.filters.DateAfterFilter;
import org.hermes.backtest.reports.HtmlBackTestReportBuilder;
import org.hermes.backtest.reports.ReportBuilder;
import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.filters.MarketBookFilter;
import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.exceptions.HermesException;
import org.hermes.core.exceptions.TradingException;
import org.hermes.data.provider.integration.ProjectProperties;
import org.hermes.data.provider.integration.ResourceManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Almaz on 01.10.2015.
 */
public class BackTestRunner {
    private static Logger logger = Logger.getLogger(BackTestRunner.class.getCanonicalName());
    public static final String DEFAULT_OUTPUT_REPORT_DIRECTORY = ProjectProperties.getInstance().getReportHomeDirectory();

    public static void main(String[] args) throws Exception {
        logger.info("Start backtesting");

//        runSimpleRSIStrategy();
//        runBarSizeStrategy();
        runSMAStrategy();
//        runGapStrategy();
//        runGapStrategyWithHourBarAccept();
//        runGapStrategySellOnlyWithHourBarAccept();
//        runGapStrategyBuyOnlyWithHourBarAccept();
//        runMondayGapStrategy();
//        runGapStrategyBuyOnly();
//        runGapStrategySellOnly();
//        runSimpleGapStrategy();

        logger.info("Back testing successfully finished");
    }

    private static void runSimpleRSIStrategy() throws Exception {
        logger.info("Start backtesting for SimpleRSIStrategy");
        Quote quote = Quote.XAUUSD;
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(quote, TimeFrame.Day1);

        DataProvider dataProvider = new BackTestDataProvider(quoteHistory);
        BackTester backTester = new BackTester(dataProvider);
        SimpleRSIStrategy strategy = new SimpleRSIStrategy(backTester, getDefaultProperties(quoteHistory));
        backTester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "simple_rsi_strategy_" + quote.toString() + "_d1.html");
        runStrategy(strategy, backTester, path);
        logger.info("Backtesting for SimpleRSIStrategy successfully finished");
    }

    private static void runBarSizeStrategy() throws Exception {
        logger.info("Start backtesting for BarSizeStrategy");
        Quote quote = Quote.XAUUSD;
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(quote, TimeFrame.Day1);
        BackTestDataProvider dataProvider = new BackTestDataProvider(quoteHistory);
        BackTester backTester = new BackTester(dataProvider);
        BarSizeStrategy strategy = new BarSizeStrategy(backTester, getDefaultProperties(quoteHistory));
        backTester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "bar_size_strategy_" + quote.toString() + "_d1.html");
        runStrategy(strategy, backTester, path);
        logger.info("Backtesting for BarSizeStrategy successfully finished");
    }

    private static void runSMAStrategy() throws Exception {
        logger.info("Start backtesting for SimpleMovingAverageStrategy");
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.XAUUSD, TimeFrame.Day1);
        BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
        BackTester tester = new BackTester(backTestDataProvider);

        Map<String, Object> properties = getDefaultProperties(quoteHistory);
        properties.put("accountName", "Gold data D1");
        properties.put("fastSMA", 115);
        properties.put("slowSMA", 171);

        BackTestStrategy strategy = new SimpleMovingAverageStrategy(
                "Simple sma strategy: buy if fast sma crossover slow sma UP, sell if fast sma crossover slow sma DOWN",
                tester, properties);
        tester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "simple_sma_strategy_(gold_d1).html");
        runStrategy(strategy, tester, path);
        logger.info("Backtesting for SimpleMovingAverageStrategy successfully finished");
    }

    private static void runGapStrategy() throws Exception{
        logger.info("Start backtesting for GapStrategy");
        List<QuoteHistory> stockHistories = ResourceManager.getQuoteHistories(Quote.US_Stocks, TimeFrame.Day1);
        List<BackTestResult> backTestResults = new ArrayList<>();

        for (QuoteHistory quoteHistory : stockHistories) {
            BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
            BackTester tester = new BackTester(backTestDataProvider);

            Map<String, Object> properties = getDefaultProperties(quoteHistory);
            properties.put("quote", quoteHistory.getQuote());
            properties.put("accountName", quoteHistory.getQuote() + " " + quoteHistory.getTimeFrame());


            GapStrategy strategy = new GapStrategy("Simple gap strategy", tester, properties);
            tester.setStrategy(strategy);
            backTestResults.add(tester.backTest());
        }

        backTestResults = filter(backTestResults);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "simple_gap_strategy.html");
        ReportBuilder resultBuilder = new HtmlBackTestReportBuilder(backTestResults);
        resultBuilder.build();

        save(resultBuilder, path);


        logger.info("Backtesting for GapStrategy successfully finished");
    }

    private static void runSimpleGapStrategy()throws Exception{
        logger.info("Start backtesting for GapStrategyBuyAndSell");
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.XAUUSD, TimeFrame.Day1);
        BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
        BackTester tester = new BackTester(backTestDataProvider);

        BackTestStrategy strategy = new GapStrategyBuyAndSell(
                "Simple gap strategy: buy if open price > close price, sell if open price < close price ",
                tester,
                getDefaultProperties(quoteHistory));
        tester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "simple_gap_strategy_(gold_d1).html");
        runStrategy(strategy, tester, path);
        logger.info("Backtesting for GapStrategyBuyAndSell successfully finished");
    }

    private static void runGapStrategySellOnly()throws Exception{
        logger.info("Start backtesting for GapStrategySellOnly");
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.XAUUSD, TimeFrame.Day1);
        BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
        BackTester tester = new BackTester(backTestDataProvider);

        BackTestStrategy strategy = new GapStrategySellOnly(
                "Simple gap strategy sell only: sell if open price > close price",
                tester, getDefaultProperties(quoteHistory));
        tester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "simple_gap_strategy_sell_only_(gold_d1).html");
        runStrategy(strategy, tester, path);
        logger.info("Backtesting for GapStrategySellOnly successfully finished");
    }

    private static void runGapStrategyBuyOnly()throws Exception{
        logger.info("Start backtesting for GapStrategyBuyOnly");
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.XAUUSD, TimeFrame.Day1);
        String starDateString = "2014.01.01";
        Date startDate = new SimpleDateFormat("yyyy.MM.dd").parse(starDateString);
        MarketBookFilter filter = new DateAfterFilter(startDate);
        BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory, filter);
        BackTester tester = new BackTester(backTestDataProvider);

        BackTestStrategy strategy = new GapStrategyBuyOnly(
                "Simple gap strategy buy only: buy if open price > close price",
                tester, getDefaultProperties(quoteHistory));
        tester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "simple_gap_strategy_buy_only_(gold_d1).html");
        runStrategy(strategy, tester, path);
        logger.info("Backtesting for GapStrategyBuyOnly successfully finished");
    }

    private static void runMondayGapStrategy() throws Exception{
        logger.info("Start backtesting for MondayGapStrategy");
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.XAUUSD, TimeFrame.Day1);
        BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
        BackTester tester = new BackTester(backTestDataProvider);
        BackTestStrategy strategy = new MondayGapStrategy("Monday gap strategy: buy if monday open price > friday close price",
                tester, getDefaultProperties(quoteHistory));
        tester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "moday_gap_strategy_(gold_d1).html");
        runStrategy(strategy, tester, path);
        logger.info("Backtesting for MondayGapStrategy successfully finished");
    }

    private static void runGapStrategyWithHourBarAccept() throws Exception{
        logger.info("Start backtesting for GapStrategyWithHourBarAccept");
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote   .GAZP, TimeFrame.Hour1);
        BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
        BackTester tester = new BackTester(backTestDataProvider);

        GapStrategyWithHourBarAccept strategy = new GapStrategyWithHourBarAccept(
                "Gap strategy with hour acceptance bar: " +
                        "<br>buy if open price > close price and next hour bar closed with upp" +
                        "<br>sell if open price > close price and next hour bar closed with down.",
                tester, getDefaultProperties(quoteHistory), 9, 16);


        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "gap_strategy_gap_strategy_with_h1_acceptable_(gzp_d1).html");
        runStrategy(strategy, tester, path);
        logger.info("Backtesting for GapStrategyWithHourBarAccept successfully finished");
    }

    private static void runGapStrategyBuyOnlyWithHourBarAccept() throws Exception{
        logger.info("Start backtesting for GapStrategyBuyOnlyWithHourBarAccept");
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.GAZP, TimeFrame.Hour1);
        BackTester tester = new BackTester(new BackTestDataProvider(quoteHistory));

        AbstractGapStrategy strategy = new GapStrategyBuyOnlyWithHourBarAccept(
                "Gap strategy with hour acceptance bar: buy if open price > close price" +
                        " and next hour bar closed with upp.", tester,
                getDefaultProperties(quoteHistory), 9, 16);
        tester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "gap_strategy_gap_strategy_sell_only_with_h1_acceptable_(gzp_d1).html");
        runStrategy(strategy, tester, path);
        logger.info("Backtesting for GapStrategyBuyOnlyWithHourBarAccept successfully finished");
    }

    private static void runGapStrategySellOnlyWithHourBarAccept() throws Exception{
        logger.info("Start backtesting for GapStrategySellOnlyWithHourBarAccept");
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.GAZP, TimeFrame.Hour1);
        BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
        BackTester tester = new BackTester(backTestDataProvider);

        BackTestStrategy strategy = new GapStrategySellOnlyWithHourBarAccept(
                "Gap strategy with hour acceptance bar: sell if open price > close price" +
                        " and next hour bar closed with down.",
                tester,
                getDefaultProperties(quoteHistory), 9, 16);
        tester.setStrategy(strategy);

        Path path = Paths.get(DEFAULT_OUTPUT_REPORT_DIRECTORY + "gap_strategy_sell_only_with_h1_acceptable_(gzp_d1).html");
        runStrategy(strategy, tester, path);
        logger.info("Backtesting for GapStrategySellOnlyWithHourBarAccept successfully finished");
    }

    private static void runStrategy(BackTestStrategy strategy, BackTester tester, Path path) throws HermesException{
        tester.setStrategy(strategy);
        BackTestResult backTestResult = tester.backTest();
        ReportBuilder reportBuilder = new HtmlBackTestReportBuilder(backTestResult);
        reportBuilder.build();

        save(reportBuilder, path);
    }

    private static void save(ReportBuilder reportBuilder, Path path) throws HermesException {
        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            reportBuilder.save(outputStream);
        } catch (IOException ex){
            throw new HermesException(ex);
        }
    }

    private static Map<String, Object> getDefaultProperties(QuoteHistory quoteHistory){
        Map<String, Object> properties = new HashMap<>();
        properties.put("accountOwner", "Almaz Murzabekov");
        properties.put("quoteName", quoteHistory.getQuote().toString());

        return properties;
    }

    private static List<BackTestResult> filter(List<BackTestResult> backTestResults) throws TradingException {
        List<BackTestResult> results = new ArrayList<>(backTestResults.size());

        for (BackTestResult backTestResult : backTestResults) {
            double profitFactor = BackTestTradeUtils.getProfitFactor(backTestResult);
            if(profitFactor >= 1)
                results.add(backTestResult);
        }

        return results;
    }
}
