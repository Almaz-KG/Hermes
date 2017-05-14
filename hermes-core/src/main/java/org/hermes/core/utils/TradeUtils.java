package org.hermes.core.utils;

import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.position.Order;
import org.hermes.core.entities.position.TradeType;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.QuotePriceType;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Almaz on 22.09.2015.
 */
public final class TradeUtils {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    private TradeUtils() {
    }

    public static BigDecimal getMaxPrice(QuoteHistory history){
        BigDecimal max = getMaxPrice(history, QuotePriceType.CLOSE);

        for (QuotePriceType quotePriceType : QuotePriceType.values()) {
            if(quotePriceType == QuotePriceType.CLOSE)
                continue;

            BigDecimal maxPrice = getMaxPrice(history, quotePriceType);
            if(maxPrice.doubleValue() > max.doubleValue())
                max = maxPrice;
        }

        return max;
    }

    public static BigDecimal getMaxPrice(QuoteHistory history, QuotePriceType priceType){
        switch (priceType){
            case CLOSE: return maxClosePrice(history);
            case LOW: return maxLowPrice(history);
            case HIGH: return maxHighPrice(history);
            case OPEN: return maxOpenPrice(history);

            default: throw new UnsupportedOperationException("Unknown priceType");
        }
    }

    public static BigDecimal getMinPrice(QuoteHistory history){
        BigDecimal min = getMinPrice(history, QuotePriceType.CLOSE);

        for (QuotePriceType quotePriceType : QuotePriceType.values()) {
            if(quotePriceType == QuotePriceType.CLOSE)
                continue;

            BigDecimal minPrice = getMinPrice(history, quotePriceType);
            if(minPrice.doubleValue() < min.doubleValue())
                min = minPrice;
        }

        return min;
    }

    public static BigDecimal getMinPrice(QuoteHistory history, QuotePriceType priceType){
        switch (priceType){
            case CLOSE: return minClosePrice(history);
            case LOW: return minLowPrice(history);
            case HIGH: return minHighPrice(history);
            case OPEN: return minOpenPrice(history);

            default: throw new UnsupportedOperationException("Unknown priceType type");
        }
    }

    public static BigDecimal getAveragePrice(QuoteHistory history, QuotePriceType priceType){
        List<Bar> Bars = history.getHistory();
        return Bars.size() == 0 ? BigDecimal.ZERO : getSumPrice(history, priceType).divide(BigDecimal.valueOf(Bars.size()));
    }

    private static BigDecimal getSumPrice(QuoteHistory history, QuotePriceType priceType){
        BigDecimal sum = BigDecimal.ZERO;
        switch (priceType){
            case CLOSE:
                for (Bar Bar : history.getHistory())
                    sum = sum.add(Bar.getClose());
                return sum;
            case LOW:
                for (Bar Bar : history.getHistory())
                    sum = sum.add(Bar.getLow());
                return sum;
            case HIGH:
                for (Bar Bar : history.getHistory())
                    sum = sum.add(Bar.getHigh());
                return sum;
            case OPEN:
                for (Bar Bar : history.getHistory())
                    sum = sum.add(Bar.getOpen());
                return sum;
            default: throw new UnsupportedOperationException("Unknown priceType type");
        }
    }

    private static BigDecimal minOpenPrice(QuoteHistory history) {
        BigDecimal min = history.getHistory().get(0).getOpen();

        for (Bar Bar : history.getHistory()) {
            if(Bar.getOpen().doubleValue() < min.doubleValue())
                min = Bar.getOpen();
        }
        return min;
    }

    private static BigDecimal minLowPrice(QuoteHistory history) {
        BigDecimal min = history.getHistory().get(0).getLow();

        for (Bar Bar : history.getHistory()) {
            if(Bar.getLow().doubleValue() < min.doubleValue())
                min = Bar.getLow();
        }
        return min;
    }

    private static BigDecimal minHighPrice(QuoteHistory history) {
        BigDecimal min = history.getHistory().get(0).getHigh();

        for (Bar Bar : history.getHistory()) {
            if(Bar.getHigh().doubleValue() < min.doubleValue())
                min = Bar.getHigh();
        }
        return min;
    }

