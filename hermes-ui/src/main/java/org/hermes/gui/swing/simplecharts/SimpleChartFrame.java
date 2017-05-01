package org.hermes.gui.swing.simplecharts;

import javax.swing.*;

/**
 * Created by Almaz on 01.10.2015.
 */
public class SimpleChartFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 640;
    public static final int DEFAULT_HEIGHT = 480;
    public static final String DEFAULT_TITLE = "Simple chart frame";

    public SimpleChartFrame(JPanel panel) throws Exception {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle(DEFAULT_TITLE);
        setLocationRelativeTo(null);

        this.add(panel);
    }
}
