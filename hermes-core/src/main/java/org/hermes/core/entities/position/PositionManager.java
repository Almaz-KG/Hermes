package org.hermes.core.entities.position;


import org.hermes.core.entities.constants.Quote;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Almaz
 * Date: 31.10.2015
 */
public interface PositionManager {
    void addOrder(Order order);

    List<Order> getClosedOrders();

    void update(OpenOrder openOrder);

    void openOrder(Order order);

    void openOrder(Date openDate, Quote quote, BigDecimal size, BigDecimal
            openPrice, BigDecimal stopLoss, BigDecimal takeProfit, TradeType tradeType);

    Set<Object> getOpenOrderKeys();

    Order getOrder(Object key);

    void closeOrder(Order order, BigDecimal closePrice);

    void closePositions(TradeType tradeType, BigDecimal closePrice, Date closeDate);
}
