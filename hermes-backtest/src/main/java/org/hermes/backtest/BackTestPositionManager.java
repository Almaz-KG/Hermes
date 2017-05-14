package org.hermes.backtest;


import org.hermes.backtest.exceptions.BackTestException;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.position.OpenOrder;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.PositionManager;
import org.hermes.core.entities.position.TradeType;

import java.math.BigDecimal;
import java.util.*;

public class BackTestPositionManager implements PositionManager {
    private BackTester backTester;
    private Map<Object, Order> currentOrders;

    public BackTestPositionManager(BackTester backTester) {
        this.currentOrders = new HashMap<>();
        this.backTester = backTester;
    }

    @Override
    public void addOrder(Order order){
        currentOrders.put(order.getOrderId(), order);
    }

    @Override
    public List<Order> getClosedOrders() {
        List<Order> closedOrders = new ArrayList<>();
        for (Object key : this.currentOrders.keySet()) {
            closedOrders.add(currentOrders.get(key));
        }

        return closedOrders;
    }

    @Override
    public void update(OpenOrder openOrder) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void openOrder(Order order){
        try {
            validateOrder(order);
            currentOrders.put(order.getOrderId(), order);
        } catch (BackTestException e) {
            throw new RuntimeException(e);
        }
    }


    private void validateOrder(Order order) throws BackTestException{
        if(order == null)
            throw new BackTestException("Illegal order to open, null");
        if(order.getTradeType() == null)
            throw new BackTestException("Unknown order trade type");
        if(order.isClosed())
            throw new BackTestException("Illegal order, order is closed");
        if(order.getOpenPrice() != backTester.getCurrentPrice())
            throw new BackTestException("Illegal open price - no accessible price");
    }

    @Override
    public void openOrder(Date openDate, Quote quote, BigDecimal size, BigDecimal
            openPrice, BigDecimal stopLoss, BigDecimal takeProfit, TradeType tradeType) {
        Order order = new Order(openDate, quote, size, openPrice, stopLoss, takeProfit, tradeType);
        currentOrders.put(order.getOrderId(), order);
    }

    @Override
    public Set<Object> getOpenOrderKeys(){
        Set<Object> openOrderKeys = new HashSet<>();

        for (Object key : currentOrders.keySet()) {
            if(!currentOrders.get(key).isClosed())
                openOrderKeys.add(key);
        }
        return openOrderKeys;
    }

    @Override
    public Order getOrder(Object key){
        return currentOrders.get(key);
    }

    @Override
    public void closeOrder(Order order, BigDecimal closePrice){
        if(currentOrders.containsValue(order) && false == order.isClosed()){
            order.setClosePrice(closePrice);
            order.setClosed(true);
        }
    }

    @Override
    public void closePositions(TradeType tradeType, BigDecimal closePrice, Date closeDate) {
        switch (tradeType) {
            case BUY: closeBuyPositions(closePrice, closeDate);
            case SELL: closeSellPositions(closePrice, closeDate);
        }
    }

    private void closeSellPositions(BigDecimal closePrice, Date closeDate) {
        for (Object key : this.getOpenOrderKeys()) {
            Order order = this.getOrder(key);

            if(!order.isClosed() && order.getTradeType() == TradeType.SELL){
                order.setCloseDate(closeDate);
                closeOrder(order, closePrice);
            }
        }
    }
    private void closeBuyPositions(BigDecimal closePrice, Date closeDate) {
        for (Object key : this.getOpenOrderKeys()) {
            Order order = this.getOrder(key);

            if(!order.isClosed() && order.getTradeType() == TradeType.BUY){
                order.setCloseDate(closeDate);
                closeOrder(order, closePrice);
            }
        }
    }
}
