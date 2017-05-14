package org.hermes.backtest.examples.researches;

import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.ResearchException;
import org.hermes.core.research.Research;
import org.hermes.core.research.ResearchResult;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Almaz
 * Date: 08.10.2015 13:23
 */
public class DayRangeResearch implements Research {

    protected QuoteHistory quoteHistory;

    public DayRangeResearch(QuoteHistory quoteHistory) {
        this.quoteHistory = quoteHistory;
    }

    public ResearchResult research() throws ResearchException {
        ResearchResult result = new ResearchResult(quoteHistory.getQuote());

        result.getProperties().put("startDate", quoteHistory.getHistory().get(0).getDate());
        result.getProperties().put("endDate", quoteHistory.getHistory().get(quoteHistory.getHistory().size() - 1).getDate());

        ResearchResult researchResult = research(quoteHistory, Calendar.MONDAY);
        result.getProperties().put("MONDAY", researchResult);

        researchResult = research(quoteHistory, Calendar.TUESDAY);
        result.getProperties().put("TUESDAY", researchResult);

        researchResult = research(quoteHistory, Calendar.WEDNESDAY);
        result.getProperties().put("WEDNESDAY", researchResult);

        researchResult = research(quoteHistory, Calendar.THURSDAY);
        result.getProperties().put("THURSDAY", researchResult);

        researchResult = research(quoteHistory, Calendar.FRIDAY);
        result.getProperties().put("FRIDAY", researchResult);

        return result;

    }

    private ResearchResult research(QuoteHistory quoteHistory, int dayOfWeek) {
        ResearchResult researchResult = new ResearchResult(quoteHistory.getQuote());

        List<Bar> bars = quoteHistory.getHistory();

        if (bars.size() == 0) {
            researchResult.getProperties().put("highLowDiff", 0);
            researchResult.getProperties().put("closeOpenDiff", 0);
            return researchResult;
        }
        Calendar calendar = Calendar.getInstance();

        // Value: high price - low price
        double highLow = 0;
        // Value: close price - open price
        double closeOpen = 0;

        for (Bar bar : bars) {
            calendar.setTime(bar.getDate());
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            if (day == dayOfWeek) {
                highLow += Math.abs(bar.getHigh().doubleValue() - bar.getLow().doubleValue());
                closeOpen += Math.abs(bar.getClose().doubleValue() - bar.getOpen().doubleValue());
            }
        }

        researchResult.getProperties().put("highLowDiff", highLow / bars.size());
        researchResult.getProperties().put("closeOpenDiff", closeOpen / bars.size());
        return researchResult;
    }

}
