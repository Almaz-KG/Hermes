package org.hermes.gui.javafx.samples;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.data.provider.integration.ResourceManager;

import java.util.List;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        QuoteHistory quoteHistory = ResourceManager.getQuoteHistory(Quote.XAUUSD, TimeFrame.Day1);
        List<Bar> bars = quoteHistory.getHistory();

        CandleStickChart candleStickChart = new CandleStickChart(quoteHistory);
        Scene scene = new Scene(candleStickChart);

        stage.setTitle(quoteHistory.getQuote().getQuoteName());
        stage.setScene(scene);
        stage.show();

   //     candleStickChart.setYAxisFormatter(new DecimalAxisFormatter("#000.00"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
