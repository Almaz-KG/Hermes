package org.hermes.core.indicators.volume;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.CachedIndicator;
import org.hermes.core.indicators.helpers.CloseLocationValueIndicator;

public class AccumulationDistributionIndicator extends CachedIndicator<Double> {

    private CloseLocationValueIndicator clvIndicator;

    public AccumulationDistributionIndicator(DataProvider dataProvider) {
        super(dataProvider);
        this.clvIndicator = new CloseLocationValueIndicator(dataProvider);
    }

    @Override
    protected Double calculate(int index) throws CalculationException {
        if (index == 0) {
            return 0.0;
        }

        // Calculating the money flow multiplier
        Double moneyFlowMultiplier = clvIndicator.getValue(index);

        // Calculating the money flow volume
        Double moneyFlowVolume = moneyFlowMultiplier * (dataProvider.getBar(index).getValue()).doubleValue();
        return moneyFlowVolume + (getValue(dataProvider.getBar(index - 1)));
    }
}
