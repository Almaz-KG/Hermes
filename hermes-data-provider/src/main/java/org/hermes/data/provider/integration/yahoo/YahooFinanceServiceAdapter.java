package org.hermes.data.provider.integration.yahoo;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.exceptions.QuoteLoadException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Adapter for Yahoo Finance Service
 *
 *  Yahoo Finance Service provide only end of day data and
 *
 * Created by Almaz on 07.01.17.
 */
public class YahooFinanceServiceAdapter {
    private static final String YAHOO_FINANCE_URL = "http://ichart.yahoo.com/table.csv?";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD");

    public static final Date DEFAULT_START_DATE = getDefaultDate();

    private YahooFinanceServiceAdapter() {
    }

    public static QuoteHistory getHistoricalPrices(Quote quote, TimeFrame timeFrame) throws QuoteLoadException{
        return getHistoricalPrices(quote, timeFrame, DEFAULT_START_DATE);
    }

    public static QuoteHistory getHistoricalPrices(Quote quote, TimeFrame timeFrame, Date startDate) throws QuoteLoadException{
        return getHistoricalPrices(quote, timeFrame, startDate, new Date());
    }

    public static QuoteHistory getHistoricalPrices(Quote quote, TimeFrame timeFrame,
                                                   Date startDate, Date endDate) throws QuoteLoadException {
        List<Bar> history = getHistory(quote, timeFrame, startDate, endDate);

        return new QuoteHistory(quote, timeFrame, history);
    }

    private static List<Bar> getHistory(Quote quote, TimeFrame timeFrame, Date startDate, Date endDate) throws QuoteLoadException {
        try {
            String url = getUrl(quote, timeFrame, startDate, endDate);
            String response = HTTPUtils.get(url);
            return parseResponse(response);
        } catch (IOException | ParseException e) {
            throw new QuoteLoadException(e.getMessage(), e);
        }
    }

    private static List<Bar> parseResponse(String response) throws ParseException {
        String[] lines = response.split("\n");
        List<Bar> result = new ArrayList<>(lines.length);


        for (int i = 1; i < lines.length; i++) {
            result.add(parseBar(lines[i]));
        }

        return result;
    }

    private static Bar parseBar(String line) throws ParseException {
        String[] tokens = line.split(",");

        return new Bar(
                DATE_FORMAT.parse(tokens[0]),
                BigDecimal.valueOf(Double.parseDouble(tokens[1])), // OPEN
                BigDecimal.valueOf(Double.parseDouble(tokens[2])), // HIGH
                BigDecimal.valueOf( Double.parseDouble(tokens[3])), // LOW
                BigDecimal.valueOf(Double.parseDouble(tokens[4])), // CLOSE
                BigDecimal.valueOf(Double.parseDouble(tokens[5]))  // VOLUME
        );
    }

    private static String getUrl(Quote quote, TimeFrame timeFrame, Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        String quoteName = quote.toString();
        if(quote.getGroup() == Quote.Forex)
            quoteName += "%3DX";

        return YAHOO_FINANCE_URL +
                "s=" + quoteName +
                "&" + getStartDateUrlPart(start) +
                "&" + getEndDateUrlPart(end) +
                "&g=d&ignore=.csv";
    }

    private static String getStartDateUrlPart(Calendar start) {
        return buildUrl(start, new String[]{"a", "b", "c"});
    }

    private static String getEndDateUrlPart(Calendar end) {
        return buildUrl(end, new String[]{"d", "e", "f"});
    }

    private static String buildUrl(Calendar calendar, String[] params) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(params[0] + "=" + calendar.get(Calendar.MONTH));
        buffer.append("&" + params[1] + "=" + calendar.get(Calendar.DAY_OF_MONTH));
        buffer.append("&" + params[2] + "=" + calendar.get(Calendar.YEAR));

        return buffer.toString();
    }

    private static Date getDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 0, 0);
        return calendar.getTime();
    }

    private static class HTTPUtils {
        private HTTPUtils() {

        }

        public static String get(String url) throws IOException {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpGet httpget = new HttpGet(url);

                ResponseHandler<String> responseHandler = response -> {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    }

                    throw new ClientProtocolException("Unexpected response status: " + status + " for url " + url);
                };

                return httpclient.execute(httpget, responseHandler);
            } finally {
                httpclient.close();
            }
        }
    }
}

