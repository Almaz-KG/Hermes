package org.hermes.gui.javafx.orderbook.model;

import java.util.Collections;
import java.util.List;

public class MarketSnapshot {
    private List<Tick> tickList;

    public MarketSnapshot(List<Tick> tickList) {
        this.tickList = tickList;
    }

    public List<Tick> getTickList() {
        return Collections.unmodifiableList(tickList);
    }
}
