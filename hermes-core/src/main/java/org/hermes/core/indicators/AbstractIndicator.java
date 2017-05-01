package org.hermes.core.indicators;


import org.hermes.core.entities.DataProvider;

public abstract class AbstractIndicator<T> implements Indicator<T> {

    protected DataProvider dataProvider;

    protected AbstractIndicator(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }
}
