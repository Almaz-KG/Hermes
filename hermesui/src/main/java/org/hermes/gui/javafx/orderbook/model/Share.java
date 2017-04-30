package org.hermes.gui.javafx.orderbook.model;

public class Share {
    private final String ticker;
    private String companyName;

    public Share(String ticker, String companyName) {
        this.ticker = ticker;
        this.companyName = companyName;
    }


    public String getTicker() {
        return ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return ticker.toUpperCase();
    }
}
