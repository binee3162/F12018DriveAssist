package gui.realtime.map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapPanel extends JPanel {
    private JLabel label;

    public  void setSize(int size) {
        this.size = size;
    }
    int trackID=14;

    public  void setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
    }
    private int[] testMax={882,1079,618,613,601,510,1477,991,788,620,1072,1092,734,1002,843,1046,706,738,959,1030,1189,613,991,1046,999};
    private  int size =300;
    private BufferedImage image;
    private  int circleWidth=15;
    public MapPanel()  {
        super();
        this.setBackground(new Color(0,0,0,0));
        this.setLayout(new GridLayout(1, 1));

        label = new JLabel();
        label.setSize(size, size);
        this.add(label);

        loadMap(trackID);
    }
    private void loadMap(int trackId) {
        try {
            //File ecgFile = new File("/home/pi/EE5/src/gui/realtime/map/tracks/"+trackId+".png");

            File ecgFile = new File("C:\\Users\\Song\\Desktop\\Lecture\\EE5\\formula4\\src\\gui\\realtime\\map\\tracks\\" + trackId + ".png");
            image = ImageIO.read(ecgFile);
            //todo:文件夹标号多个
        }catch(Exception e){
            System.out.println("File 404");
        }
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
        loadMap(trackID);
    }

    private  BufferedImage processImage(final BufferedImage bufferedimage,
                                        final int degree, final int x, final int y) {
        int w = bufferedimage.getWidth();// 得到图片宽度。
        int h = bufferedimage.getHeight();// 得到图片高度。
        int type = bufferedimage.getColorModel().getTransparency();// 得到图片透明度。
        BufferedImage img;// 空的图片。
        Graphics2D graphics2d;// 空的画笔。
        //BufferedImage tempimg=bufferedimage.getSubimage(-x*800/858+800-(size -2*circleWidth)/2,y*800/858+800-(size -2*circleWidth)/2, size -2*circleWidth, size -2*circleWidth);
        BufferedImage tempimg=bufferedimage.getSubimage(-x*800/testMax[trackID]+800-(size -2*circleWidth)/2,y*800/testMax[trackID]+800-(size -2*circleWidth)/2, size -2*circleWidth, size -2*circleWidth);
        (graphics2d = (img = new BufferedImage(size, size, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        Ellipse2D.Double shape = new Ellipse2D.Double(circleWidth, circleWidth, size -2*circleWidth, size -2*circleWidth);
        graphics2d.setClip(shape);  //裁剪圆形

        graphics2d.rotate(Math.toRadians(degree), size /2, size /2);

        graphics2d.drawImage(tempimg,circleWidth, circleWidth,null);
        graphics2d.setColor(Color.ORANGE);
        Area area=new Area(new Ellipse2D.Double(size/2-5,size/2-5,10,10));
        graphics2d.fill(area);
        //graphics2d.drawOval(size/2-5,size/2-5,10,10);
        graphics2d.dispose();
        return img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

            drawMap(g);

    }
    private void drawMap(Graphics g) {
        Graphics2D g2d=(Graphics2D) g.create();
        g2d.setColor(Color.RED);
        BasicStroke bs = new BasicStroke(2);
        g2d.setStroke(bs);
            g2d.drawOval(0,0, size, size);
        Shape inner   =new Ellipse2D.Double(circleWidth, circleWidth, size -2*circleWidth, size -2*circleWidth);
        Shape outer   =new Ellipse2D.Double(0,0 , size, size);
        Shape labelArea = new Rectangle2D.Double(0,0, size +2, size +2);

        label.setSize(size +2, size +2);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        g2d.fill(area);
        area= new Area(labelArea);
        area.subtract(new Area(outer));
        g2d.setColor(Color.BLACK);
        g2d.fill(area);
        g2d.fill(new Area(inner));

    };

    public void setPosition(int degree, int x, int y) {
        BufferedImage ecgImage= processImage(image, degree,x,y);
        label.setIcon(new ImageIcon(ecgImage));
        this.repaint();

    }
}
