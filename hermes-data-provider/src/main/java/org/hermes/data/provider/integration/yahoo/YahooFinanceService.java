package org.hermes.data.provider.integration.yahoo;

import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.exceptions.QuoteException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Almaz on 07.01.17.
 */
public class YahooFinanceService {

    public static final Date DEFAULT_START_DATE = getDefaultStartDate();

    private YahooFinanceService(){}

    public static QuoteHistory getHistoricalPrices(Quote quote, TimeFrame timeFrame) throws QuoteException, IOException, ParseException {
        return getHistoricalPrices(quote, timeFrame, DEFAULT_START_DATE);
    }

    public static QuoteHistory getHistoricalPrices(Quote quote, TimeFrame timeFrame, Date startDate) throws QuoteException, IOException, ParseException {
        return getHistoricalPrices(quote, timeFrame, startDate, new Date());
    }

    public static QuoteHistory getHistoricalPrices(Quote quote, TimeFrame timeFrame, Date startDate, Date endDate) throws QuoteException, IOException {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);


        Stock stock = YahooFinance.get(quote.toString(), start, end);
        List<Bar> history = new ArrayList<>(stock.getHistory().size());

        stock.getHistory().forEach(h -> history.add(convert(h)));

        return new QuoteHistory(quote, timeFrame, history);
    }

    private static Bar convert(HistoricalQuote historicalQuote) {
        return new Bar(
                historicalQuote.getDate().getTime(),
                BigDecimal.valueOf(historicalQuote.getOpen().doubleValue()),
                BigDecimal.valueOf(historicalQuote.getHigh().doubleValue()),
                BigDecimal.valueOf(historicalQuote.getLow().doubleValue()),
                BigDecimal.valueOf(historicalQuote.getClose().doubleValue()),
                BigDecimal.valueOf(historicalQuote.getVolume()));
    }

    private static Date getDefaultStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 0, 0);
        return calendar.getTime();
    }
}
