package org.hermes.backtest.examples.strategies.ma;


import org.hermes.backtest.BackTestStrategy;
import org.hermes.backtest.BackTester;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.QuotePriceType;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.exceptions.HermesException;
import org.hermes.core.exceptions.TradingException;
import org.hermes.core.indicators.helpers.PriceIndicator;
import org.hermes.core.indicators.simple.MovingAverageIndicator;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Almaz
 * Date: 13.10.2015
 *
 * Simple Moving Average (SMA) strategy
 *
 * Buy signal:
 *      Fast SMA crossover UP slow SMA
 * Sell signal
 *      Fast SMA crossover DOWN slow SMA
 *
 * Close position:
 *  1.  If opposite signal generated
 *  2.  Take profit
 */
public class SimpleMovingAverageStrategy extends BackTestStrategy {

    private MovingAverageIndicator fastSma;
    private MovingAverageIndicator slowSma;
    private double lastFastSmaValue;
    private double lastSlowSmaValue;
    private double orderSize = 1;

    public SimpleMovingAverageStrategy(String strategyName,
                                       BackTester backTester, Map<String, Object> properties) {
        super(strategyName, backTester, properties);


        fastSma = new MovingAverageIndicator(
                new PriceIndicator(backTester.getBackTestDataProvider(), QuotePriceType.CLOSE),
                MovingAverageIndicator.TYPE.SMA, (int)properties.get("fastSMA"));
        slowSma = new MovingAverageIndicator(
                new PriceIndicator(backTester.getBackTestDataProvider(), QuotePriceType.CLOSE),
                MovingAverageIndicator.TYPE.SMA, (int)properties.get("slowSMA"));
    }

    @Override
    public void onBarOpen() throws HermesException {
       /* Buy signal:
        *      Fast SMA crossover UP slow SMA
        * Sell signal
        *      Fast SMA crossover DOWN slow SMA
        */
        try {
            double fastSmaValue = fastSma.calculate();
            double slowSmaValue = slowSma.calculate();

            checkBuySignal(fastSmaValue, slowSmaValue);
            checkSellSignal(fastSmaValue, slowSmaValue);

            lastFastSmaValue = fastSmaValue;
            lastSlowSmaValue = slowSmaValue;
        } catch (CalculationException e) {
            throw new HermesException(e);
        }
    }

    private void checkSellSignal(double fastSmaValue, double slowSmaValue) throws TradingException {
        /**
         *  Fast SMA crossover DOWN slow SMA
         *
         *  Текущий быстрый SMA меньше текущего медленнего SMA
         *  и предыдущий быстрый SMA больше предыдущего медленнего SMA
         *
         *  FAST SMA пересекает SLOW SMA вверху-вниз
         */
        if (fastSmaValue < slowSmaValue && lastFastSmaValue > lastSlowSmaValue){
            Bar currentBar = this.backTester.getCurrentBar();
            // If exist buy order - close it
            for (Object key : this.positionManager.getOpenOrderKeys()) {
                Order order = this.positionManager.getOrder(key);
                if(order.getTradeType() == TradeType.BUY) {
                    order.setCloseDate(currentBar.getDate());
                    positionManager.closeOrder(order, currentBar.getOpen());
                }
            }

            boolean existSellOrder = false;
            for (Object key : this.positionManager.getOpenOrderKeys()) {
                Order order = this.positionManager.getOrder(key);
                if(order.getTradeType() == TradeType.SELL) {
                    existSellOrder = true;
                    break;
                }
            }

            // If no active sell order
            if(!existSellOrder)
                positionManager.openOrder(
                        currentBar.getDate(),
                        this.backTester.getBackTestDataProvider().getQuote(),
                        BigDecimal.valueOf(orderSize),
                        currentBar.getOpen(),
                        BigDecimal.valueOf(currentBar.getOpen().doubleValue() + 2.0), // SL
                        BigDecimal.valueOf(currentBar.getOpen().doubleValue() - 20.0), // TP
                        TradeType.SELL);
        }
    }

    private void checkBuySignal(double fastSmaValue, double slowSmaValue) throws TradingException {
        /**
         *  Fast SMA crossover UP slow SMA
         *
         *  Текущий быстрый SMA больше текущего медленнего SMA
         *  и предыдущий быстрый SMA меньше предыдущего медленнего SMA
         *
         *  FAST SMA пересекает SLOW SMA снизу-вверх
         *
         *  TODO: попробовать поиграться с погрешностями
         */
        if (fastSmaValue > slowSmaValue && lastFastSmaValue < lastSlowSmaValue){
            Bar currentBar = this.backTester.getCurrentBar();
            // If exist sell order - close it
            for (Object key : this.positionManager.getOpenOrderKeys()) {
                Order order = this.positionManager.getOrder(key);
                if(order.getTradeType() == TradeType.SELL) {
                    order.setCloseDate(currentBar.getDate());
                    positionManager.closeOrder(order, currentBar.getOpen());
                }
            }

            boolean existBuyOrder = false;
            for (Object key : this.positionManager.getOpenOrderKeys()) {
                Order order = this.positionManager.getOrder(key);
                if(order.getTradeType() == TradeType.BUY) {
                    existBuyOrder = true;
                    break;
                }
            }

            // If no active buy order
            if(!existBuyOrder)
                positionManager.openOrder(
                        currentBar.getDate(),
                        this.backTester.getBackTestDataProvider().getQuote(),
                        BigDecimal.valueOf(orderSize),
                        currentBar.getOpen(),
                        BigDecimal.valueOf(currentBar.getOpen().doubleValue() - 2.0),  // SL
                        BigDecimal.valueOf(currentBar.getOpen().doubleValue() + 20.0), // TP
                        TradeType.BUY);
        }
    }

    @Override
    public void onBarClose(){
        // Do nothing
    }

    @Override
    public void onBackTestStarted(){
        // Do nothing
    }

    @Override
    public void onBackTestFinished(){
        Bar currentBar = this.backTester.getCurrentBar();

        for (Object key : this.positionManager.getOpenOrderKeys()) {
            Order order = this.positionManager.getOrder(key);
            order.setCloseDate(currentBar.getDate());
            this.positionManager.closeOrder(order, currentBar.getClose());
        }

        assert this.positionManager.getOpenOrderKeys().size() == 0;
    }

    @Override
    public void onTick() {
        // Do nothing
    }
}
