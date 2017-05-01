package org.hermes.gui.swing.simplecharts;

import javax.swing.*;

/**
 * Created by Almaz
 * Date: 15.10.2015
 */
public class Example {
    public static void main(String[] args) {
        try {
            double[] myDepositBalance = new double[]{
                100, 120, 140, 156, 140, 160, 150, 140,
                160, 150, 170, 140, 140, 110, 160, 180,
                190, 210, 230, 250, 200, 190, 120, 180,
                210, 190, 200, 200, 140, 160, 200, 250,
                290, 270, 250, 260, 270, 230, 290, 320,
                360, 340, 300, 320, 380, 430, 420, 450
            };
            SimpleChartPanel panel = new BalanceChartPanel(myDepositBalance);
            SimpleChartFrame frame = new SimpleChartFrame(panel);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
