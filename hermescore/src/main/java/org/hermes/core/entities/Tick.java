package org.hermes.core.entities;

public class Tick {
    private final String ticker;
    private final double price;
    private final TickType tickType;
    private final Long timestamp;
    private final Long value;

    public Tick(String ticker, double price, TickType tickType, Long value, Long timestamp) {
        this.ticker = ticker;
        this.price = price;
        this.tickType = tickType;
        this.timestamp = timestamp;
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public String getTicker() {
        return ticker;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }

    public TickType getTickType() {
        return tickType;
    }
}
