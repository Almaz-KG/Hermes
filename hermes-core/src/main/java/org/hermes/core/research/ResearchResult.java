package org.hermes.core.research;

import org.hermes.core.entities.constants.Quote;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Almaz
 * Date: 08.10.2015
 */
public class ResearchResult {

    protected BigDecimal initialBalance;
    protected Quote quote;
    protected Map<String, Object> properties;

    public ResearchResult(Quote quote) {
        this.properties = new HashMap<>();
        this.quote = quote;
    }

    public Quote getQuote() {
        return quote;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Object getProperty(String name){
        return getProperties().get(name);
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

}
