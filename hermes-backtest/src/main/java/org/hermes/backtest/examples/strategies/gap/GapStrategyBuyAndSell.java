package org.hermes.backtest.examples.strategies.gap;


import org.hermes.backtest.BackTestPositionManager;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.PositionManager;
import org.hermes.core.exceptions.TradingException;

import java.util.Map;

/**
 * Created by Almaz on 01.10.2015.
 */
public class GapStrategyBuyAndSell extends AbstractGapStrategy{

    private GapStrategySellOnly sellStrategy;
    private GapStrategyBuyOnly buyStrategy;
    private BackTester backTester;

    public GapStrategyBuyAndSell(String strategyName, BackTester backTester, Map<String, Object> properties) {
        super(strategyName, backTester, properties);
        this.backTester = backTester;
        sellStrategy = new GapStrategySellOnly(strategyName, backTester, properties);
        buyStrategy = new GapStrategyBuyOnly(strategyName, backTester, properties);

    }

    @Override
    public void onBarOpen() throws TradingException {
        sellStrategy.onBarOpen();
        buyStrategy.onBarOpen();
    }

    @Override
    public void onBarClose() throws BackTestException {
        sellStrategy.onBarClose();
        buyStrategy.onBarClose();
    }

    @Override
    public void onBackTestStarted() throws BackTestException {
        sellStrategy.onBackTestStarted();
        buyStrategy.onBackTestStarted();
    }

    @Override
    public void onBackTestFinished() throws BackTestException{
        sellStrategy.onBackTestFinished();
        buyStrategy.onBackTestFinished();
    }

    @Override
    public void onTick() {
        // Do nothing
    }

    @Override
    public PositionManager getPositionManager() {
        PositionManager sellPositionManager = sellStrategy.getPositionManager();
        PositionManager buyPositionManager = buyStrategy.getPositionManager();

        PositionManager result = new BackTestPositionManager(backTester);
        for (Order order : sellPositionManager.getClosedOrders()) {
            result.addOrder(order);
        }

        for (Order order : buyPositionManager.getClosedOrders()) {
            result.addOrder(order);
        }
        return result;
    }
}