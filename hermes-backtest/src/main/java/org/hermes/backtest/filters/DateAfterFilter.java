package org.hermes.backtest.filters;


import org.hermes.core.entities.filters.MarketBookFilter;
import org.hermes.core.entities.price.Bar;

import java.util.Date;

public class DateAfterFilter implements MarketBookFilter {

    private Date startDate;

    public DateAfterFilter(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean accept(Bar bar) {
        return bar.getDate().after(startDate);
    }
}
