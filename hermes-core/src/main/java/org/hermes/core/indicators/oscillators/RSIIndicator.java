package org.hermes.core.indicators.oscillators;

import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.PeriodicalIndicator;
import org.hermes.core.indicators.helpers.AverageGainIndicator;
import org.hermes.core.indicators.helpers.AverageLossIndicator;
import org.hermes.core.indicators.helpers.PriceIndicator;

public class RSIIndicator extends PeriodicalIndicator<Double> {

    private AverageGainIndicator averageGainIndicator;
    private AverageLossIndicator averageLossIndicator;

    public RSIIndicator(PriceIndicator priceIndicator, int period) {
        super(priceIndicator, period);
        this.averageGainIndicator = new AverageGainIndicator(priceIndicator, period);
        this.averageLossIndicator = new AverageLossIndicator(priceIndicator, period);
    }

    @Override
    protected Double calculate(int index) throws CalculationException {
        return 100 - 100 / (1 + (relativeStrength(index)));
    }

    private Double relativeStrength(int index) throws CalculationException {
        if (index == 0) {
            return 0.0;
        }
        double averageGain = averageGainIndicator.getValue(index);
        double averageLoss = averageLossIndicator.getValue(index);
        return averageLoss != 0 ? averageGain / (averageLoss) : averageGain;
    }
}
