package gui.realtime;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

class ProgressCircleUI extends BasicProgressBarUI {
    private double width=5;
    public void setWidth(double width){
        this.width=width;
    }
    @Override public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        int v = Math.max(d.width, d.height);
        d.setSize(v, v);
        //d.setSize(50, 50);
        return d;
    }
    @Override public void paint(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth  = progressBar.getWidth()  - b.right - b.left;
        int barRectHeight = progressBar.getHeight() - b.top - b.bottom;
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        // draw the cells
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(progressBar.getForeground());

        double degree = 360 * progressBar.getPercentComplete(); //angle
        double sz = Math.min(barRectWidth, barRectHeight);
        double or = sz * .5;
        double ir = or-width;
        double[] center={or+b.left,or+b.top};
//        double cx = b.left + barRectWidth  * .5;
//        double cy = b.top  + barRectHeight * .5;


       // Shape inner = new Ellipse2D.Double(cx - ir -27, cy - ir -27, ir * 3.5, ir * 3.5);
       // Shape outer = new Arc2D.Double(cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);
        Shape inner = new Ellipse2D.Double(center[0]-or+5, center[1]-or+5, 2*ir , 2*ir );
        Shape outer = new Arc2D.Double(center[0]-or, center[1]-or, 2*or, 2*or, 90 - degree, degree, Arc2D.PIE);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        g2.fill(area);


        g2.dispose();

        // Deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, (int)center[0]-(int)(0.5*barRectWidth),(int)center[1]-(int)(0.5*barRectHeight), barRectWidth, barRectHeight, 0, b);
        }
    }
}