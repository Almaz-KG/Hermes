package org.hermes.core.indicators;


import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.CalculationException;

import java.util.*;

public abstract class CachedIndicator<T> extends AbstractIndicator<T> {

    private final Map<Bar, T> cache = new HashMap<>();

    public CachedIndicator(DataProvider dataProvider) {
        super(dataProvider);
    }

    public CachedIndicator(Indicator indicator) {
        this(indicator.getDataProvider());
    }

    protected abstract T calculate(int index) throws CalculationException;

    @Override
    public T getValue(int index) throws CalculationException {
        DataProvider provider = getDataProvider();
        if (provider == null) {
            throw new NullPointerException("DataProvider is null");
        }

        Bar bar = dataProvider.getBar(index);
        if (cache.containsKey(bar))
            return cache.get(bar);
        else {
            T result = calculate(index);
            cache.put(bar, result);
            return result;
        }
    }

    @Override
    public T getValue() throws CalculationException {
        return getValue(dataProvider.getCurrentBar());
    }

    @Override
    public T getValue(Bar bar) throws CalculationException {
        return getValue(dataProvider.indexOf(bar));
    }
}
