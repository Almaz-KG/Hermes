package org.hermes.core.entities.filters;

import org.hermes.core.entities.price.Bar;

/**
 * Created by Almaz on 14.05.2017.
 */
public class AcceptAllFilter implements MarketBookFilter {

    @Override
    public boolean accept(Bar bar) {
        return true;
    }

}
