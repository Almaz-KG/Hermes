package org.hermes.core.entities.price;


public enum TimeFrame {
    Minute1("1 minute", 1),
    Minute5("5 minutes", 5),
    Minute15("15 minutes", 15),
    Minute30("30 minutes", 30),
    Hour1("1 hour", 60),
    Hour4("4 hour", 4 * 60),
    Day1("1 day", 24 * 60),
    Day3("3 days", 3 * 24 * 60),
    Week1("1 week", 7 * 24 * 60);

    public final String name;
    public final int period;

    TimeFrame(String name, int period) {
        this.name = name;
        this.period = period;
    }

    public static TimeFrame getTimeFrame(int minutes) {
        for (TimeFrame timeFrame : values()) {
            if (timeFrame.period == (minutes * 60)) {
                return timeFrame;
            }
        }
        return null;
    }
}
