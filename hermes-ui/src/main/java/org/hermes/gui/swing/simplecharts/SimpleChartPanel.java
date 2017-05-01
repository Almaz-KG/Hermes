package org.hermes.gui.swing.simplecharts;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;

/**
 * Created by Almaz on 01.10.2015.
 */
public class SimpleChartPanel extends JPanel {
    protected final int PADDING = 20;
    protected String DEFAULT_ORDINATE_LABEL = "Price";
    protected String DEFAULT_ABSCISSA_LABEL = "DATE";
    protected double[] data;
    protected double min = Double.MAX_VALUE;;
    protected double max = -Double.MAX_VALUE;;
    private int shift;

    public SimpleChartPanel(double[] data) {
        this.data = data;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        // Draw ordinate.
        g2.draw(new Line2D.Double(PADDING, PADDING, PADDING, h- PADDING));

        // Draw abcissa.
        g2.draw(new Line2D.Double(PADDING, h- PADDING, w- PADDING, h- PADDING));

        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();


        float sy = PADDING + ((h - 2* PADDING) - DEFAULT_ORDINATE_LABEL.length()*sh)/2 + lm.getAscent();
        for(int i = 0; i < DEFAULT_ORDINATE_LABEL.length(); i++) {
            String letter = String.valueOf(DEFAULT_ORDINATE_LABEL.charAt(i));
            float sw = (float)font.getStringBounds(letter, frc).getWidth();
            float sx = (PADDING - sw)/2;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }


        sy = h - PADDING + (PADDING - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(DEFAULT_ABSCISSA_LABEL, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(DEFAULT_ABSCISSA_LABEL, sx, sy);



        double xInc = (double)(w - 2 * PADDING)/(data.length-1);
        double scale = (double)(h - 2 * PADDING) / (getMax() - getMin() + 10) ;

        for (int i = 0; i < data.length; i++) {
            data[i] -= min / 2 ;
        }
        g2.setPaint(Color.RED.darker());
        g2.draw(new Line2D.Double(PADDING, h - PADDING - scale * data[0], w- PADDING, h - PADDING - scale * data[0]));

        g2.setPaint(Color.green.darker());
        for(int i = shift; i < data.length-1; i++) {
            double x1 = PADDING + i * xInc;
            double y1 = h - PADDING - scale * data[i];
            double x2 = PADDING + (i+1) * xInc;
            double y2 = h - PADDING - scale * data[i+1];
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }

    }

    protected double getMax() {
        if(max != -Double.MAX_VALUE)
            return max;

        for(int i = 0; i < data.length; i++) {
            if(data[i] > max)
                max = data[i];
        }
        return max;
    }
    protected double getMin() {
        if(min != Double.MAX_VALUE)
            return min;

        for(int i = 0; i < data.length; i++) {
            if(data[i] < min)
                min = data[i];
        }
        return min;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
}
