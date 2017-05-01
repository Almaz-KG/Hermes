package org.hermes.backtest.examples;

import org.hermes.backtest.BackTestDataProvider;
import org.hermes.backtest.BackTestResult;
import org.hermes.backtest.BackTestStrategy;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.examples.researches.*;
import org.hermes.backtest.examples.strategies.gap.GapStrategy;
import org.hermes.backtest.exceptions.ReportBuilderException;
import org.hermes.backtest.reports.HtmlResearchReportBuilder;
import org.hermes.backtest.reports.ReportBuilder;
import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.exceptions.HermesException;
import org.hermes.core.exceptions.ResearchException;
import org.hermes.core.exceptions.ResourceLoadException;
import org.hermes.core.research.Research;
import org.hermes.core.research.ResearchResult;
import org.hermes.data.provider.integration.IOUtils;
import org.hermes.data.provider.integration.ResourceManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Almaz
 * Date: 05.10.2015
 * <p/>
 */
public class ResearchRunner {
    public static void main(String[] args) throws Exception{
        runBarUpDownClosePercentageResearch();
//        runBarSizeStrategyResearch();
//        runSMAResearch();
//        runDayRangeResearch();
//        runProfitFactorResearch();
    }

    private static void runBarUpDownClosePercentageResearch() throws HermesException{
        TimeFrame timeFrame = TimeFrame.Day1;
        Quote quote = Quote.US_Stocks;


        List<QuoteHistory> quoteHistories = ResourceManager.getQuoteHistories(quote, timeFrame);
        List<ResearchResult> results = new ArrayList<>();

        for (QuoteHistory quoteHistory : quoteHistories) {
            BackTestDataProvider dataProvider = new BackTestDataProvider(quoteHistory);
            Research research = new BarUpDownPercentageResearch(dataProvider);
            results.add(research.research());
        }

        Path path = Paths.get(BackTestRunner.DEFAULT_OUTPUT_REPORT_DIRECTORY + "BarUpDownClosePercentageResearch_" + quote.toString()+".html");
        String template = "HtmlBarUpDownClosePercentageResearch.vm";
        ReportBuilder reportBuilder = new HtmlResearchReportBuilder(results, template);
        reportBuilder.build();

        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            reportBuilder.save(outputStream);
        } catch (IOException e) {
            throw new HermesException(e);
        }
    }

    private static void runBarSizeStrategyResearch() throws HermesException{
        Quote quote = Quote.XAUUSD;
        TimeFrame timeFrame = TimeFrame.Day1;

        int startSignalBarPeriod = 1;
        int finishSignalBarPeriod = 10;
        double startSignalBarSize = 0.5;
        double finishSignalBarSize = 2.0;

        BarSizeResearch research = new BarSizeResearch(quote, timeFrame, startSignalBarPeriod, finishSignalBarPeriod,
                startSignalBarSize, finishSignalBarSize);
        ResearchResult result = research.research();

        List<BackTestResult> results = new ArrayList<>();
        for (String s : result.getProperties().keySet()) {
            BackTestResult researchResult = (BackTestResult)result.getProperties().get(s);
            results.add(researchResult);
        }

        Path path = Paths.get(BackTestRunner.DEFAULT_OUTPUT_REPORT_DIRECTORY + "barSizeStrategyResearch_"+quote.toString()+".html");

        String template = "HtmlBarSizeStrategyResearchTemplate.vm";
        ReportBuilder reportBuilder = new HtmlResearchReportBuilder(castBackTestResultToResearchResult(results), template);
        reportBuilder.build();

        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            reportBuilder.save(outputStream);
        } catch (IOException e) {
            throw new HermesException(e);
        }
    }

    private static void runSMAResearch() throws HermesException{
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.XAUUSD, TimeFrame.Day1);

        int startFastSMA = 1;
        int endFastSMA = 120;
        int startSlowSMA = 2;
        int endSlowSMA = 230;

        SimpleMovingAverageResearch research = new SimpleMovingAverageResearch(quoteHistory,
                startSlowSMA, endSlowSMA, startFastSMA, endFastSMA);
        ResearchResult result = research.research();

        List<BackTestResult> results = new ArrayList<>();
        for (String s : result.getProperties().keySet()) {
            BackTestResult researchResult = (BackTestResult)result.getProperties().get(s);
            results.add(researchResult);
        }

        Path path = Paths.get(BackTestRunner.DEFAULT_OUTPUT_REPORT_DIRECTORY + "sma_gold_research.html");

        String template = "HtmlFastAndSlowSMAResearchReportTemplate.vm";
        ReportBuilder reportBuilder = new HtmlResearchReportBuilder(castBackTestResultToResearchResult(results), template);
        reportBuilder.build();

        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            reportBuilder.save(outputStream);
        } catch (IOException e) {
            throw new HermesException(e);
        }
    }

    private static void runDayRangeResearch() throws HermesException {
        List<QuoteHistory> quoteHistories = ResourceManager.getQuoteHistories(Quote.RU_Stocks, TimeFrame.Day1);
        List<ResearchResult> results = new ArrayList<>();

        for (QuoteHistory quoteHistory : quoteHistories) {
            DayRangeResearch research = new DayRangeResearch(quoteHistory);
            ResearchResult researchResult = research.research();
            results.add(researchResult);
        }

        Path path = Paths.get(BackTestRunner.DEFAULT_OUTPUT_REPORT_DIRECTORY + "barDayRangeResearch.html");

        String template = "HtmlDayRangeResearchReportTemplate.vm";
        ReportBuilder reportBuilder = new HtmlResearchReportBuilder(results, template);
        reportBuilder.build();

        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            reportBuilder.save(outputStream);
        } catch (IOException e){
            throw new HermesException(e);
        }
    }

    private static void runProfitFactorResearch() throws HermesException{
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.GAZP, TimeFrame.Hour1);
        BackTestDataProvider backTestDataProvider = new BackTestDataProvider(quoteHistory);
        BackTester backTester = new BackTester(backTestDataProvider);

        Map<String, Object> properties = getDefaultProfitFactorResearchProperties();
        properties.put("quote", quoteHistory.getQuote());
        BackTestStrategy strategy = new GapStrategy("Simple gap strategy", backTester, properties);
        backTester.setStrategy(strategy);

        GapStrategyProfitFactorResearch profitFactor = new GapStrategyProfitFactorResearch(strategy, backTestDataProvider, properties);

        ResearchResult research = profitFactor.research();
        List<BackTestResult> researchResult = (List<BackTestResult>) research.getProperties().get("backtestResults");

        Path path = Paths.get(BackTestRunner.DEFAULT_OUTPUT_REPORT_DIRECTORY + "simple_gap_strategy_profit_factor_research.html");
        try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
            IOUtils.checkAndCreateFolder(path);
            String template = "HtmlProfitFactorResearchReportTemplate.vm";
            HtmlResearchReportBuilder reportBuilder = new HtmlResearchReportBuilder(
                    castBackTestResultToResearchResult(researchResult), template);
            reportBuilder.build();
            reportBuilder.save(outputStream);

        } catch (IOException e) {
            throw new HermesException(e);
        }

    }

    // TODO: Really ?
    @Deprecated
    private static List<ResearchResult> castBackTestResultToResearchResult(List<BackTestResult> research) {
        List<ResearchResult> result = new ArrayList<>(research.size());
        research.forEach(r -> research.add(r));

        return result;
    }

    private static Map<String, Object> getDefaultResearchProperties(QuoteHistory quoteHistory) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("accountOwner", "Almaz Murzabekov");
        properties.put("quoteName", quoteHistory.getQuote().toString());

        return properties;
    }

    private static Map<String, Object> getDefaultProfitFactorResearchProperties() {
        Map<String, Object> result = new HashMap<>();
        result.put(GapStrategyProfitFactorResearch.OPEN_HOUR, 16);
        result.put(GapStrategyProfitFactorResearch.CLOSE_HOUR, 22);
        result.put(GapStrategyProfitFactorResearch.SIGNAL_BAR_SIZE_FACTOR_END, 1);
        result.put(GapStrategyProfitFactorResearch.SIGNAL_BAR_SIZE_FACTOR_STEP, 0.1);
        result.put(GapStrategyProfitFactorResearch.SIGNAL_BAR_SIZE_FACTOR_START, 0);
        result.put(GapStrategyProfitFactorResearch.TAKE_PROFIT_FACTOR_START, 1);
        result.put(GapStrategyProfitFactorResearch.TAKE_PROFIT_FACTOR_END, 10);
        result.put(GapStrategyProfitFactorResearch.TAKE_PROFIT_FACTOR_STEP, 0.25);
        return result;
    }
}
