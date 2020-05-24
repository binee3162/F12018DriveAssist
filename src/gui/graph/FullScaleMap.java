package gui.graph;

import database.DbMethod;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FullScaleMap extends JPanel {

    private int[] testMax={882,1079,618,613,601,510,1477,991,788,620,1072,1092,734,1002,843,1046,706,738,959,1030,1189,613,991,1046,999};
    private Color[] colors = new Color[]{ Color.GREEN, Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA,
            Color.YELLOW, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY};
    private BufferedImage image;
    private JLabel label;
    private  int size =800;
    private int trackID=-1;
    private String raceID;
    private int lap=1;

    private ArrayList<ArrayList<float[]>> dataFromDB;

    public FullScaleMap() {
        super();
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(1, 1));
        label = new JLabel();
        label.setSize(size, size);
        label.setBackground(Color.black);
        this.add(label);
        this.trackID=trackID;

    }

    public void setLap(int lap) {
        this.lap = lap;
        redraw(lap);
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
        loadTrackData();
        loadMap(trackID);
        redraw(0);
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
    private void loadTrackData(){
        DbMethod db=new DbMethod();
        db.connectDb();
        String sql="SELECT X,Z,lapNumber from ee5.lapdata where raceID = "+raceID+";";
        ResultSet resultSet = db.executeSql(sql);
        dataFromDB=new ArrayList<>();
        try{
            int currentLap=0;
            ArrayList<float[]> dataPerLap=new ArrayList<>();
            while(resultSet.next()){
                float x=resultSet.getFloat(1);
                float y=resultSet.getFloat(2);
                int lapNumber=resultSet.getInt(3);
                if(currentLap!=lapNumber) {

                    if (currentLap != 0) {
                        dataFromDB.add(currentLap-1,dataPerLap);
                    }
                    currentLap++;
                    dataPerLap=new ArrayList<>();
                }

                float[] temp={x,y};
                dataPerLap.add(temp);

            }
            if(currentLap!=0){
                dataFromDB.add(currentLap-1,dataPerLap);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        sql="SELECT trackID from ee5.race where raceID="+raceID+" LIMIT 1;";
        resultSet = db.executeSql(sql);
            try{
            if(resultSet.next())
                trackID=resultSet.getInt(1);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void redraw(int lap){
        loadMap(trackID);
        BufferedImage ecgImage= processImage(image, lap);
        label.removeAll();
        label.setIcon(new ImageIcon(ecgImage));
        this.repaint();
    }

    private BufferedImage processImage(final BufferedImage bufferedimage,int lap){
        int size=800;
        BufferedImage img;// 空的图片。
        Graphics2D graphics2d;// 空的画笔。
        int w = bufferedimage.getWidth();// 得到图片宽度。
        int h = bufferedimage.getHeight();// 得到图片高度。
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage tempimg=bufferedimage.getSubimage(0,0,w,h);

                (graphics2d = (tempimg)
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //graphics2d.drawImage(tempimg,w, h,this);
        graphics2d.setStroke(new BasicStroke(2));
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(lap==0) {
            for (int i = 0; i < dataFromDB.size(); i++) {
                graphics2d.setColor(colors[i]);
                float[] first = null;
                for (float[] coord : dataFromDB.get(i)) {
                    if (first == null) {
                        first = coord;
                    } else {
                        graphics2d.drawLine((int) (-first[1] * size / testMax[trackID] + size), (int) (first[0] * size / testMax[trackID] + size), (int) (-coord[1] * size / testMax[trackID] + size), (int) (coord[0] * size / testMax[trackID] + size));
                        first = coord;

                    }

                }
            }
        }else{
                if(lap<=dataFromDB.size()+1) {
                    graphics2d.setColor(colors[lap-1]);

                    float[] first = null;
                    for (float[] coord : dataFromDB.get(lap-1)) {
                        if (first == null) {
                            first = coord;
                        } else {
                            graphics2d.drawLine((int) (-first[1] * size / testMax[trackID] + size), (int) (first[0] * size / testMax[trackID] + size), (int) (-coord[1] * size / testMax[trackID] + size), (int) (coord[0] * size / testMax[trackID] + size));
                            first = coord;

                        }

                    }
                }
            }


        graphics2d.dispose();
        (graphics2d = (img = new BufferedImage(size, size, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        graphics2d.drawImage(tempimg,0,0,size,size,null);
        graphics2d.dispose();
    return img ;


    }
    public void setTrackID(int trackID) {
        this.trackID = trackID;
        loadMap(trackID);
    }


}
