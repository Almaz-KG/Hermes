package org.hermes.core.indicators.simple;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.PeriodicalIndicator;
import org.hermes.core.indicators.helpers.PriceIndicator;

public class MovingAverageIndicator extends PeriodicalIndicator<Double> {

    private PriceIndicator priceIndicator;
    private final TYPE movingAverageType;

    public MovingAverageIndicator(PriceIndicator priceIndicator, TYPE movingAverageType, int period) {
        super(priceIndicator, period);
        this.priceIndicator = priceIndicator;
        this.movingAverageType = movingAverageType;
    }

    @Override
    protected Double calculate(int index) throws CalculationException {
        switch (movingAverageType){
            case SMA: return sma(index);
            case EMA: return ema(index);
            case SMOOTHED: return smoothedMA(index);
            case LINEAR_WEIGHTED: return linearWeightedMA(index);

            default: throw new CalculationException("Unknown moving average type " + movingAverageType.toString());
        }
    }

    public Double calculate() throws CalculationException{
        return getValue(dataProvider.getCurrentBar());
    }

    @Override
    public DataProvider getDataProvider() {
        return dataProvider;
    }

    private double sma(int index) throws CalculationException {
        double sum = 0.0;
        for (int i =  Math.max(0, index - period + 1);
             i <= index; i++) {
            sum += priceIndicator.getValue(i);
        }
        return sum / Math.min(period, index + 1);
    }

    // TODO: Implement
    private double ema(int index){
        throw new UnsupportedOperationException("Operation is not supported yet");
//        // EMA(t) = a * Price(t) + (1 - a) * EMA(t - 1)
//        // a = 2 / (period + 1)
//        // EMA(1) = bars[period].closePrice
//        if(index == 0)
//            return dataProvider.getBar(period).getClose();
//
//        Bar bar = dataProvider.getBar(index);
//        // Looking in cache
//        if(this.cache.containsKey(bar))
//            return this.cache.get(bar);
//
//        double result = alpha * bar.getClose() + (1 - alpha) * ema(index - 1);
//        cache.put(bar, result);
//
//        return result ;
    }
    // TODO: Implement
    private double smoothedMA(int index) {
        throw new UnsupportedOperationException("Operation is not supported yet");
    }
    // TODO: Implement
    private double linearWeightedMA(int index) {
        throw new UnsupportedOperationException("Operation is not supported yet");
    }

    public enum TYPE{
        SMA,
        EMA,
        SMOOTHED,
        LINEAR_WEIGHTED
    }
}

