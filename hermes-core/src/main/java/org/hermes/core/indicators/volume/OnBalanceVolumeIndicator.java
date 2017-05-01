package org.hermes.core.indicators.volume;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.CachedIndicator;

public class OnBalanceVolumeIndicator extends CachedIndicator<Double> {

    public OnBalanceVolumeIndicator(DataProvider dataProvider) {
        super(dataProvider);
    }

    @Override
    public Double calculate(int index) throws CalculationException {
        if(index == 0)
            return 0.0;

        Bar previousBar = this.dataProvider.getBar(index - 1);
        Bar currentBar = this.dataProvider.getBar(index);

        if (previousBar.getClose().doubleValue() > currentBar.getClose().doubleValue())
            return getValue(index - 1) - (currentBar.getValue()).doubleValue();
        else if (previousBar.getClose().doubleValue() < currentBar.getClose().doubleValue())
            return getValue(index - 1) + (currentBar.getValue()).doubleValue();

        return getValue(index - 1);
    }
}
