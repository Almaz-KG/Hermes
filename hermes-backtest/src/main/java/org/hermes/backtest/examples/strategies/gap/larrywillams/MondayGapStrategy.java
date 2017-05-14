package org.hermes.backtest.examples.strategies.gap.larrywillams;

import org.hermes.backtest.BackTester;
import org.hermes.backtest.examples.strategies.gap.AbstractGapStrategy;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Almaz on 01.10.2015
 *
 * Simple trading strategy from Larry William's book (Long term secrets to short term trading)
 * Buy: If monday open price lowest friday low price
 * Close position: End of monday trading session
 */
public class MondayGapStrategy extends AbstractGapStrategy {

    private Calendar calendar = Calendar.getInstance();

    public MondayGapStrategy(String strategyName, BackTester backTester, Map<String, Object> properties) {
        super(strategyName, backTester, properties);
    }

    @Override
    public void onBarOpen() {
        Bar currentBar = backTester.getCurrentBar();
        Bar previousBar = backTester.getBackTestDataProvider().getBar(1);

        if(previousBar == null)
            return;

        calendar.setTime(currentBar.getDate());
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.setTime(previousBar.getDate());
        int previousDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Покупать если цена в понедельник открылась ниже минимума прошлой пятницы.
        if (currentDayOfWeek == Calendar.MONDAY && previousDayOfWeek == Calendar.FRIDAY) {
            BigDecimal currentOpenPrice = currentBar.getOpen();
            BigDecimal previousLowPrice = previousBar.getLow();

            if (currentOpenPrice.doubleValue() < previousLowPrice.doubleValue())
                this.positionManager.openOrder(
                        currentBar.getDate(),
                        backTester.getBackTestDataProvider().getQuote(),
                        BigDecimal.ONE,
                        currentOpenPrice,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        TradeType.BUY);
        }
    }

    @Override
    public void onBarClose() throws BackTestException {
        //  Позицию закрываем в конце текущей торговой сессии в понедельник.
        for (Object key : this.positionManager.getOpenOrderKeys()) {
            Order order = this.positionManager.getOrder(key);
            Bar currentBar = backTester.getCurrentBar();
            calendar.setTime(currentBar.getDate());
            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if(order != null && !order.isClosed() && Calendar.MONDAY == currentDayOfWeek ){
                BigDecimal currentClosePrice = currentBar.getClose();
                order.setCloseDate(currentBar.getDate());

                this.positionManager.closeOrder(order, currentClosePrice);
            }
        }
    }

    @Override
    public void onBackTestStarted() throws BackTestException {
        // Do nothing
    }

    @Override
    public void onBackTestFinished() throws BackTestException{
        onBarClose();
    }
}
