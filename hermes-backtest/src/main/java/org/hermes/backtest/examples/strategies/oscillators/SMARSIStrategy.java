package org.hermes.backtest.examples.strategies.oscillators;

import org.hermes.backtest.BackTestStrategy;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.QuotePriceType;
import org.hermes.core.exceptions.HermesException;
import org.hermes.core.indicators.Indicator;
import org.hermes.core.indicators.helpers.PriceIndicator;
import org.hermes.core.indicators.oscillators.RSIIndicator;
import org.hermes.core.indicators.simple.MovingAverageIndicator;

import java.util.Map;

/**
 * Торговая стратегия
 * Покупать:
 * 1. 5 SMA пересекает 20 SMA снизу вверх
 * 2. Индекс относительной силы ниже 20
 * 3. Цена дневного закрытия выросла на 1% и цена недельного
 *      закрытия выросла на 1%
 * 4. Цена сегодняшнего закрытия выше цены закрытия вчерашнего
 *      закрытия и находится выше 50% торгового диапазона
 * 5. Сегодняшнее закрытие выше 4х предыдущих закрытий
 *
 * Продавать:
 * 1. 5 SMA пересекает 20 SMA с верху вниз
 * 2. Индекс относительной силы выше 80
 * 3. Цена дневного закрытия снизился на 1% м цена недельного
 *      закрытия снизился на 1%
 * 4. Цена сегодняшнего закрытия ниже цены закрытия вчерашнего
 *      закрытия и находится выше 50% торгового диапазона
 * 5. Сегодняшнее закрытие ниже 4х предыдущих закрытий
 *
 * Выход из сделки:
 * TODO: Провести исследование
 * 1. Выход по TP - исследование по величине TP
 * 2. По противоположенному сигналу
 *
 * Created by Almaz
 * Date: 21.10.2015
 */
public class SMARSIStrategy extends BackTestStrategy {

    public static String DEFAULT_STRATEGY_NAME = "";

    private MovingAverageIndicator fastMovingAverageIndicator;
    private MovingAverageIndicator slowMovingAverageIndicator;
    private Indicator<Double> rsi;

    private int fastMovingAveragePeriod = 5;
    private int slowMovingAveragePeriod = 20;
    private int rsiPeriod = 14;

    private double lastFastSmaValue;
    private double lastSlowSmaValue;

    public SMARSIStrategy(BackTester backTester, Map<String, Object> properties) {

        super(DEFAULT_STRATEGY_NAME, backTester, properties);

        this.fastMovingAverageIndicator = new MovingAverageIndicator(
                new PriceIndicator(backTester.getBackTestDataProvider(), QuotePriceType.CLOSE),
                MovingAverageIndicator.TYPE.SMA, fastMovingAveragePeriod);
        this.slowMovingAverageIndicator = new MovingAverageIndicator(
                new PriceIndicator(backTester.getBackTestDataProvider(), QuotePriceType.CLOSE),
                MovingAverageIndicator.TYPE.SMA, slowMovingAveragePeriod);


        this.rsi = new RSIIndicator(
                new PriceIndicator(backTester.getBackTestDataProvider(), QuotePriceType.CLOSE),
                rsiPeriod);

    }

    @Override
    public void onBackTestStarted() throws BackTestException {
    }

    @Override
    public void onBackTestFinished() throws BackTestException {
    }

    @Override
    public void onBarOpen() throws HermesException {
        // Do nothing
    }

    @Override
    public void onBarClose() throws HermesException {
            double fastSMA = fastMovingAverageIndicator.getValue();
            double slowSMA = slowMovingAverageIndicator.getValue();
            double rsiValue = rsi.getValue();

            checkBuySignals(fastSMA, slowSMA, rsiValue);
            checkSellSignals(fastSMA, slowSMA, rsiValue);

            lastFastSmaValue = fastSMA;
            lastSlowSmaValue = slowSMA;
    }

    private void checkSellSignals(double fastSMA, double slowSMA, double rsi) {

    }


    /***
     * Покупать:
     * 1. 5 SMA пересекает 20 SMA снизу вверх
     * 2. Индекс относительной силы ниже 20
     * 3. Цена дневного закрытия выросла на 1% м цена недельного
     *      закрытия выросла на 1%
     * 4. Цена сегодняшнего закрытия выше цены закрытия вчерашнего
     *      закрытия и находится выше 50% торгового диапазона
     * 5. Сегодняшнее закрытие выше 4х предыдущих закрытий
     */
    private void checkBuySignals(double fastSMA, double slowSMA, double rsi) {

        if (fastSMA < slowSMA && lastFastSmaValue > lastSlowSmaValue
                && rsi < 20) {
            Bar currentBar = this.backTester.getCurrentBar();

//            boolean result = false;
//            for (int i = 1; i <= 4; i++) {
//                if(backTester.getBackTestDataProvider().getBar(i).getClose() > currentBar.getClose())
//                    result = true;
//            }
        }
    }
}
