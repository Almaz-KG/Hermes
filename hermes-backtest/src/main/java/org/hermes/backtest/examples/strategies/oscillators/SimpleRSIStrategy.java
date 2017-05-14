package org.hermes.backtest.examples.strategies.oscillators;

import org.hermes.backtest.BackTestStrategy;
import org.hermes.backtest.BackTester;
import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.QuotePriceType;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.exceptions.HermesException;
import org.hermes.core.indicators.helpers.PriceIndicator;
import org.hermes.core.indicators.oscillators.RSIIndicator;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Almaz
 * Date: 26.10.2015
 *
 * Простая стратегия с использованием осцилятора RSI
 * =================================================
 * Buy:
 *   Если RSI(14) ниже 30 и нет открытых позиций
 * Sell
 *   Если RSI(14) выше 70 и нет открытых позиций
 *
 * Выход:
 *  По противоположенному сигналу
 */
public class SimpleRSIStrategy extends BackTestStrategy {

    public static final String DEFAULT_STRATEGY_NAME = "Simple rsi strategy";

    private double rsiSellSignalValue = 70.0;
    private double rsiBuySignalValue = 30.0;

    private RSIIndicator rsi;
    private int rsiPeriod = 14;
    private double previousRSIValue = 0;


    public SimpleRSIStrategy(BackTester backTester, Map<String, Object> properties) {
        super(DEFAULT_STRATEGY_NAME, backTester, properties);
        this.rsi = new RSIIndicator(
                new PriceIndicator(backTester.getBackTestDataProvider(), QuotePriceType.CLOSE),
                rsiPeriod);
    }

    @Override
    public void onBackTestStarted() throws BackTestException {
    }

    @Override
    public void onBackTestFinished() throws BackTestException {
        Bar currentBar = backTester.getCurrentBar();

        this.positionManager.closePositions(TradeType.BUY, currentBar.getClose(), currentBar.getDate());
        this.positionManager.closePositions(TradeType.SELL, currentBar.getClose(), currentBar.getDate());
    }

    @Override
    public void onBarOpen() throws HermesException{
        // Do nothing
    }

    @Override
    public void onBarClose() throws HermesException{
        BigDecimal closePrice = this.getBackTester().getCurrentBar().getClose();
        double rsiValue = rsi.getValue();

        checkSellPositions(rsiValue, closePrice);
        checkBuyPositions(rsiValue, closePrice);

        previousRSIValue = rsiValue;

    }

    private void checkBuyPositions(double rsiValue, BigDecimal closePrice) throws BackTestException {
        if(rsiValue < rsiBuySignalValue &&
                previousRSIValue < rsiValue
                && !existPosition(TradeType.BUY)) {
            Bar currentBar = this.backTester.getCurrentBar();
            this.positionManager.closePositions(TradeType.SELL, closePrice, currentBar.getDate());

            Order order = new Order(
                    currentBar.getDate(),
                    backTester.getBackTestDataProvider().getQuote(),
                    BigDecimal.ONE, // Size
                    currentBar.getClose(), // Open price
                    BigDecimal.ZERO, // SL
                    BigDecimal.ZERO, // TP
                    TradeType.BUY);
            positionManager.openOrder(order);
        }
    }

    private void checkSellPositions(double rsiValue, BigDecimal closePrice) throws BackTestException {
        if(rsiValue > rsiSellSignalValue &&
                previousRSIValue > rsiValue
                && !existPosition(TradeType.SELL)) {
            Bar currentBar = this.backTester.getCurrentBar();

            this.positionManager.closePositions(TradeType.BUY, closePrice, currentBar.getDate());

            Order order = new Order(
                    currentBar.getDate(),
                    backTester.getBackTestDataProvider().getQuote(),
                    BigDecimal.ONE, // Size
                    currentBar.getClose(), // Open price
                    BigDecimal.ZERO, // SL
                    BigDecimal.ZERO, // TP
                    TradeType.SELL);
            positionManager.openOrder(order);
        }
    }

    private boolean existPosition(TradeType type) {
        for (Object key : this.positionManager.getOpenOrderKeys()) {
            if (positionManager.getOrder(key).getTradeType() == type)
                return true;
        }

        return false;
    }
}
