package org.hermes.gui.swing.jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Created by Almaz
 * Date: 07.10.2015 18:49
 * 
 */
public class FirstExample{

    public static void main(String[] args) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("First", 23.76);
        dataset.setValue("Second", 24.08);
        dataset.setValue("Third", 17.13);
        dataset.setValue("Fourth", 12.89);
        dataset.setValue("Fifth", 35.70);

        JFreeChart chart = ChartFactory.createPieChart(
                "Simple pie chart",
                dataset,
                true,
                true,
                false
        );
        ChartFrame frame = new ChartFrame("Pie chart", chart);
        frame.pack();
        frame.setVisible(true);
    }
}
