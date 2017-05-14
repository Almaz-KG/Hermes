package org.hermes.core.indicators.simple;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.QuotePriceType;
import org.hermes.core.exceptions.CalculationException;
import org.hermes.core.indicators.helpers.PriceIndicator;
import org.hermes.core.utils.TradeUtils;

/**
 * Created by Almaz
 * Date: 20.10.2015
 */
public class BarSizeSimpleMovingAverageIndicator extends MovingAverageIndicator {
    public enum BarSizeOption{
        OPEN_CLOSE,
        HIGH_LOW
    }

    private DataProvider dataProvider;
    private BarSizeOption option;
    private int period;

    public BarSizeSimpleMovingAverageIndicator(DataProvider dataProvider, BarSizeOption option, int period) {
        super(new PriceIndicator(dataProvider, QuotePriceType.CLOSE), TYPE.SMA, period);
        this.dataProvider = dataProvider;
        this.option = option;
    }

    @Override
    public Double calculate() throws CalculationException {
        double result = 0;

        for (int i = 0; i < period; i++) {
            Bar bar = dataProvider.getBar(i);
            if(bar == null)
                return 0.0;

            result += getBarSize(bar);
        }

        return result / period;
    }

    private double getBarSize(Bar bar) throws CalculationException {
        switch (option){
            case HIGH_LOW: return TradeUtils.getBarSizeHighLowLAbs(bar).doubleValue();
            case OPEN_CLOSE: return TradeUtils.getBarSizeOpenCloseAbs(bar).doubleValue();
            default: throw new CalculationException ("Unknown calculation option");
        }
    }
}
