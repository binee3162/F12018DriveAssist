package gui.realtime.map;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import static org.apache.commons.lang3.math.NumberUtils.toDouble;

public class DashBoard extends JComponent{
    private static final int VALUE_FONT_SIZE = 18;
    private double from = 0;
    private double to = 400;
    private double major = 40;
    private double minor = 4;
    private String value = "";
    private String unit = "";
    public DashBoard(){
        super();
        this.setBackground(Color.black);
        this.setForeground(Color.WHITE);
        this.setPreferredSize(new Dimension(180,180));
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void paintComponent(Graphics g){
        double w = this.getWidth();
        double h = this.getHeight();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.getBackground());
        g2.fillRect(0, 0, (int) w, (int) h);
        g2.setColor(this.getForeground());
        g2.setStroke(new BasicStroke(1));
        int fontSizeMajor = 13;
        g2.setFont(new Font(Font.SERIF, Font.PLAIN, fontSizeMajor));
        if (major <= 0) {
            major = to - from;
        }
        double angle=240;
        double angleStart= (180 + (angle - 180) / 2) / 180 * Math.PI;
        double r = Math.min(w / 2, h / 2);
        double voff =r;
        double dunit = (angle / 180 * Math.PI) / (to - from);
        if (to>from){

            for (int i = 0; i <= (to - from) / major; i++) {


                g2.draw(new Line2D.Double(Math.cos(angleStart - i * major * dunit) * r + w / 2, h - voff - Math.sin(angleStart - i * major * dunit) * r,
                        Math.cos(angleStart - i * major * dunit) * r * 0.75 + w / 2, h - voff - Math.sin(angleStart - i * major * dunit) * r * 0.75));
                if(i>=(to - from) / major-1)  g2.setColor(Color.RED);
                if (minor > 0 && i < (to - from) / major) {
                    for (int j = 1; j < major / minor; j++) {
                        if (i * major + j * minor < to - from) {
                            if(j==major / (2*minor)) {
                                g2.draw(new Line2D.Double(Math.cos(angleStart - (i * major + j * minor) * dunit) * r + w / 2, h - voff - Math.sin(angleStart - (i * major + j * minor) * dunit) * r,
                                        Math.cos(angleStart - (i * major + j * minor) * dunit) * r * 0.825 + w / 2, h - voff - Math.sin(angleStart - (i * major + j * minor) * dunit) * r * 0.825));
                            }else{
                                g2.draw(new Line2D.Double(Math.cos(angleStart - (i * major + j * minor) * dunit) * r + w / 2, h - voff - Math.sin(angleStart - (i * major + j * minor) * dunit) * r,
                                        Math.cos(angleStart - (i * major + j * minor) * dunit) * r * 0.875 + w / 2, h - voff - Math.sin(angleStart - (i * major + j * minor) * dunit) * r * 0.875));
                            }
                        }
                    }
                }
            }
            g2.setColor(this.getForeground());
            if (value.length() > 0) {
                double val = toDouble(value);
                GeneralPath p = new GeneralPath();
                p.moveTo(Math.cos(angleStart - (val - from) * dunit) * r * 0.875 + w / 2, h - voff - Math.sin(angleStart - (val - from) * dunit) * r * 0.875);
                p.lineTo(Math.cos(angleStart - (val - from) * dunit + Math.PI * 0.5) * 2 + w / 2, h - voff - Math.sin(angleStart - (val - from) * dunit + Math.PI * 0.5) * 2);
                p.lineTo(Math.cos(angleStart - (val - from) * dunit - Math.PI * 0.5) * 2 + w / 2, h - voff - Math.sin(angleStart - (val - from) * dunit - Math.PI * 0.5) * 2);
                p.closePath();
                g2.setColor(Color.RED);
                g2.fill(p);   //shape of pointer
                g2.setColor(this.getForeground());
            }
        }
        if (to > from) {
            if (major > 0) {
                int xoff = 0;
                int yoff = 0;
                double strAngle;
                for (int i =  0; i <= (to - from) / major; i++) {
                    String str;
                    str = format(from + i * major);
                    strAngle = (angleStart - i * major * dunit + Math.PI * 2) % (Math.PI * 2);
                    xoff = 0;
                    yoff = 0;
                    if (strAngle >= 0 && strAngle < Math.PI * 0.25) {
                        xoff = (int) -getStrBounds(g2, str).getWidth();
                        yoff = fontSizeMajor / 2;
                    } else if (near(strAngle, Math.PI * 0.5)) {
                        xoff = (int) -getStrBounds(g2, str).getWidth() / 2;
                        yoff = fontSizeMajor;
                    } else if (strAngle >= Math.PI * 0.25 && strAngle < Math.PI * 0.5) {
                        xoff = (int) -getStrBounds(g2, str).getWidth();
                        yoff = fontSizeMajor;
                    } else if (strAngle >= Math.PI * 0.5 && strAngle < Math.PI * 0.75) {
                        yoff = fontSizeMajor;
                    } else if (strAngle >= Math.PI * 0.75 && strAngle < Math.PI) {
                        yoff = fontSizeMajor / 2;
                    } else if (near(strAngle, Math.PI)) {
                        xoff = 1;
                        yoff = fontSizeMajor / 2;
                    } else if (strAngle >= Math.PI && strAngle < Math.PI * 1.25) {
                        yoff = fontSizeMajor / 4;
                    } else if (near(strAngle, Math.PI * 1.5)) {
                        xoff = (int) -getStrBounds(g2, str).getWidth() / 2;
                    } else if (strAngle >= Math.PI * 1.5 && strAngle < Math.PI * 2) {
                        xoff = (int) -getStrBounds(g2, str).getWidth();
                    }
                    g2.drawString(str, (int) (Math.cos(strAngle) * r * 0.75 + w / 2) + xoff, (int) (h - voff - Math.sin(strAngle) * r * 0.75) + yoff);
                }

                if (value.length() > 0) {
                    voff = r -2* fontSizeMajor ;
                    drawValue(g2, value , unit, (int) (w / 2 - getStrBounds(g2, value+unit).getWidth()/2 ), (int) (h - voff));
                }
            }
        }


    }

    private void drawValue(Graphics2D g2, String value,String unit, int x, int y) {
        g2.setFont(new Font(Font.SERIF, Font.BOLD, VALUE_FONT_SIZE));
        g2.drawString(value, x, y);

        int dx = (int)getStrBounds(g2,value).getWidth();
        g2.setFont(new Font(Font.SERIF, Font.BOLD, VALUE_FONT_SIZE-4));
        g2.drawString(unit,x+dx,y);
    }
    private String format(double d) {
        if ((int) d == d) {
            return String.valueOf((int) d);
        } else {
            return String.valueOf(d);
        }
    }
    private static Rectangle2D getStrBounds(Graphics2D g, String str) {
        Font f = g.getFont();
        Rectangle2D rect = f.getStringBounds(str, g.getFontRenderContext());
        if (rect.getHeight() < f.getSize()) {
            rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), f.getSize() + 1);
        }
        return rect;
    }
    private boolean near(double d1, double d2) {
        return Math.round(d1 * 1000000) == Math.round(d2 * 1000000);
    }

    public static void main(String[] args) {
        try {
            JFrame f = new JFrame("刻度盘测试");
            Container p = f.getContentPane();
            DashBoard tick;
            JPanel panel = new JPanel(new GridLayout(1, 1));
            tick = new DashBoard();
            tick.setValue("350");
            tick.setUnit("km/h");
            panel.add(tick);
            p.add(panel, BorderLayout.CENTER);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(640, 480);
            f.setVisible(true);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }













}
