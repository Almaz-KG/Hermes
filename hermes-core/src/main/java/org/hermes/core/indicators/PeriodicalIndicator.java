package org.hermes.core.indicators;


import org.hermes.core.entities.DataProvider;

/**
 * Created by Almaz
 * Date: 05.11.2015
  */
public abstract class PeriodicalIndicator<T> extends CachedIndicator<T> {
    protected final int period;

    protected PeriodicalIndicator(DataProvider dataProvider, int period) {
        super(dataProvider);
        this.period = period;
    }

    protected PeriodicalIndicator(Indicator indicator, int period) {
        super(indicator);
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }
}
