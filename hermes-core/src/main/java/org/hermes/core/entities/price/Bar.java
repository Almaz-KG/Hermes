package org.hermes.core.entities.price;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bar {

    private static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy.MM.dd";
    private static final SimpleDateFormat DEFAULT_SIMPLE_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);

    private final Date date;
    private final BigDecimal open;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal close;
    private final BigDecimal value;

    public Bar(Date date, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, BigDecimal value) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" date: ").append(DEFAULT_SIMPLE_DATE_FORMAT.format(this.date));
        sb.append(" open: ").append(open);
        sb.append(" high: ").append(high);
        sb.append(" low: ").append(low);
        sb.append(" close: ").append(close);
        sb.append(" value: ").append(value);
        return sb.toString();
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
}
