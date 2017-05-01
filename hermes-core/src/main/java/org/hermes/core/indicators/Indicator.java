package org.hermes.core.indicators;

import org.hermes.core.entities.DataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.CalculationException;

/**
 * Created by Almaz
 * Date: 01.11.2015
 */
public interface Indicator<T> {

    DataProvider getDataProvider();

    T getValue() throws CalculationException;

    T getValue(Bar bar) throws CalculationException;

    T getValue(int index) throws CalculationException;
}
