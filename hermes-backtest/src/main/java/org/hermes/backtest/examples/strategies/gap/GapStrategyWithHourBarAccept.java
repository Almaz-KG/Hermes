package org.hermes.backtest.examples.strategies.gap;

import org.hermes.backtest.BackTestPositionManager;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.PositionManager;
import org.hermes.core.exceptions.TradingException;

import java.util.Map;

/**
 * Created by
 * Author:  Almaz
 * Date:    03.10.2015 22:58
 */
public class GapStrategyWithHourBarAccept extends AbstractGapStrategy{

    private GapStrategyBuyOnlyWithHourBarAccept buyStrategy;
    private GapStrategySellOnlyWithHourBarAccept sellStrategy;
    private BackTester backTester;

    public GapStrategyWithHourBarAccept(String strategyName, BackTester tester, Map<String, Object> properties,
                int openHour, int closeHour) {
        super(strategyName, tester, properties);
        this.sellStrategy = new GapStrategySellOnlyWithHourBarAccept(strategyName, tester,
                properties, openHour, closeHour);
        this.buyStrategy= new GapStrategyBuyOnlyWithHourBarAccept(strategyName, tester,
                properties, openHour, closeHour);

        this.backTester = tester;
    }

    @Override
    public PositionManager getPositionManager() {
        PositionManager result = new BackTestPositionManager(backTester);
        sellStrategy.getPositionManager().getClosedOrders().forEach(e -> result.addOrder(e));
        buyStrategy.getPositionManager().getClosedOrders().forEach(e -> result.addOrder(e));

        return result;
    }

    @Override
    public void onBackTestStarted() throws BackTestException {
        sellStrategy.onBackTestStarted();
        buyStrategy.onBackTestStarted();
    }

    @Override
    public void onBackTestFinished() throws BackTestException {
        sellStrategy.onBackTestFinished();
        buyStrategy.onBackTestFinished();
    }

    @Override
    public void onTick() {
        // Do nothing
    }

    @Override
    public void onBarOpen() throws TradingException {
        sellStrategy.onBarOpen();
        buyStrategy.onBarOpen();
    }
}
