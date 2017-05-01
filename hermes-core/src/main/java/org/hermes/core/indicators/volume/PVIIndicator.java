package org.hermes.core.indicators.volume;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.CachedIndicator;

public class PVIIndicator extends CachedIndicator<Double> {

    public PVIIndicator(DataProvider dataProvider) {
        super(dataProvider);
    }

    @Override
    protected Double calculate(int index) throws CalculationException {
        if (index == 0) {
            return 1000.0;
        }

        Bar currentBar = dataProvider.getBar(index);
        Bar previousBar = dataProvider.getBar(index - 1);
        double previousValue = getValue(index - 1);

        if (currentBar.getValue().doubleValue() > previousBar.getValue().doubleValue()) {
            double currentPrice = currentBar.getClose().doubleValue();
            double previousPrice = previousBar.getClose().doubleValue();
            double priceChangeRatio = (currentPrice - (previousPrice)) / (previousPrice);
            return previousValue + (priceChangeRatio * previousValue);
        }
        return previousValue;
    }
}