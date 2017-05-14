package org.hermes.backtest;

import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.PositionManager;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.HermesException;
import org.hermes.core.exceptions.TradingException;
import org.hermes.core.utils.TradeUtils;

import java.math.BigDecimal;
import java.util.Map;

public class BackTester {

    private BackTestStrategy strategy;
    private DataProvider backTestDataProvider;
    private PositionManager positionManager;
    private BigDecimal currentPrice;

    public BackTester(DataProvider backTestDataProvider) throws BackTestException {
        this.positionManager = new BackTestPositionManager(this);
        this.backTestDataProvider = backTestDataProvider;

        if (backTestDataProvider.size() == 0)
            throw new BackTestException("Quote history is empty");
    }

    public BackTestResult backTest() throws BackTestException {
        try {
            strategy.onBackTestStarted();

            while (backTestDataProvider.hasMoreData()) {

                backTestDataProvider.nextBar();
                Bar currentBar = backTestDataProvider.getCurrentBar();

                currentPrice = currentBar.getOpen();
                    strategy.onBarOpen();


                // check Stop Loss & Take Profit
                checkSLandTP();

                strategy.onTick();

                currentPrice = currentBar.getClose();
                strategy.onBarClose();
            }

            strategy.onBackTestFinished();
            return getBackTestResult();
        } catch (HermesException e) {
            throw new BackTestException(e);
        }
    }

    private void checkSLandTP() throws BackTestException {
        PositionManager positionManager = strategy.getPositionManager();
        for (Object key : positionManager.getOpenOrderKeys()) {
            Bar currentBar = backTestDataProvider.getCurrentBar();
            Order order = positionManager.getOrder(key);

            if(order.isClosed())
                continue;

            if (TradeUtils.checkStopLoss(currentBar, order)) {
                BigDecimal stopLoss = order.getStopLoss();
                order.setCloseDate(currentBar.getDate());
                positionManager.closeOrder(order, stopLoss);
            } else if(TradeUtils.checkTakeProfit(currentBar, order)) {
                BigDecimal takeProfit = order.getTakeProfit();
                order.setCloseDate(currentBar.getDate());
                positionManager.closeOrder(order, takeProfit);
            }
        }
    }

    private BackTestResult getBackTestResult() throws BackTestException{
       try {
            BackTestResult result = new BackTestResult(this.strategy);

            result.setOrders(strategy.getPositionManager().getClosedOrders());
            result.setQuoteHistory(backTestDataProvider.getQuoteHistory());
            Map<String, Object> properties = result.getProperties();
            properties.put("strategyName", this.strategy.getStrategyName());
            properties.put("orders", this.strategy.getPositionManager().getClosedOrders());
            properties.put("grossProfit", BackTestTradeUtils.getGrossProfit(result));
            properties.put("grossLoss", BackTestTradeUtils.getGrossLoss(result));
            properties.put("totalNet", BackTestTradeUtils.getTotalNet(result));
            properties.put("profitFactor", BackTestTradeUtils.getProfitFactor(result));
            properties.put("expectedPayoff", BackTestTradeUtils.getExpectedPayoff(result));

            properties.put("totalTrades", BackTestTradeUtils.getCountPositions(result));
            properties.put("totalShortTradeCount", BackTestTradeUtils.getShortPositions(result));
            properties.put("totalLongTradeCount", BackTestTradeUtils.getLongPositions(result));
            properties.put("totalProfitTradeCount", BackTestTradeUtils.getCountProfitTrades(result));
            properties.put("totalLossTradeCount", BackTestTradeUtils.getCountLossTrades(result));

            properties.put("totalLargestProfit", BackTestTradeUtils.getLargestProfit(result));
            properties.put("totalLargestLoss", BackTestTradeUtils.getLargestLoss(result));
            properties.put("totalAverageProfit", BackTestTradeUtils.getAverageProfit(result));
            properties.put("totalAverageLoss", BackTestTradeUtils.getAverageLoss(result));
            properties.put("totalProfitPercentage", BackTestTradeUtils.getTotalProfitPercentage(result));

            properties.put("consecutiveProfitCount", BackTestTradeUtils.getConsecutiveProfitCount(result));
            properties.put("consecutiveLossCount", BackTestTradeUtils.getConsecutiveLossCount(result));
            properties.put("defaultOrderSize", strategy.DEFAULT_ORDER_SIZE);
            properties.put("drawdownBalanceClose", BackTestTradeUtils.getDrawdownBalanceClose(result));
            properties.put("startDate", BackTestTradeUtils.getFirstBar(backTestDataProvider).getDate());
            properties.put("endDate", BackTestTradeUtils.getLastBar(backTestDataProvider).getDate());

            return result;
       } catch (TradingException e) {
           throw new BackTestException(e);
       }
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }
    public Bar getCurrentBar(){
        return this.backTestDataProvider.getCurrentBar();
    }

    public DataProvider getBackTestDataProvider(){
        return backTestDataProvider;
    }

    public BackTestStrategy getStrategy() {
        return strategy;
    }
    public void setStrategy(BackTestStrategy strategy) {
        this.strategy = strategy;
    }

    public void setBackTestDataProvider(DataProvider backTestDataProvider) {
        this.backTestDataProvider = backTestDataProvider;
    }

    public PositionManager getBackTestPositionManager(){
        return this.positionManager;
    }
}