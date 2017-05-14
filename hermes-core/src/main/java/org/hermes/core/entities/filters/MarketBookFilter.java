package org.hermes.core.entities.filters;

import org.hermes.core.entities.price.Bar;

public interface MarketBookFilter {

    boolean accept(Bar bar);

}
