package org.hermes.backtest.examples.strategies.gap;

import org.hermes.backtest.BackTestStrategy;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.price.Bar;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by
 * Author:  Almaz
 * Date:    03.10.2015
 */
public abstract class AbstractGapStrategy extends BackTestStrategy {

    public AbstractGapStrategy(String strategyName, BackTester backTester, Map<String, Object> properties) {
        super(strategyName, backTester, properties);
    }

    @Override
    public void onBarClose() throws BackTestException {
        // Позицию закрываем в конце текущей торговой сессии.
        for (Object key : this.positionManager.getOpenOrderKeys()) {
            Order order = this.positionManager.getOrder(key);
            if(order != null && false == order.isClosed()){
                Bar currentBar = backTester.getCurrentBar();
                BigDecimal currentClosePrice = currentBar.getClose();

                order.setCloseDate(currentBar.getDate());
                this.positionManager.closeOrder(order, currentClosePrice);
            }
        }
    }
}
