package org.hermes.backtest.examples.strategies;


import org.hermes.backtest.BackTestStrategy;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.exceptions.HermesException;
import org.hermes.core.indicators.simple.BarSizeSimpleMovingAverageIndicator;
import org.hermes.core.indicators.simple.MovingAverageIndicator;
import org.hermes.core.utils.TradeUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Almaz
 * Date: 19.10.2015
 */

/***
 *  Торговая стратегия:
 *
 *  Покупать на открытии завтра, если сегодня рынок демонстрирует
 *  рост на signalBarSize% (от high-low or open-close) от signalBarPeriod
 *  среднего дневного диапазона.
 *
 *  Продавать на открытии завтра, если егодня рынок демонстрирует
 *  снижение на signalBarSize% (от high-low or open-close) от signalBarPeriod
 *  среднего дневного диапазона.
 *
 *  Выход по противоположенному сигналу
 */
public class BarSizeStrategy extends BackTestStrategy {
    private static Logger logger = Logger.getLogger(BarSizeStrategy.class.getCanonicalName());
    private static final String DEFAULT_STRATEGY_NAME = "Bar size strategy";

    private int signalBarPeriod = 3;
    private double signalBarSize = 1.2;
    private MovingAverageIndicator movingAverageIndicator;


    public BarSizeStrategy(BackTester backTester,
                           Map<String, Object> properties) {
        super(DEFAULT_STRATEGY_NAME, backTester, properties);
        movingAverageIndicator = new BarSizeSimpleMovingAverageIndicator(backTester.getBackTestDataProvider(),
                BarSizeSimpleMovingAverageIndicator.BarSizeOption.OPEN_CLOSE, signalBarPeriod);
    }

    @Override
    public void onBackTestStarted() throws BackTestException {
        logger.info("Backtesting started for " + this.toString());
    }

    @Override
    public void onBackTestFinished() throws BackTestException {
        Bar currentBar = backTester.getCurrentBar();

        this.positionManager.closePositions(TradeType.SELL, currentBar.getClose(), currentBar.getDate());
        this.positionManager.closePositions(TradeType.BUY, currentBar.getClose(), currentBar.getDate());

        logger.info("Backtesting finished for " + this.toString());
    }

    @Override
    public void onBarOpen(){
        // Do nothing
    }

    @Override
    public void onBarClose() throws HermesException {
        try {
            double smaBarSize = movingAverageIndicator.calculate();
            double currentBarSize = TradeUtils.getBarSizeOpenClose(backTester.getCurrentBar()).doubleValue();

            if(Math.abs(currentBarSize) >= (smaBarSize * signalBarSize)){
                if(currentBarSize > 0)
                    checkAndOpenBuyPositions();
                if(currentBarSize < 0)
                    checkAndOpenSellPositions();
            }

        } catch (CalculationException e) {
            throw new HermesException(e);
        }
    }

    private void checkAndOpenBuyPositions() {
        Bar currentBar = backTester.getCurrentBar();

        this.positionManager.closePositions(TradeType.SELL, currentBar.getClose(), currentBar.getDate());
        if(!existPosition(TradeType.BUY)){
            openOrder(currentBar, TradeType.BUY);

            logger.info("Buy order: " + currentBar.getDate() + " open price " + currentBar.getClose());
        }
    }

    private void openOrder(Bar currentBar, TradeType tradeType){
        positionManager.openOrder(
                currentBar.getDate(),
                backTester.getBackTestDataProvider().getQuote(),
                BigDecimal.ONE,
                currentBar.getClose(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                tradeType);
    }

    private void checkAndOpenSellPositions() {
        Bar currentBar = backTester.getCurrentBar();

        this.positionManager.closePositions(TradeType.BUY, currentBar.getClose(), currentBar.getDate());

        if(!existPosition(TradeType.SELL)){
            openOrder(currentBar, TradeType.SELL);

            logger.info("Sell order: " + currentBar.getDate() + " open price " + currentBar.getClose());
        }
    }

    private boolean existPosition(TradeType tradeType) {
        for (Object key : positionManager.getOpenOrderKeys()) {
            Order order = positionManager.getOrder(key);

            if(order.getTradeType() == tradeType)
                return true;
        }
        return false;
    }


    public int getSignalBarPeriod() {
        return signalBarPeriod;
    }

    public void setSignalBarPeriod(int signalBarPeriod) {
        this.signalBarPeriod = signalBarPeriod;
    }

    public double getSignalBarSize() {
        return signalBarSize;
    }

    public void setSignalBarSize(double signalBarSize) {
        this.signalBarSize = signalBarSize;
    }
}
