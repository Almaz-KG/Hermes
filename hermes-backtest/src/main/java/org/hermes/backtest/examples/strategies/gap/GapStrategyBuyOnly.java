package org.hermes.backtest.examples.strategies.gap;

import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.TradingException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Almaz on 01.10.2015.
 *
 * buy:
 *  Если цена открытия больше чем цена закрытия предыдущей торговой сессии
 *  (гэп вверх)
 *  Позицию закрываем в конце текущей торговой сессии.
 *
 */
public class GapStrategyBuyOnly extends AbstractGapStrategy{

    protected static final double DEFAULT_PREVIOUS_BAR_SIZE = 2.0;

    private int gapSizeInPercentage = 20;

    public GapStrategyBuyOnly(String strategyName, BackTester backTester, Map<String, Object> properties) {
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
         *  Цена открытия больше чем цена закрытия на <code>gapSizeInPercentage</code>%
         *  от размера <code>Math.previousBarSize(open - close)</code> предыдущего бара
         */
        double previousBarSize = Math.abs(previousBar.getOpen().doubleValue() - previousBar.getClose().doubleValue());

        /**
         * Если размеры предыдущего бара слишком малы - то сигнал пропускаем
         */
        if(previousBarSize < DEFAULT_PREVIOUS_BAR_SIZE)
            return;
        double minPercentage = (previousBarSize * gapSizeInPercentage) / 100;

        BigDecimal diff = currentOpenPrice.subtract(previousClosePrice);
        // Gap upp
        if(diff.doubleValue() > 0 && (diff.doubleValue() > minPercentage)){
            positionManager.openOrder(
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
    public void onBackTestStarted() throws BackTestException {
        // Do nothing
    }

    @Override
    public void onBackTestFinished() throws BackTestException{
        onBarClose();
    }

}
