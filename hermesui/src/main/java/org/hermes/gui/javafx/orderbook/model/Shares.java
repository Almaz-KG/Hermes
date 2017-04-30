package org.hermes.gui.javafx.orderbook.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shares {
    private static final List<Share> shares = initShares();

    private static List<Share> initShares() {
        List<Share> result = new ArrayList<>();

        result.add(new Share("SBER", "OAO Sberbank"));
        result.add(new Share("GZPR", "OAO Gazprom"));
        result.add(new Share("LUK", "PAO Lukoil"));
        result.add(new Share("ROSN", "NK Rosneft"));
        result.add(new Share("URKA", "PAO Ural kali"));
        result.add(new Share("ALROS", "OOO Alros"));

        return Collections.unmodifiableList(result);
    }

    public static List<Share> getShares(){
        return shares;
    }

    public static MarketSnapshot getMarketSnapshot(Share share){
        List<Tick> result = new ArrayList<>();
        result.add(new Tick(share.getTicker(), 98.4, TickType.BID, 100L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 99.8, TickType.ASK, 200L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 100.1, TickType.ASK, 100L,  System.currentTimeMillis()));

        result.add(new Tick(share.getTicker(), 98.4, TickType.BID, 100L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 99.8, TickType.ASK, 200L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 100.1, TickType.ASK, 100L,  System.currentTimeMillis()));

        result.add(new Tick(share.getTicker(), 98.4, TickType.BID, 100L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 99.8, TickType.ASK, 200L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 100.1, TickType.ASK, 100L,  System.currentTimeMillis()));

        result.add(new Tick(share.getTicker(), 98.4, TickType.BID, 100L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 99.8, TickType.ASK, 200L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 100.1, TickType.ASK, 100L,  System.currentTimeMillis()));

        result.add(new Tick(share.getTicker(), 98.4, TickType.BID, 100L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 99.8, TickType.ASK, 200L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 100.1, TickType.ASK, 100L,  System.currentTimeMillis()));

        result.add(new Tick(share.getTicker(), 98.4, TickType.BID, 100L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 99.8, TickType.ASK, 200L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 100.1, TickType.ASK, 100L,  System.currentTimeMillis()));

        result.add(new Tick(share.getTicker(), 98.4, TickType.BID, 100L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 99.8, TickType.ASK, 200L,  System.currentTimeMillis()));
        result.add(new Tick(share.getTicker(), 100.1, TickType.ASK, 100L,  System.currentTimeMillis()));
        return new MarketSnapshot(result);
    }
}
