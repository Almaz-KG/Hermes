package org.hermes.gui.swing.jfreechart;

import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.data.provider.integration.ResourceManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

import java.util.List;

/**
 * Created by Almaz
 * Date: 07.10.2015 19:39
 */
public class CandlestickChart {

    public static void main(String[] args) throws Exception{
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.XAUUSD, TimeFrame.Day1);

        OHLCDataItem[] items = convertToOHLCItems(quoteHistory.getHistory());

        OHLCDataset dataset = new DefaultOHLCDataset("Gold", items);
        JFreeChart chart = ChartFactory.createCandlestickChart(
                "",
                "",
                "",
                dataset,
                false
        );
        ChartFrame frame = new ChartFrame("Candlestck frame", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private static OHLCDataItem[] convertToOHLCItems(List<Bar> history) {
        OHLCDataItem[] result = new OHLCDataItem[history.size()];

        for (int i = 0; i < history.size(); i++) {
            Bar bar = history.get(i);
            result[i] = new OHLCDataItem(bar.getDate(),
                    bar.getOpen().doubleValue(),
                    bar.getHigh().doubleValue(),
                    bar.getLow().doubleValue(),
                    bar.getClose().doubleValue(),
                    0);
                    //bar.getValue()/ 100);
        }

        return result;
    }
}
