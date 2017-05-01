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
 * buy:
 *  Если цена открытия больше чем цена закрытия предыдущей торговой сессии
 *  (гэп вверх) - ожидаем  закрытия часового бара открытия: если данный бар
 *  закрылся в плюсе открываем длинную позицию. Если же цена открылась с
 *  гэпом вверх, но бар закрылся вниз - пропускаем сигнал.
 *  Позицию закрываем в конце текущей торговой сессии.
 */
public class GapStrategyBuyOnlyWithHourBarAccept extends AbstractGapStrategy{
    protected int OPEN_HOUR;
    protected int CLOSE_HOUR;
    protected boolean isOpenedWithGap;
    protected Calendar calendar;

    public GapStrategyBuyOnlyWithHourBarAccept(String strategyName,
               BackTester backTester,
               Map<String, Object> properties,
               int openHour, int closeHour) {
        super(strategyName, backTester, properties);
        this.OPEN_HOUR = openHour;
        this.CLOSE_HOUR = closeHour;
        this.calendar = Calendar.getInstance();
    }

    @Override
    public void onBarClose() throws BackTestException {
        /**
         *  Позицию закрываем в конце текущей торговой сессии,
         *  т.е. current_hour = closeHour
         */
        Bar currentBar = backTester.getCurrentBar();
        calendar.setTime(currentBar.getDate());
        if(CLOSE_HOUR == calendar.get(Calendar.HOUR_OF_DAY)){
            for (Object key : this.positionManager.getOpenOrderKeys()) {
                Order order = this.positionManager.getOrder(key);
                if(order != null && false == order.isClosed()){

                    BigDecimal currentClosePrice = currentBar.getClose();

                    order.setCloseDate(currentBar.getDate());
                    this.positionManager.closeOrder(order, currentClosePrice);
                }
            }
        }
    }

    @Override
    public void onBackTestStarted() throws BackTestException {
        // Do nothing
    }

    @Override
    public void onBackTestFinished() throws BackTestException {
        Bar currentBar = backTester.getCurrentBar();
        for (Object key : this.positionManager.getOpenOrderKeys()) {
            Order order = this.positionManager.getOrder(key);
            if(order != null && false == order.isClosed()){

                BigDecimal currentClosePrice = currentBar.getClose();

                order.setCloseDate(currentBar.getDate());
                this.positionManager.closeOrder(order, currentClosePrice);
            }
        }
    }

    @Override
    public void onTick() {
        // Do nothing
    }

    @Override
    public void onBarOpen() throws TradingException {
        Bar currentBar = backTester.getCurrentBar();

        calendar.setTime(currentBar.getDate());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if(hour == OPEN_HOUR){
            Bar previousBar = backTester.getBackTestDataProvider().getBar(1);

            BigDecimal gapSize = TradeUtils.getGapSize(currentBar, previousBar);

            // Gap up
            isOpenedWithGap = gapSize.doubleValue() > 0;
            return;
        }
        // Nex bar after opening with gap
        if(isOpenedWithGap && hour == OPEN_HOUR + 1){
            BigDecimal barSize = currentBar.getClose().subtract(currentBar.getOpen());
            if(barSize.doubleValue() > 0)
                positionManager.openOrder(
                        currentBar.getDate(),
                        backTester.getBackTestDataProvider().getQuote(),
                        BigDecimal.ONE,
                        currentBar.getClose(),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        TradeType.BUY);
        }
    }
}
