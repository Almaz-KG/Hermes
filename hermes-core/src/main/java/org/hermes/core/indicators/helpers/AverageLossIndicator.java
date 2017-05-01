package org.hermes.core.indicators.helpers;

import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.Indicator;
import org.hermes.core.indicators.PeriodicalIndicator;

public class AverageLossIndicator extends PeriodicalIndicator<Double> {
    private Indicator priceIndicator;

    public AverageLossIndicator(Indicator indicator, int period) {
        super(indicator, period);
        this.priceIndicator = indicator;
    }

    @Override
    protected Double calculate(int index) throws CalculationException {
        double result = 0.0;

        for (int i = Math.max(1, index - period + 1); i <= index; i++) {
            double previousValue = (double) priceIndicator.getValue(i - 1);
            double currentValue = (double) priceIndicator.getValue(i);
            if( previousValue > currentValue)
                result += (previousValue - currentValue);
        }
        int realPeriod = Math.min(period, index + 1);
        return result / realPeriod;
    }
}
