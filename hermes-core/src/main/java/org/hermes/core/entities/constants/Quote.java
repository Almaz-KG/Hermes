package org.hermes.core.entities.constants;

import org.hermes.core.exceptions.QuoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Almaz
 * Date: 15.10.2015
 */
public enum Quote {

    QUOTE_GROUPS("Quote group", null),
    UNDEFINED("Undefined quote group (for testing and other development features)", QUOTE_GROUPS),
    ETF("ETF Group", QUOTE_GROUPS),
        DIA ("SPDR Dow Jones Industrial Average ETF Trust", ETF),
        FAS("Direxon Finl Bull 3X", ETF),
        FXI("iShares FTSE/Xinhua China 25 Index", ETF),
        GLD("SPDR Gold Shares", ETF),
        QQQ("CDF Nasdaq-100 Index Tracking Stock", ETF),
        SDS("UltraShort SP500 ProShares", ETF),
        SMH("HLDRS Semiconductor", ETF),
        SPY("CDF Standard Poor's Depositary Receipts", ETF),
        SSO("Ultra SP500 ProShares", ETF),
        TLT("iShares Barclays 20+ yr Treasury Bond", ETF),
        XLI("SPDR Industrial", ETF),
    Forex("Forex Group", QUOTE_GROUPS),
        AUDCAD("Australian dollar vs Canadian dollar", Forex),
        AUDUSD("Australian dollar vs USA dollar", Forex),
        EURAUD("Euro vs Australian dollar", Forex),
        EURCAD("Euro vs Canadian dollar", Forex),
        EURCHF("Euro vs Swiss frank", Forex),
        EURGBP("Euro vs Great Britain pound", Forex),
        EURJPY("Euro vs Japanese yen", Forex),
        EURRUB("Euro vs Russian ruble", Forex),
        EURUSD("Euro vs USA dollar", Forex),
        GBPAUD("Great Britain pound vs Australian dollar", Forex),
        GBPCAD("Great Britain pound vs Canadian dollar", Forex),
        GBPCHF("Great Britain pound vs Swiss frank", Forex),
        GBPJPY("Great Britain pound vs Japanese yen", Forex),
        GBPUSD("Great Britain pound vs USA dollar", Forex),
        NZDCAD("New Zealand dollar vs Canadian dollar", Forex),
        NZDJPY("New Zealand dollar vs Japanese yen", Forex),
        NZDUSD("New Zealand dollar vs USA dollar", Forex),
        USDCAD("USA dollar vs Canadian dollar", Forex),
        USDCHF("USA dollar vs Swiss frank", Forex),
        USDJPY("USA dollar vs Japanese yen", Forex),
        USDRUB("USA dollar vs Russian ruble", Forex),
    Futures("Futures Group", QUOTE_GROUPS),
        BRN("Brend oil", Futures),
        CF("CAC 40 Index", Futures),
        DJI("", Futures),
        DXY("Dollar Index Future", Futures),
        EN("ICE WTI Crude Oil", Futures),
        ES("E-mini SP500", Futures),
        GC("Gold Future", Futures),
        GX("Dax Index", Futures),
        NQ("E-mini Nasdaq 100", Futures),
        NQ100("Nasdaq 100 Index", Futures),
        NQCOMP("Nasdaq Composite Index", Futures),
        NX("Nikkei 225 Index", Futures),
        S("Soybean Future", Futures),
        SI("Silver Future", Futures),
        SP500("SP500 Index", Futures),
        W("Wheat Future", Futures),
        Z("FTSE 100 Index", Futures),
    RU_Stocks("Russian stocks", QUOTE_GROUPS),
        GAZP("OAO Gazprom", RU_Stocks),
        GMKN("OAO Norilsky Nickel", RU_Stocks),
        LKOH("OAO Lukoil", RU_Stocks),
        ROSN("OAO Rosneft", RU_Stocks),
        SBER("OAO Sberbank", RU_Stocks),
        SBERP("OAO Sberbank Preferred stocks", RU_Stocks),
        SIBN("OAO Gazprom Neft", RU_Stocks),
        SNGS("OAO SurgutNefteGaz", RU_Stocks),
        VTBR("OAO Bank VTB", RU_Stocks),
    US_Stocks("USA stocks", QUOTE_GROUPS),
        AA("Alcoa Inc", US_Stocks),
        AAPL("Apple Corp", US_Stocks),
        AIG("American International Group", US_Stocks),
        AXP("American Express Company", US_Stocks),
        BA("Boeing Company", US_Stocks),
        BAC("Bank of America", US_Stocks),
        C("Citigroup Inc", US_Stocks),
        CAT("Caterpillar Inc", US_Stocks),
        CSCO("Cisco Company", US_Stocks),
        CVX("Chevron", US_Stocks),
        DD("DuPont, E.I. du Pont de Nemours and Company", US_Stocks),
        DIS("Walt Disney Company", US_Stocks),
        EBAY("Ebay Company", US_Stocks),
        FCX("Freeport-McMoRan Copper Gold Inc", US_Stocks),
        FDX("FedEx Corporation", US_Stocks),
        GE("General Electric Corporation", US_Stocks),
        GOOG("Google Corp", US_Stocks),
        GS("The Goldman Sachs Group", US_Stocks),
        HAL("Halliburton Company", US_Stocks),
        HD("Home Depot Inc", US_Stocks),
        HON("Honeywell International Inc", US_Stocks),
        HPQ("Hewlett-Packard Company", US_Stocks),
        IBM("IBM Corporation", US_Stocks),
        INTC("Intel Corporation", US_Stocks),
        IP("International Paper Company", US_Stocks),
        JNJ("Johnson & Johnson", US_Stocks),
        JPM("JPMorgan Chase & Co", US_Stocks),
        KO("Coca-Cola Company", US_Stocks),
        KHC("The Kraft Heinz Company", US_Stocks),
        MCD("McDonald's Corporation", US_Stocks),
        MMM("3M Company", US_Stocks),
        MO("Altria Group Inc", US_Stocks),
        MRK("Merck & Co Inc", US_Stocks),
        MSFT("Microsoft Corporation", US_Stocks),
        ORCL("Oracle Corporation", US_Stocks),
        PEP("PepsiCo Inc", US_Stocks),
        PFE("Pfizer Inc", US_Stocks),
        PG("Procter & Gamble Company", US_Stocks),
        QCOM("QUALCOMM Inc", US_Stocks),
        SBUX("Starbucks Corporation", US_Stocks),
        T("AT&T Inc", US_Stocks),
        TRV("Travelers", US_Stocks),
        UPS("United Parcel Service, Inc", US_Stocks),
        UTX("United Technologies Corporation", US_Stocks),
        V("Visa Inc", US_Stocks),
        VZ("Verizon Communications Inc", US_Stocks),
        WFC("Wells Fargo Company", US_Stocks),
        WMT("Wal-Mart Stores Inc", US_Stocks),
        XOM("ExxonMobil Corporation", US_Stocks),
        YHOO("AA", US_Stocks),
    Metals("Metals Group", QUOTE_GROUPS),
        XAUUSD ("Gold (Spot)", Metals),
        XAGUSD ("Silver (Spot)", Metals),
        XPDUSD ("Pallidum", Metals),
        XPTUSD ("Platinum", Metals);

