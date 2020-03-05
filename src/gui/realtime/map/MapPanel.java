package gui.realtime.map;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MapPanel extends JPanel {
    private int trackId;
    private ArrayList<int[]> trackCoords = new ArrayList<>();
    public MapPanel(int trackId){
        super();
        this.trackId=trackId;
        this.setSize(25,25);
        this.setBackground(new Color(0,0,0,0));

        readTrackFile(trackId);
    }
    //paint when creating panel, use updateUI to fresh content of panel.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
    }
    private void drawMap(Graphics g){
  //todo: draw map not always in full scale
        int[] first=null;
        for(int[] coord: trackCoords) {
            if(first==null)
                first=coord;
            else {
                //System.out.println("z: " + coord[0] + " x: " + coord[1]);
                Graphics2D g2d=(Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.RED);
                BasicStroke bs=new BasicStroke(2);
                g2d.setStroke(bs);
                g2d.drawLine(first[1]/8+100,first[0]/8+100,coord[1]/8+100,coord[0]/8+100);
                first=coord;
            }
        }
    }


    public void readTrackFile(int trackId) {
        //read file and store in List
        //String fileName = "./tracks/" + String.valueOf(trackId) + ".track";
        String fileName="C:\\Users\\Song\\Desktop\\Lecture\\EE5\\formula4\\src\\gui\\realtime\\map\\tracks\\"+trackId+".track";
        try {
            Scanner sc = new Scanner(new File(fileName));
            //first read data that is not important
            for(int i = 0; i<7;i++) sc.next();

            while (sc.hasNext()) {
                String fromFile = sc.next();
                String[] values = fromFile.split(",");

                int[] coord = new int[2];
                coord[0] = (int)Float.parseFloat(values[1]); //coord[0] = zPos
                coord[1] = (int)Float.parseFloat(values[2]); //coord[1] = xPos
                trackCoords.add(coord);
            }
            sc.close();
        }

        catch (FileNotFoundException fnfe)
        {
            System.out.println("File " + fileName + " not found");
        }
    }
}
