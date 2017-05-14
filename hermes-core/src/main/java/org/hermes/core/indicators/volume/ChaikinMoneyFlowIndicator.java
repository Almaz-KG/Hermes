package org.hermes.core.indicators.volume;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.CachedIndicator;
import org.hermes.core.indicators.helpers.CloseLocationValueIndicator;

public class ChaikinMoneyFlowIndicator extends CachedIndicator<Double> {

    private CloseLocationValueIndicator clvIndicator;
    private int period;

    public ChaikinMoneyFlowIndicator(DataProvider dataProvider, int period) {
        super(dataProvider);
        this.period = period;
        this.clvIndicator = new CloseLocationValueIndicator(dataProvider);
    }

    @Override
    protected Double calculate(int index) throws CalculationException {
        int startIndex = Math.max(0, index - period + 1);
        Double sumOfMoneyFlowVolume = 0.0;
        for (int i = startIndex; i <= index; i++) {
            sumOfMoneyFlowVolume += (getMoneyFlowVolume(i));
        }

        Double sumVolume = getSumVolume(index);

        return sumVolume == 0 ? 0 : sumOfMoneyFlowVolume / sumVolume;
    }

    private double getSumVolume(int index){
        double result = 0;
        int startIndex = Math.max(0, index - period + 1);
        for (int i = startIndex; i <= index; i++) {
            Bar bar = dataProvider.getBar(i);
            if(bar == null)
                return result;

            result += bar.getValue().doubleValue();
        }

        return result;
    }
    private Double getMoneyFlowVolume(int index) throws CalculationException {
        Bar bar = dataProvider.getBar(index);

        if(bar != null) {
            Double clvIndicatorValue = clvIndicator.getValue(bar);
            double barValue = bar.getValue().doubleValue();
            return clvIndicatorValue * barValue;
        }
        return 0.0;
    }
}
