package org.hermes.data.provider.integration;

import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.exceptions.QuoteException;
import org.hermes.core.exceptions.QuoteLoadException;
import org.hermes.core.exceptions.ResourceLoadException;
import org.hermes.core.exceptions.UnsupportedTimeFrameException;
import org.hermes.data.provider.integration.yahoo.YahooFinanceServiceAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Almaz
 * Date: 15.10.2015
 */
public class ResourceManager {

    private static final Logger logger = Logger.getLogger(ResourceManager.class.getCanonicalName());

    private ResourceManager() {
    }

    public static QuoteHistory getQuoteHistory(Quote quote, TimeFrame timeFrame) throws ResourceLoadException {
        try {
            return YahooFinanceServiceAdapter.getHistoricalPrices(quote, timeFrame);
        } catch (QuoteLoadException e) {
//            logger.log(Level.SEVERE, "Yahoo finance service adapter fail with ", e);
//            logger.log(Level.SEVERE, "Trying to load data from local storage");
            return getQuoteHistoryFromLocal(quote, timeFrame);
        }
    }

    public static QuoteHistory getQuoteHistoryFromLocal(Quote quote, TimeFrame timeFrame) throws ResourceLoadException{
        try {
            String folder = getResourceFolder(quote, timeFrame);
            return IOUtils.getHistoryData(folder, quote, timeFrame);
        } catch (Exception e){
            logger.log(Level.SEVERE, "Loading data from local storage fail", e);
            throw new ResourceLoadException("Loading data from local storage fail", e);
        }
    }
    public static List<QuoteHistory> getQuoteHistories(Quote group, TimeFrame timeFrame) throws ResourceLoadException {
        List<QuoteHistory> result = new ArrayList<>();

        try {
            for (Quote quote : Quote.getQuotesByGroup(group)) {
                long l = System.currentTimeMillis();
                result.add(getQuoteHistory(quote, timeFrame));
                logger.info(quote + " readed. Time elapsed: " + (System.currentTimeMillis() - l) + " ms");
            }
        } catch (QuoteException e) {
            throw new ResourceLoadException("Loading data fail", e);
        }
        return result;
    }

    private static String getResourceFolder(Quote quote, TimeFrame timeFrame) throws UnsupportedTimeFrameException {
        String separator = File.separator;
        String directory = ProjectProperties.getInstance().getQuotesHistoryHomeDirectory();
        String timeFrameFolder = getFolderNameFromTimeFrame(timeFrame);
        String quoteGroup = quote.getGroup().toString();

        return directory + separator + timeFrameFolder + separator + quoteGroup + separator + quote + ".csv";
    }

    private static String getFolderNameFromTimeFrame(TimeFrame timeFrame) throws UnsupportedTimeFrameException {
        switch (timeFrame){
            case Minute1: return "M1";
            case Minute5: return "M5";
            case Minute15: return "M15";
            case Minute30: return "M30";
            case Day1: return "D1";
            case Day3: return "D3";
            case Hour1: return "H1";
            case Hour4: return "H4";
            case Week1: return "W1";

            default: throw new UnsupportedTimeFrameException("Unknown timeframe for " + timeFrame);
        }
    }
}
