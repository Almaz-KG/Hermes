package org.hermes.core.utils;

import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.exceptions.QuoteException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVReader {

    private static final char DELIMITER = ',';
    private static final String DATE_FORMAT_TEMPLATE = "yyyy.MM.dd HH:mm";
    private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_TEMPLATE);


    /***
     * The simple csv file parser. We have assumption about csv column structure.
     *
     * Expected column descriptions
     * COLUMN_0 = DATE in format (yyyy.MM.dd)
     * COLUMN_1 = HOUR in format (hh:mm)
     * COLUMN_2 = OPEN PRICE (the DELIMITER of price is ('.'))
     * COLUMN_3 = HIGH PRICE (the DELIMITER of price is ('.'))
     * COLUMN_4 = LOW  PRICE (the DELIMITER of price is ('.'))
     * COLUMN_5 = CLOSE PRICE (the DELIMITER of price is ('.'))
     * COLUMN_6 = VALUE
     */
    public static QuoteHistory read(File file, Quote quote, TimeFrame timeFrame) throws IOException, ParseException, QuoteException {
        List<String> allLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());

        QuoteHistory result = new QuoteHistory(quote, timeFrame);

        for (String line : allLines) {
            String[] args = line.split(String.valueOf(DELIMITER));

            // Skip empty lines
            if(args.length == 1 && args[0].isEmpty())
                continue;
            if(args.length != 7)
                throw new ParseException("Illegal type of input line", 0);

            Date stockBarDate = getStockBarDate(args[0], args[1]);
            BigDecimal openPrice = BigDecimal.valueOf(Double.parseDouble(args[2]));
            BigDecimal maxPrice = BigDecimal.valueOf(Double.parseDouble(args[3]));
            BigDecimal minPrice = BigDecimal.valueOf(Double.parseDouble(args[4]));
            BigDecimal closePrice = BigDecimal.valueOf(Double.parseDouble(args[5]));
            BigDecimal value = BigDecimal.valueOf(Double.parseDouble(args[6]));

            result.getHistory().add(new Bar(stockBarDate, openPrice, maxPrice, minPrice, closePrice, value));
        }

        return result;

    }

    public static List<QuoteHistory> load(File directory) throws IOException, ParseException, QuoteException {
        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            List<QuoteHistory> histories = new ArrayList<>();
            for (File file: files){
                if(file.isFile()){
                    Quote name = getQuoteName(file.getName());
                    TimeFrame timeFrame = getTimeFrame(file.getName());

                    QuoteHistory history = read(file, name, timeFrame);
                    if(history != null)
                        histories.add(history);
                }
            }
            return histories;
        }
        return  null;
    }

    private static Quote getQuoteName(String fileName) throws QuoteException {
        if(fileName.endsWith(".csv")){
            int index = 0;
            if(fileName.startsWith("#"))
                index = 1;
            int last = fileName.indexOf(".csv");

            return Quote.getQuoteByTicker(fileName.substring(index, last));
        }
        throw new QuoteException("Quote not found for file name, " +fileName);
    }

    private static TimeFrame getTimeFrame(String fileName) {
        if(fileName.endsWith(".csv")){
            String reverse = new StringBuilder(fileName).reverse().toString();
            int start = reverse.indexOf(".");

            int end = start;
            char[] chars = reverse.toCharArray();
            for (int i = start + 1; i < chars.length; i++) {
                if(Character.isDigit(chars[i]))
                    end++;
                else
                    break;
            }

            if(end > start){
                String minutes = reverse.substring(start + 1, end + 1);
                String minute = new StringBuilder(minutes).reverse().toString();

                int i = Integer.parseInt(minute);
                return TimeFrame.getTimeFrame(i);
            }
        }
        return null;
    }

    private static Date getStockBarDate(String date, String time) throws ParseException {
        return DEFAULT_DATE_FORMAT.parse(date + " " + time);
    }
}