    private String quoteName;
    private Quote group;

    Quote(String quoteName, Quote group) {
        this.quoteName = quoteName;
        this.group = group;
    }

    public String getQuoteName() {
        return quoteName;
    }

    public Quote getGroup() {
        return group;
    }

    public static Quote[] getQuotesGroups(){
        List<Quote> result = new ArrayList<>();
        for (Quote quote : values()) {
            if(quote.getGroup() == QUOTE_GROUPS)
                result.add(quote);
        }

        Quote[] qu = new Quote[result.size()];
        return result.toArray(qu);
    }

    public static Quote[] getQuotesByGroup(Quote group) throws QuoteException {
        Quote[] quoteGroups = getQuotesGroups();
        for (Quote quoteGroup : quoteGroups) {
            if(quoteGroup == group){
                List<Quote> result = new ArrayList<>();
                for (Quote quote : Quote.values()) {
                    if(quote.getGroup() == group)
                        result.add(quote);
                }

                Quote[] r = new Quote[result.size()];
                return result.toArray(r);
            }
        }

        throw new QuoteException("Unknown quote", group);
    }

    public static String getTickerByName(String quoteName) throws QuoteException {
        for (Quote quote : values()) {
            if(quote.quoteName.equals(quoteName))
                return quote.toString();
        }

        throw new QuoteException("Unknown quote for: " + quoteName);
    }

    public static Quote getQuoteByTicker(String ticker) throws QuoteException {
        for (Quote quote : values()) {
            if(quote.toString().equals(ticker))
                return quote;
        }
        throw new QuoteException("Unknown ticker " + ticker);
    }
}
