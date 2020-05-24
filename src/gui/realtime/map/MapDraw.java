package gui.realtime.map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MapDraw extends JPanel{

    private float testMax;
    private ArrayList<int[]> outerlimit = new ArrayList<>();
    private ArrayList<int[]> innerLimit = new ArrayList<>();
    private ArrayList<int[]> racingline = new ArrayList<>();
    private ArrayList<int[]> boundaries = new ArrayList<>();
    private ArrayList<ArrayList<int[]>> tracks=new ArrayList<>();
    private ArrayList<GeneralPath> path=new ArrayList<>();
    private GeneralPath outerPath=new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    private GeneralPath innerPath=new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    public MapDraw() throws IOException {
        this.setSize(25,25);
        this.setBackground(new Color(0,0,0,0));
       // readTrackFile("Abu_Dhabi");
        //extractMap(14);
    }

    public void readTrackFile(String track) {
        //read file and store in List
        //String fileName = "./tracks/" + String.valueOf(trackId) + ".track";
        //String fileName="C:\\Users\\Song\\Desktop\\Lecture\\EE5\\formula4\\src\\gui\\realtime\\map\\tracks\\"+trackId+".track";
        String loc="C:\\Users\\Song\\Desktop\\Lecture\\EE5\\formula4\\src\\gui\\realtime\\map\\tracks\\";
        String outer = loc+track+"_outerlimit.track";
        String inner = loc+track+"_innerlimit.track";
        String racing =loc+track+"_boundaries.track";
        String bound = loc+track+"_racingline.track";
        ArrayList<String> file=new ArrayList<>();
        file.add(0,outer);
        file.add(1,inner);
        file.add(2,bound);
        file.add(3,racing);
        tracks.add(0,outerlimit);
        tracks.add(1,innerLimit);
        tracks.add(2,boundaries);
        tracks.add(3,racingline);
        Scanner sc;
        try {
            for(int i=0;i<3;i++) {
                sc = new Scanner(new File(file.get(i)));
                for (int j = 0; j < 2; j++) sc.nextLine();
                while (sc.hasNext()) {
                    String fromFile = sc.next();
                    String[] values = fromFile.split(",");
                    int[] coord = new int[2];
                    coord[0] = (int) Float.parseFloat(values[1]); //coord[0] = zPos
                    coord[1] =(int) Float.parseFloat(values[2]); //coord[1] = xPos
                    testMax = Math.max(Math.max(Math.abs(coord[0]), Math.abs(coord[1])), testMax);

                    tracks.get(i).add(coord);
                }
                sc.close();
                System.out.println(testMax);

            }System.out.println("*");

        } catch (FileNotFoundException fnfe) {
            System.out.println("File "  + " not found");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
    }
    public void extractMap(int j) throws IOException {
        int size=800;
        path.add(0,outerPath);
        path.add(1,innerPath);
        BufferedImage bufferedImage=new BufferedImage(2*size,2*size,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d=bufferedImage.createGraphics();
        bufferedImage=g2d.getDeviceConfiguration().createCompatibleImage(2*size,2*size,Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = bufferedImage.createGraphics();


        for(int i=0;i<2;i++) {
            int[] first = null;
            for (int[] coord : tracks.get(i)) {
                coord[1]=-coord[1];
                if (first == null) {
                    first = coord;
                    path.get(i).moveTo(coord[1] * size / testMax + size, coord[0] * size / testMax + size);
                }
                else {
                    //System.out.println("z: " + coord[0] + " x: " + coord[1]);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(Color.white);
                    BasicStroke bs = new BasicStroke(3);
                    g2d.setStroke(bs);
                    //g2d.drawLine(first[1]/8+100,first[0]/8+100,coord[1]/8+100,coord[0]/8+100);
                    g2d.drawLine((int)(first[1]*size /testMax + size), (int)(first[0]*size /testMax + size), (int)(coord[1]*size /testMax + size), (int)(coord[0]*size /testMax + size));
                    first = coord;
                    path.get(i).lineTo(coord[1] * size / testMax + size, coord[0] * size / testMax + size);
                }
            }
            path.get(i).closePath();
        }
        Area area=new Area(path.get(0));
        area.subtract(new Area(path.get(1)));
        g2d.setColor(Color.DARK_GRAY);
        g2d.fill(area);
        g2d.dispose();

        File out=new File("C:\\Users\\Song\\Desktop\\track\\"+j+".png");
        ImageIO.write(bufferedImage,"png",out);
    }
    private void drawMap(Graphics g) {
        //todo: draw map not always in full scale
        int[] first = null;
        Graphics2D g2d = (Graphics2D) g.create();
        GeneralPath outer = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        for (int[] coord : outerlimit) {
            if (first == null) {
                first = coord;

                outer.moveTo(coord[1]/ 2 + 500,coord[0] / 2 + 500);

            }
            else {
                //System.out.println("z: " + coord[0] + " x: " + coord[1]);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.RED);
                BasicStroke bs = new BasicStroke(1);
                g2d.setStroke(bs);

                //g2d.drawLine(first[1]/8+100,first[0]/8+100,coord[1]/8+100,coord[0]/8+100);
                g2d.drawLine(first[1] / 2 + 500, first[0] / 2 + 500, coord[1] / 2+ 500, coord[0] / 2 + 500);
                first = coord;
                outer.lineTo(coord[1]/ 2 + 500,coord[0] / 2 + 500);


            }
        }
        outer.closePath();
        g2d.fill(outer);

        for (int[] coord : innerLimit) {
            if (first == null)
                first = coord;
            else {
                //System.out.println("z: " + coord[0] + " x: " + coord[1]);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.YELLOW);
                BasicStroke bs = new BasicStroke(1);
                g2d.setStroke(bs);
                //g2d.drawLine(first[1]/8+100,first[0]/8+100,coord[1]/8+100,coord[0]/8+100);
                g2d.drawLine(first[1] / 2 + 500, first[0] / 2+ 500, coord[1] / 2 + 500, coord[0] / 2 + 500);
                first = coord;
            }
        }
//        for (int[] coord : boundaries) {
//            if (first == null)
//                first = coord;
//            else {
//                //System.out.println("z: " + coord[0] + " x: " + coord[1]);
//                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2d.setColor(Color.BLUE);
//                BasicStroke bs = new BasicStroke(1);
//                g2d.setStroke(bs);
//                //g2d.drawLine(first[1]/8+100,first[0]/8+100,coord[1]/8+100,coord[0]/8+100);
//                g2d.drawLine(first[1] / 2 + 500, first[0] / 2 + 500, coord[1] / 2 + 500, coord[0] / 2 + 500);
//                first = coord;
//            }
//        }


//        for (int[] coord : racingline) {
//            if (first == null)
//                first = coord;
//            else {
//                //System.out.println("z: " + coord[0] + " x: " + coord[1]);
//                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2d.setColor(Color.white);
//                BasicStroke bs = new BasicStroke(1);
//                g2d.setStroke(bs);
//                //g2d.drawLine(first[1]/8+100,first[0]/8+100,coord[1]/8+100,coord[0]/8+100);
//                g2d.drawLine(first[1] / 2 + 500, first[0] / 2 + 500, coord[1] / 2 + 500, coord[0] / 2 + 500);
//                first = coord;
//            }
//        }

}

    public static void main(String[] args) throws IOException {
        MapDraw mapDraw=new MapDraw();
        mapDraw.readTrackFile("Australia");
        mapDraw.extractMap(0);

        mapDraw=new MapDraw();
        mapDraw.readTrackFile("France");
        mapDraw.extractMap(1);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("China");
        mapDraw.extractMap(2);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Bahrain");
        mapDraw.extractMap(3);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Spain");
        mapDraw.extractMap(4);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Monaco");
        mapDraw.extractMap(5);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Canada");
        mapDraw.extractMap(6);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Britain");
        mapDraw.extractMap(7);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Germany");
        mapDraw.extractMap(8);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Hungary");
        mapDraw.extractMap(9);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Belgium");
        mapDraw.extractMap(10);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Italy");
        mapDraw.extractMap(11);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Singapore");
        mapDraw.extractMap(12);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Japan");
        mapDraw.extractMap(13);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Abu_Dhabi");
        mapDraw.extractMap(14);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("USA");
        mapDraw.extractMap(15);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Brazil");
        mapDraw.extractMap(16);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Austria");
        mapDraw.extractMap(17);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Russia");
        mapDraw.extractMap(18);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Mexico");
        mapDraw.extractMap(19);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Azerbaijan");
        mapDraw.extractMap(20);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Bahrain_short");
        mapDraw.extractMap(21);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Britain_short");
        mapDraw.extractMap(22);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("USA_short");
        mapDraw.extractMap(23);
        mapDraw=new MapDraw();
        mapDraw.readTrackFile("Japan_short");
        mapDraw.extractMap(24);










    }
}