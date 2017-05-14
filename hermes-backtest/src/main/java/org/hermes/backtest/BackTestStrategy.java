package org.hermes.backtest;

import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.Strategy;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.PositionManager;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.utils.TradeUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by
 * Author:  Almaz
 * Date:    03.10.2015
 */
public abstract class BackTestStrategy implements Strategy {
    protected static final double DEFAULT_ORDER_SIZE = 1.0;

    protected String strategyName;
    protected PositionManager positionManager;
    protected BackTester backTester;
    protected Map<String, Object> properties;

    public BackTestStrategy(String strategyName, PositionManager positionManager, Map<String, Object> properties) {
        this.strategyName = strategyName;
        this.properties = properties;
        this.positionManager = positionManager;
    }

    public BackTestStrategy(String strategyName, BackTester backTester, Map<String, Object> properties) {
        this.strategyName = strategyName;
        this.properties = properties;
        this.positionManager = backTester.getBackTestPositionManager();
        this.backTester = backTester;
    }

    public BackTester getBackTester() {
        return backTester;
    }
    public void setBackTester(BackTester backTester) {
        this.backTester = backTester;
    }

    public abstract void onBackTestStarted() throws BackTestException;
    public abstract void onBackTestFinished() throws BackTestException;

    public void onTick(){
        for (Object key : this.positionManager.getOpenOrderKeys()) {
            Bar currentBar = backTester.getCurrentBar();
            Order order = positionManager.getOrder(key);

            if (TradeUtils.checkStopLoss(currentBar, order)) {
                BigDecimal stopLoss = order.getStopLoss();
                positionManager.closeOrder(order, stopLoss);
            } else if(TradeUtils.checkTakeProfit(currentBar, order)) {
                BigDecimal takeProfit = order.getTakeProfit();
                positionManager.closeOrder(order, takeProfit);
            }
        }
    }

    public PositionManager getPositionManager(){
        return positionManager;
    }

    public void setBackTestPositionManager(PositionManager positionManager){
        this.positionManager = positionManager;
    }

    public String getStrategyName() {
        return this.strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