    private static BigDecimal minClosePrice(QuoteHistory history) {
        BigDecimal min = history.getHistory().get(0).getClose();

        for (Bar Bar : history.getHistory()) {
            if(Bar.getClose().doubleValue() < min.doubleValue())
                min = Bar.getClose();
        }
        return min;
    }

    private static BigDecimal maxOpenPrice(QuoteHistory history) {
        BigDecimal max = history.getHistory().get(0).getOpen();

        for (Bar Bar : history.getHistory()) {
            if(Bar.getOpen().doubleValue() > max.doubleValue())
                max = Bar.getOpen();
        }
        return max;
    }

    private static BigDecimal maxLowPrice(QuoteHistory history) {
        BigDecimal max = history.getHistory().get(0).getLow();

        for (Bar Bar : history.getHistory()) {
            if(Bar.getLow().doubleValue() > max.doubleValue())
                max = Bar.getLow();
        }
        return max;
    }

    private static BigDecimal maxHighPrice(QuoteHistory history) {
        BigDecimal max = history.getHistory().get(0).getHigh();

        for (Bar Bar : history.getHistory()) {
            if(Bar.getHigh().doubleValue() > max.doubleValue())
                max = Bar.getHigh();
        }
        return max;
    }

    private static BigDecimal maxClosePrice(QuoteHistory history) {
        BigDecimal max = history.getHistory().get(0).getClose();

        for (Bar Bar : history.getHistory()) {
            if(Bar.getClose().doubleValue() > max.doubleValue())
                max = Bar.getClose();
        }
        return max;
    }

    public static BigDecimal getGapSize(Bar currentBar, Bar previousBar) {
        return currentBar.getOpen().subtract(previousBar.getClose());
    }

    public static BigDecimal getBarSizeOpenClose(Bar bar){
        return bar.getClose().subtract(bar.getOpen());
    }

    public static BigDecimal getBarSizeHighLow(Bar bar) {
        return bar.getHigh().subtract(bar.getLow());
    }

    public static BigDecimal getBarSizeOpenCloseAbs(Bar bar){
        return BigDecimal.valueOf(Math.abs(getBarSizeOpenClose(bar).doubleValue()));
    }

    public static BigDecimal getBarSizeHighLowLAbs(Bar bar) {
        return BigDecimal.valueOf(Math.abs(getBarSizeHighLow(bar).doubleValue()));
    }

    public static int getUniqueId(){
        return ID_GENERATOR.getAndIncrement();
    }

    public static double[] getPricesAsArray(QuoteHistory history, QuotePriceType priceType) {
        double[] result = new double[history.getHistory().size()];

        List<Bar> bars = history.getHistory();
        for (int i = 0; i < result.length; i++) {
            switch (priceType){
                case CLOSE: result[i] = bars.get(i).getClose().doubleValue(); continue;
                case OPEN: result[i] = bars.get(i).getOpen().doubleValue(); continue;
                case LOW: result[i] = bars.get(i).getLow().doubleValue(); continue;
                case HIGH: result[i] = bars.get(i).getHigh().doubleValue(); continue;
            }
        }

        return result;
    }

    public static boolean checkTakeProfit(Bar currentBar, Order order) {
        double low = currentBar.getLow().doubleValue();
        double high = currentBar.getHigh().doubleValue();
        double takeProfit = order.getTakeProfit().doubleValue();

        if(order.getTradeType() == TradeType.BUY){
            return takeProfit != 0 && high > takeProfit;
        } else if(order.getTradeType() == TradeType.SELL){
            return takeProfit != 0 && low < takeProfit;
        }

        return false;
    }

    public static boolean checkStopLoss(Bar currentBar, Order order) {
        double low = currentBar.getLow().doubleValue();
        double high = currentBar.getHigh().doubleValue();
        double stopLoss = order.getStopLoss().doubleValue();

        if(order.getTradeType() == TradeType.BUY){
            return stopLoss != 0 && low <= stopLoss;
        } else if(order.getTradeType() == TradeType.SELL){
            return stopLoss != 0 && high >= stopLoss;
        }
        return false;
    }
}
