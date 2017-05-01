package org.hermes.backtest;

import com.google.common.base.Preconditions;
import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.TradingException;

import java.math.BigDecimal;

/**
 * Created by Almaz on 14.05.2017.
 */
public class BackTestTradeUtils {

    private static boolean isInvalidBacktestResult(BackTestResult result){
        Preconditions.checkNotNull(result);

        return result.getOrders().isEmpty();
    }
//
//    public static double[] getBackTestBalanceResult(BackTestResult backTestResult) throws TradingException {
//        if(isInvalidBacktestResult(backTestResult))
//            throw new TradingException("Backtest result with empty orders provided");
//
//        double[] result = new double[backTestResult.getOrders().size()];
//
//        List<Order> orders = backTestResult.getOrders();
//        result[0] = (backTestResult.getInitialBalance().add(orders.get(0).getResult())).doubleValue();
//        for (int i = 1; i < result.length; i++) {
//            result[i] = result[i - 1] + orders.get(i).getResult().doubleValue();
//        }
//
//        return result;
//    }

    public static BigDecimal getLargestLoss(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        BigDecimal result = BigDecimal.valueOf(Double.MAX_VALUE);

        for (Order order : backTestResult.getOrders()) {
            BigDecimal orderResult = order.getResult();
            if(orderResult.doubleValue() > 0)
                continue;

            if(result.doubleValue() > orderResult.doubleValue())
                result = orderResult;
        }

        if(result.equals(BigDecimal.valueOf(Double.MAX_VALUE)))
            return BigDecimal.ZERO;

        return result;
    }

    public static BigDecimal getLargestProfit(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        BigDecimal result = BigDecimal.valueOf(Double.MIN_VALUE);

        for (Order order : backTestResult.getOrders()) {
            BigDecimal orderResult = order.getResult();
            if(orderResult.doubleValue() < 0)
                continue;

            if(result.doubleValue() < orderResult.doubleValue())
                result = orderResult;
        }

        if(result.equals(BigDecimal.valueOf(Double.MIN_VALUE)))
            return BigDecimal.ZERO;

        return result;
    }

    public static BigDecimal getAverageProfit(BackTestResult backTestResult) throws TradingException {
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        BigDecimal profit = getGrossProfit(backTestResult);
        int trades = backTestResult.getOrders().size();
        return BigDecimal.valueOf(profit.doubleValue() / trades);
    }

    public static BigDecimal getAverageLoss(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        BigDecimal grossLoss = getGrossLoss(backTestResult);
        int trades = backTestResult.getOrders().size();

        return BigDecimal.valueOf(-1 * grossLoss.doubleValue() / trades);
    }

    public static BigDecimal getGrossLoss(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        BigDecimal result = BigDecimal.ZERO;

        for (Order order : backTestResult.getOrders())
            if(order.getResult().doubleValue() < 0)
                result = result.subtract(order.getResult());

        return result;
    }

    public static BigDecimal getGrossProfit(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        BigDecimal result = BigDecimal.ZERO;

        for (Order order : backTestResult.getOrders())
            if(order.getResult().doubleValue() > 0)
                result = result.add(order.getResult());

        return result;
    }

    public static BigDecimal getExpectedPayoff(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        int trades = backTestResult.getOrders().size();

        return BigDecimal.valueOf(getTotalNet(backTestResult).doubleValue() / trades);
    }

    public static BigDecimal getTotalNet(BackTestResult backTestResult) throws TradingException{
        return getGrossProfit(backTestResult).subtract(getGrossLoss(backTestResult));
    }

    public static Bar getLastBar(DataProvider backTestDataProvider){
        int size = backTestDataProvider.getQuoteHistory().getHistory().size();

        if(size != 0)
            return backTestDataProvider.getQuoteHistory().getHistory().get(size - 1);
        return backTestDataProvider.getQuoteHistory().getHistory().get(size);
    }

    public static Bar getFirstBar(DataProvider backTestDataProvider){
        return backTestDataProvider.getQuoteHistory().getHistory().get(0);
    }

    public static int getLongPositions(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        int result = 0;

        for (Order order : backTestResult.getOrders()) {
            if(order.getTradeType() == TradeType.BUY)
                result++;
        }

        return result;
    }

    public static int getShortPositions(BackTestResult backTestResult) throws TradingException {
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        int result = 0;

        for (Order order : backTestResult.getOrders()) {
            if(order.getTradeType() == TradeType.SELL)
                result++;
        }

        return result;
    }

    public static int getCountPositions(BackTestResult backTestResult) throws TradingException{
        return getLongPositions(backTestResult) + getShortPositions(backTestResult);
    }

    public static int getCountLossTrades(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        int result = 0;

        for (Order order : backTestResult.getOrders())
            if(order.getResult().doubleValue() < 0)
                result++;

        return result;
    }

    public static int getCountProfitTrades(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        int result = 0;

        for (Order order : backTestResult.getOrders())
            if(order.getResult().doubleValue() > 0)
                result++;

        return result;
    }

    public static int getConsecutiveProfitCount(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        int result = 0;
        int index = 0;
        for (Order order : backTestResult.getOrders()) {
            if(order.getResult().doubleValue() > 0) {
                index++;
                continue;
            }
            if(result < index)
                result = index;
            index = 0;
        }

        return result;
    }

    public static int getConsecutiveLossCount(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        int result = 0;
        int index = 0;
        for (Order order : backTestResult.getOrders()) {
            if(order.getResult().doubleValue() < 0) {
                index++;
                continue;
            }
            if(result < index)
                result = index;

            // See continue command
            index = 0;
        }

        return result;
    }

    public static double getProfitFactor(BackTestResult backTestResult) throws TradingException{
        BigDecimal grossLoss = getGrossLoss(backTestResult);

        if(grossLoss.doubleValue() == 0)
            return 100;
        return getGrossProfit(backTestResult).doubleValue() / grossLoss.doubleValue();
    }

    public static double getDrawdownBalanceClose(BackTestResult backTestResult) throws TradingException{
        if(isInvalidBacktestResult(backTestResult))
            throw new TradingException("Backtest result with empty orders provided");

        double result = 0;
        double current = 0;

        for (Order order : backTestResult.getOrders()) {
            BigDecimal orderResult = order.getResult();
            if(orderResult.doubleValue() < 0)
                current += orderResult.doubleValue();
            else if(result > current) {
                result = current;
                current = 0;
            }
        }

        return result;
    }

    public static double getTotalProfitPercentage(BackTestResult backTestResult) throws TradingException{
        int countProfitTrades = getCountProfitTrades(backTestResult);
        int countPositions = getCountPositions(backTestResult);
        return countPositions == 0 ? 0 : 100.0 * countProfitTrades / countPositions;
    }
}
