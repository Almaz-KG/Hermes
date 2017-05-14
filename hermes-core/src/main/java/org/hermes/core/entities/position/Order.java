package org.hermes.core.entities.position;

import org.hermes.core.entities.constants.Quote;
import org.hermes.core.exceptions.TradingException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * User: Almaz
 * Date: 23.08.14
 * Time: 18:46
 */
public class Order implements Cloneable {
    private String orderId;
    private Date openDate;
    private Date closeDate;
    private Quote quote;
    private BigDecimal value;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private TradeType tradeType;
    private boolean isClosed;

    public Order(Date openDate, Quote quote, BigDecimal value, BigDecimal openPrice, BigDecimal stopLoss, BigDecimal takeProfit, TradeType tradeType) {
        this.orderId = UUID.randomUUID().toString();
        this.openDate = openDate;
        this.quote = quote;
        this.value = value;
        this.openPrice = openPrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.tradeType = tradeType;
    }

    public Order(Date openDate, Date closeDate, Quote quote, BigDecimal value, BigDecimal openPrice, BigDecimal closePrice, BigDecimal stopLoss, BigDecimal takeProfit, TradeType tradeType) {
        this.orderId = UUID.randomUUID().toString();
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.quote = quote;
        this.value = value;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.tradeType = tradeType;
        this.isClosed = true;
    }

    public BigDecimal getResult() throws TradingException{
        if(!isClosed())
            throw new IllegalArgumentException("Order is not closed");

        if(tradeType == TradeType.BUY)
            return closePrice.subtract(openPrice);
        if(tradeType == TradeType.SELL)
            return openPrice.subtract(closePrice);

        throw new TradingException("Unknown order trade type (expected result: BUY, SELL; actual result: " + tradeType + ")");
    }

    public BigDecimal getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(BigDecimal takeProfit) {
        if(isClosed())
            throw new IllegalArgumentException("Order is closed");
        this.takeProfit = takeProfit;
    }

    public BigDecimal getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(BigDecimal stopLoss) {
        if(isClosed())
            throw new IllegalArgumentException("Order is closed");
        this.stopLoss = stopLoss;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        if(isClosed())
            throw new IllegalArgumentException("Order is closed");
        this.closePrice = closePrice;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        if(isClosed())
            throw new IllegalArgumentException("Order is closed");
        this.openPrice = openPrice;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        if(isClosed())
            throw new IllegalArgumentException("Order is closed");
        this.closeDate = closeDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Object getClone(){
        try {
            return this.clone();
        } catch (CloneNotSupportedException e){
            new TradingException(e);
        }
        return null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        if(isClosed())
            return new Order(this.openDate,
                this.closeDate,
                this.quote,
                this.value,
                this.openPrice,
                this.closePrice,
                this.stopLoss,
                this.takeProfit,
                this.tradeType);
        else
            return new Order(this.openDate, this.quote, this.value, this.openPrice,
                    this.stopLoss, this.takeProfit, this.tradeType);
    }
}

