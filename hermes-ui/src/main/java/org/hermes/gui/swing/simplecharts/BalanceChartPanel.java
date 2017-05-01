package org.hermes.gui.swing.simplecharts;

/**
 * Created by Almaz on 01.10.2015.
 */
public class BalanceChartPanel extends SimpleChartPanel {
    public BalanceChartPanel(double[] data) {
        super(data);
        this.DEFAULT_ABSCISSA_LABEL = "Trades";
    }
}
