package org.hermes.backtest.examples.strategies.gap;

import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.TradingException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * sell:
 *  Если цена открытия меньше чем цена закрытия предыдущей торговой сессии (гэп вниз)
 *  Позицию закрываем в конце текущей торговой сессии.
 *
 * User: Almaz
 * Date: 23.08.14
 * Time: 17:19
 *
 */
public class GapStrategySellOnly extends AbstractGapStrategy{
    protected static final double DEFAULT_PREVIOUS_BAR_SIZE = 2.0;
    private int gapSizeInPercentage = 20;

    public GapStrategySellOnly(String strategyName, BackTester backTester, Map<String, Object> properties) {
        super(strategyName, backTester, properties);
    }

    @Override
    public void onBarOpen() throws TradingException {
        Bar currentBar = backTester.getCurrentBar();
        Bar previousBar = backTester.getBackTestDataProvider().getBar(1);

        if(previousBar == null)
            return;

        BigDecimal currentOpenPrice = currentBar.getOpen();
        BigDecimal previousClosePrice = previousBar.getClose();

        /**
         *  Цена открытия меньше чем цена закрытия на <code>gapSizeInPercentage</code>%
         *  от размера <code>Math.previousBarSize(open - close)</code> предыдущего бара
         */
        double previousBarSize = Math.abs(previousBar.getOpen().doubleValue() - previousBar.getClose().doubleValue());

        /**
         * Если размеры предыдущего бара слишком малы - то сигнал пропускаем
         */
        if(previousBarSize < DEFAULT_PREVIOUS_BAR_SIZE)
            return;
        double minPercentage = (previousBarSize * gapSizeInPercentage) / 100;

        double diff = currentOpenPrice.doubleValue() - previousClosePrice.doubleValue();
        // Gap down
        if(diff < 0 && (Math.abs(diff) > minPercentage)){
            this.positionManager.openOrder(
                    currentBar.getDate(),
                    backTester.getBackTestDataProvider().getQuote(),
                    BigDecimal.ONE,
                    currentOpenPrice,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    TradeType.SELL);
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
