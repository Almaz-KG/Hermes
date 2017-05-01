package org.hermes.core.indicators.helpers;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.QuotePriceType;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.CachedIndicator;

public class PriceIndicator extends CachedIndicator<Double> {
    private QuotePriceType type;

    public PriceIndicator(DataProvider dataProvider, QuotePriceType type) {
        super(dataProvider);
        this.type = type;
    }

    @Override
    protected Double calculate(int index) throws CalculationException {
        switch (type) {
            case OPEN: return open(index);
            case HIGH: return high(index);
            case LOW: return low(index);
            case CLOSE: return close(index);
            case MEDIAN_OPEN_CLOSE: return openClose(index);
            case MEDIAN_OPEN_HIGH: return openHigh(index);
            case MEDIAN_OPEN_LOW: return openLow(index);
            case MEDIAN_HIGH_LOW: return highLow(index);
            case MEDIAN_HIGH_CLOSE: return highClose(index);
            case MEDIAN_LOW_CLOSE: return lowClose(index);
            case MEDIAN: return median(index);
        }
        throw new CalculationException("Unsupported type for "+ type);
    }

    private double median(int index) {
        return (open(index) + high(index) + low(index) + close(index)) / 4.0;
    }

    private double close(int index) {
        return dataProvider.getBar(index).getClose().doubleValue();
    }
    private double low(int index) {
        return dataProvider.getBar(index).getLow().doubleValue();
    }
    private double high(int index) {
        return dataProvider.getBar(index).getHigh().doubleValue();
    }
    private double open(int index) {
        return dataProvider.getBar(index).getOpen().doubleValue();
    }

    private double lowClose(int index) {
        return Math.abs(low(index) + close(index)) / 2.0;
    }
    private double highClose(int index) {
        return Math.abs(high(index) + close(index)) / 2.0;
    }
    private double openClose(int index) {
        return Math.abs(open(index) + close(index)) / 2.0;
    }
    private double openHigh(int index) {
        return Math.abs(open(index) + high(index)) / 2.0;
    }
    private double highLow(int index) {
        return Math.abs(high(index) + low(index)) / 2.0;
    }
    private double openLow(int index) {
        return Math.abs(open(index) + low(index)) / 2.0;
    }
}
