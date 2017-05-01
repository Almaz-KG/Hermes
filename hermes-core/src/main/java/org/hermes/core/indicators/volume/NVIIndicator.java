package org.hermes.core.indicators.volume;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.CachedIndicator;

public class NVIIndicator extends CachedIndicator<Double> {

    public NVIIndicator(DataProvider dataProvider) {
        super(dataProvider);
    }

    @Override
    public Double calculate(int index) throws CalculationException {
        if(index == 0)
            return 1000.0;

        Bar currentBar = dataProvider.getBar(index);
        Bar previousBar = dataProvider.getBar(index - 1);

        Double previousValue = getValue(previousBar);
        if(currentBar.getValue().doubleValue() < previousBar.getValue().doubleValue()){
            double currentClosePrice = currentBar.getClose().doubleValue();
            double previousClosePrice = previousBar.getClose().doubleValue();

            double priceChangeRatio = (currentClosePrice - previousClosePrice) / previousClosePrice;
            return previousValue + (priceChangeRatio * previousValue);
        }
        return previousValue;
    }
}