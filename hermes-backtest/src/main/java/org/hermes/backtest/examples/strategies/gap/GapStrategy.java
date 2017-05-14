package org.hermes.backtest.examples.strategies.gap;

import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.TradingException;
import org.hermes.core.utils.TradeUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Almaz
 * Date: 04.10.2015 23:02
 */
public class GapStrategy extends AbstractGapStrategy {

    protected double SIGNAL_BAR_SIZE_FACTOR = 0.1;
    protected double TAKE_PROFIT_FACTOR = 2.0;
    protected double STOP_LOSS_FACTOR = 1.0;
    protected double GAP_SIZE_FACTOR = 1.0;
    protected int openHour = 16;
    protected int closeHour = 22;
    private final Calendar calendar;

    public GapStrategy(String strategyName, BackTester backTester, Map<String, Object> properties) {
        super(strategyName, backTester, properties);
        this.calendar = Calendar.getInstance();
    }

    public GapStrategy(String strategyName, BackTester backTester, Map<String, Object> properties,
                       int openHour, int closeHour) {
        super(strategyName, backTester, properties);
        this.calendar = Calendar.getInstance();
        this.openHour = openHour;
        this.closeHour = closeHour;
    }

    @Override
    public void onBackTestStarted() throws BackTestException {
        // Do nothing
    }

    @Override
    public void onBackTestFinished() throws BackTestException {
        closeOrders(backTester.getCurrentBar());
    }

    @Override
    public void onBarOpen() throws TradingException {
        // Do nothing
    }

    @Override
    public void onBarClose() throws BackTestException {
        Bar previousBar = backTester.getBackTestDataProvider().getBar(1);
        Bar currentBar = backTester.getCurrentBar();

        calendar.setTime(previousBar.getDate());
        //  If previous bar was a open market bar
        if(calendar.get(Calendar.HOUR_OF_DAY) == openHour){
            double previousBarSize = TradeUtils.getBarSizeOpenCloseAbs(previousBar).doubleValue();
            double gap = TradeUtils.getGapSize(currentBar, previousBar).doubleValue();

            if(previousBarSize  == 0)
                previousBarSize = TradeUtils.getBarSizeHighLowLAbs(previousBar).doubleValue();

            if(Math.abs(gap) < (previousBarSize * GAP_SIZE_FACTOR))
                return;

            // If price gap present between current and previous bars
            // Gap up
            if(gap > 0 && gap >= previousBarSize * SIGNAL_BAR_SIZE_FACTOR){
                positionManager.openOrder(
                        currentBar.getDate(),
                        backTester.getBackTestDataProvider().getQuote(),
                        BigDecimal.valueOf(DEFAULT_ORDER_SIZE), // position size
                        currentBar.getClose(), // Open price
                        BigDecimal.valueOf(currentBar.getClose().doubleValue() - (previousBarSize * STOP_LOSS_FACTOR)), // Stop loss
                        BigDecimal.valueOf(currentBar.getClose().doubleValue() + (previousBarSize * TAKE_PROFIT_FACTOR)), // Take profit,
                        TradeType.BUY);
            }
            // If gap down
            else if(gap < 0 && Math.abs(gap) >= previousBarSize * SIGNAL_BAR_SIZE_FACTOR){
                positionManager.openOrder(
                        currentBar.getDate(),
                        backTester.getBackTestDataProvider().getQuote(),
                        BigDecimal.valueOf(DEFAULT_ORDER_SIZE),
                        currentBar.getClose(),
                        BigDecimal.valueOf(currentBar.getClose().doubleValue() + (previousBarSize * STOP_LOSS_FACTOR)),
                        BigDecimal.valueOf(currentBar.getClose().doubleValue() - (previousBarSize * TAKE_PROFIT_FACTOR)),
                        TradeType.SELL);
            }
        }

        calendar.setTime(currentBar.getDate());

        if(calendar.get(Calendar.HOUR_OF_DAY) == closeHour){
            closeOrders(currentBar);
        }
    }

    private void closeOrders(Bar currentBar) throws BackTestException{
        for (Object key : this.positionManager.getOpenOrderKeys()) {
            Order order = this.positionManager.getOrder(key);
            if(order != null && false == order.isClosed()){
                BigDecimal currentClosePrice = currentBar.getClose();

                order.setCloseDate(currentBar.getDate());
                this.positionManager.closeOrder(order, currentClosePrice);
            }
        }
    }

    public double getSignalBarSizeFactor() {
        return SIGNAL_BAR_SIZE_FACTOR;
    }

    public double getTakeProfitFactor() {
        return TAKE_PROFIT_FACTOR;
    }

    public double getStopLossFactor() {
        return STOP_LOSS_FACTOR;
    }

    public double getGapSizeFactor() {
        return GAP_SIZE_FACTOR;
    }

    public void setSignalBarSizeFactor(double SIGNAL_BAR_SIZE_FACTOR) {
        this.SIGNAL_BAR_SIZE_FACTOR = SIGNAL_BAR_SIZE_FACTOR;
    }

    public void setTakeProfitFactor(double TAKE_PROFIT_FACTOR) {
        this.TAKE_PROFIT_FACTOR = TAKE_PROFIT_FACTOR;
    }

    public void setStopLossFactor(double STOP_LOSS_FACTOR) {
        this.STOP_LOSS_FACTOR = STOP_LOSS_FACTOR;
    }

    public void setGapSizeFactor(double GAP_SIZE_FACTOR) {
        this.GAP_SIZE_FACTOR = GAP_SIZE_FACTOR;
    }
}
