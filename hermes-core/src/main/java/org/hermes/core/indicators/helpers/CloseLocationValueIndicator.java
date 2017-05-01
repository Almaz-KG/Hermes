package org.hermes.core.indicators.helpers;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.CachedIndicator;

public class CloseLocationValueIndicator extends CachedIndicator<Double> {

    public CloseLocationValueIndicator(DataProvider dataProvider) {
        super(dataProvider);
    }

    @Override
    protected Double calculate(int index) throws CalculationException {
        Bar currentBar = dataProvider.getBar(index);

        double a = currentBar.getClose().doubleValue() - currentBar.getLow().doubleValue();
        double b = currentBar.getHigh().doubleValue() - currentBar.getClose().doubleValue();
        double c = currentBar.getHigh().doubleValue() - currentBar.getLow().doubleValue();

        return c == 0 ? 0.0 : (a - b) / c ;
    }
}
