package org.hermes.backtest.examples.researches;

import org.hermes.backtest.BackTestDataProvider;
import org.hermes.core.entities.price.Bar;
import org.hermes.core.exceptions.ResearchException;
import org.hermes.core.research.Research;
import org.hermes.core.research.ResearchResult;
import org.hermes.core.utils.TradeUtils;

/**
 * Created by Almaz
 * Date: 31.10.2015
 *
 * Исследование:
 *
 * 1. В скольких процентах случаев происходит 2-ое закрытие вверх
 * 2. В скольких процентах случаев происходит 2-ое закрытие вниз
 * 3. В скольких процентах случаев происходит 3-ое закрытие вверх
 * 4. В скольких процентах случаев происходит 3-ое закрытие вниз
 * 5. В скольких процентах случаев происходит 4-ое закрытие вверх
 * 6. В скольких процентах случаев происходит 4-ое закрытие вниз
 * 7. В скольких процентах случаев происходит закрытие вверх после 2х кратного закрытия вниз
 * 8. В скольких процентах случаев происходит закрытие вниз после 2х кратного закрытия вверх
 * 9. В скольких процентах случаев происходит закрытие вверх после 3х кратного закрытия вниз
 *10. В скольких процентах случаев происходит закрытие вниз после 3х кратного закрытия вверх
 *
 <th>Up bars<br> after 2x down</th>
 <th>Down bars<br> after 2x up</th>
 <th>Up bars<br> after 3x down</th>
 <th>Down bars<br> after 3x up</th>
 */
public class BarUpDownPercentageResearch implements Research {

    private BackTestDataProvider dataProvider;

    private int doubleUpClose;
    private int doubleDownClose;
    private int threeUpClose;
    private int threeDownClose;
    private int fourUpClose;
    private int fourDownClose;
    private int upAfter2xDownClose;
    private int downAfter2xUpClose;
    private int upAfter3xDownClose;
    private int downAfter3xUpClose;

    private Bar currentBar;
    private Bar previousBar;
    private Bar thirdLastBar;
    private Bar fourthLastBar;

    public BarUpDownPercentageResearch(BackTestDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    private boolean doubleUpClose(){
        return  TradeUtils.getBarSizeOpenClose(currentBar).doubleValue()> 0 &&
                TradeUtils.getBarSizeOpenClose(previousBar).doubleValue() > 0;

    }

    private boolean doubleDownClose(){
        return TradeUtils.getBarSizeOpenClose(currentBar).doubleValue() < 0 &&
                TradeUtils.getBarSizeOpenClose(previousBar).doubleValue() < 0;
    }

    private boolean threeUpClose(){
        return doubleUpClose() && TradeUtils.getBarSizeOpenClose(thirdLastBar).doubleValue() > 0;
    }

    private boolean threeDownClose(){
        return doubleDownClose() && TradeUtils.getBarSizeOpenClose(thirdLastBar).doubleValue() < 0;
    }

    private boolean fourUpClose(){
        return threeUpClose() && TradeUtils.getBarSizeOpenClose(fourthLastBar).doubleValue() > 0;
    }

    private boolean fourDownClose(){
        return threeDownClose() && TradeUtils.getBarSizeOpenClose(fourthLastBar).doubleValue() < 0;
    }

    private boolean upAfter2xDownClose(){
        return doubleDownClose() && TradeUtils.getBarSizeOpenClose(thirdLastBar).doubleValue()> 0;
    }

    private boolean downAfter2xUpClose(){
        return doubleUpClose() && TradeUtils.getBarSizeOpenClose(thirdLastBar).doubleValue() < 0;
    }

    private boolean upAfter3xDownClose(){
        return threeDownClose() && TradeUtils.getBarSizeOpenClose(fourthLastBar).doubleValue() > 0;
    }

    private boolean downAfter3xUpClose(){
        return threeUpClose() && TradeUtils.getBarSizeOpenClose(fourthLastBar).doubleValue() < 0;
    }

    @Override
    public ResearchResult research() throws ResearchException {
        while (dataProvider.hasMoreData()){
            dataProvider.nextBar();
            changeBars(dataProvider.getCurrentBar());

            if(previousBar != null){
                doubleUpClose += doubleUpClose() ? 1 : 0;
                doubleDownClose += doubleDownClose() ? 1 : 0;
            }
            if(thirdLastBar != null){
                threeUpClose += threeUpClose() ? 1 : 0;
                threeDownClose += threeDownClose() ? 1 : 0;
                upAfter2xDownClose += upAfter2xDownClose() ? 1 : 0;
                downAfter2xUpClose += downAfter2xUpClose() ? 1 : 0;
            }
            if(fourthLastBar != null){
                fourUpClose += fourUpClose() ? 1 : 0;
                fourDownClose += fourDownClose() ? 1 : 0;
                upAfter3xDownClose += upAfter3xDownClose() ? 1 : 0;
                downAfter3xUpClose += downAfter3xUpClose() ? 1 : 0;
            }
        }

        return getResult();
    }

    private ResearchResult getResult() {
        ResearchResult result = new ResearchResult(dataProvider.getQuote());
        int barsCount = dataProvider.size();
        result.getProperties().put("barsCount", barsCount);

        result.getProperties().put("startDate", dataProvider.getFirstBar().getDate());
        result.getProperties().put("endDate", dataProvider.getLastBar().getDate());

        result.getProperties().put("doubleUpBarsCount", doubleUpClose);
        result.getProperties().put("doubleUpBarsCountPercentage", 100.0 * doubleUpClose / barsCount);

        result.getProperties().put("doubleDownBarsCount", doubleDownClose);
        result.getProperties().put("doubleDownBarsCountPercentage", 100.0 * doubleDownClose / barsCount);

        result.getProperties().put("threeUpBarsCount", threeUpClose);
        result.getProperties().put("threeUpBarsCountPercentage", 100.0 * threeUpClose / barsCount);

        result.getProperties().put("threeDownBarsCount", threeDownClose);
        result.getProperties().put("threeDownBarsCountPercentage", 100.0 * threeDownClose / barsCount);

        result.getProperties().put("fourUpBarsCount", fourUpClose);
        result.getProperties().put("fourUpBarsCountPercentage", 100.0 * fourUpClose / barsCount);

        result.getProperties().put("fourDownBarsCount", fourDownClose);
        result.getProperties().put("fourDownBarsCountPercentage", 100.0 * fourDownClose / barsCount);

        result.getProperties().put("upAfter2xDownBarsCount", upAfter2xDownClose);
        result.getProperties().put("upAfter2xDownBarsCountPercentage", 100.0 * upAfter2xDownClose / barsCount);

        result.getProperties().put("downAfter2xUpBarsCount", downAfter2xUpClose);
        result.getProperties().put("downAfter2xUpBarsCountPercentage", 100.0 * downAfter2xUpClose / barsCount);

        result.getProperties().put("upAfter3xDownBarsCount", upAfter3xDownClose);
        result.getProperties().put("upAfter3xDownBarsCountPercentage", 100.0 * upAfter3xDownClose / barsCount);

        result.getProperties().put("downAfter3xUpBarsCount", downAfter3xUpClose);
        result.getProperties().put("downAfter3xUpBarsCountPercentage", 100.0 * downAfter3xUpClose / barsCount);

        return result;
    }

    private void changeBars(Bar bar) {
        fourthLastBar = thirdLastBar;
        thirdLastBar = previousBar;
        previousBar = currentBar;
        currentBar = bar;
    }
}
