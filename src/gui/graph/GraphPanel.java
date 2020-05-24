package gui.graph;


import database.DbMethod;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    //private boolean firstCall;
    private String title;
    private int typePanel;
    private int trackDistance;
    private int previousLapNumber;
    private Color[] colors;
    private int lapNumber;
    private ArrayList<GraphData> dataListPerLap;
    private Boolean useDB=false;
    private ArrayList<GraphData> dataListPerLapDB;
    private int ySize;
    private int xSize;

    public GraphPanel(String title, int typePanel){
        super();
        this.ySize = getHeight();
        this.xSize = getWidth();
        this.title = title;
        this.typePanel = typePanel;
        this.previousLapNumber = 0;
        this.lapNumber = 0;
        this.dataListPerLap = new ArrayList<>();
        this.colors = new Color[]{Color.GREEN, Color.GREEN, Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA,
                Color.YELLOW, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY};
    }


    public void setTrackDistance(int trackDistance){
        this.trackDistance = trackDistance;
    }
    public void setLapNumber(int lapNumber){
        if(previousLapNumber == 0){ //first lap only once
            this.previousLapNumber = lapNumber;
            dataListPerLap.add(new GraphData(lapNumber));
        }
        this.lapNumber = lapNumber;
        if(lapNumber > previousLapNumber){
            previousLapNumber++;
            dataListPerLap.add(new GraphData(lapNumber));
        }
    }

    public void setCurrentDistance(int currentDistance){
        if(dataListPerLap == null){
            setLapNumber(lapNumber);
        }
        if (lapNumber > 0) {
            dataListPerLap.get(lapNumber-1).setDistance(currentDistance);
        }
    }
    public void setUseDB(Boolean useDB){
        this.useDB=useDB;
        repaint();
    }

    public Boolean getUseDB() {
        return useDB;
    }

    public void setDataListPerLapDB(ArrayList<GraphData> dataListPerLapDB){
        this.dataListPerLapDB=dataListPerLapDB;
    }
    public void updateValue(int value){
        if(dataListPerLap == null){
            setLapNumber(lapNumber);
        }
        if(lapNumber > 0){
            dataListPerLap.get(lapNumber-1).setValue(value);
            repaint();
        }
    }


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Color.black);
        drawLayout(g);
        if(useDB)
            drawGraph(g,dataListPerLapDB);
        else
            drawGraph(g,dataListPerLap);
    }

//    private void paint(Graphics g, ArrayList<Integer> values, ArrayList<Integer> distances){
//        int distance1 = distances.get(i-1);
//        int distance2 = distances.get(i);
//        int value1 = values.get(i-1);
//        int value2 = values.get(i);
//        int x1 = 55 + Math.round(distance1 * (460 - 55) / (trackDistance-5));
//        int y1;
//        int x2 = 55 + Math.round(distance2 * (460 - 55) / (trackDistance-5));
//        int y2;
//        if (speedPanel) {
//            //calculate the pixels for speedGraph
//            y1 = 145 + Math.round(value1 * (30 - 145) / 320);
//            y2 = 145 + Math.round(value2 * (30 - 145) / 320);
//        } else {
//            //calculate the pixels for the throttleGraph
//            y1 = 145 + Math.round(value1 * (30 - 145) / 100);
//            y2 = 145 + Math.round(value2 * (30 - 145) / 100);
//        }
//        g.drawLine(x1, y1, x2, y2);
//        /*
//            55 is location of start on x-axis
//            460 is location of finish on x-axis
//            145 is location of 0 on y-axis
//            30 is location of maximum on y-axis
//            320 is maximum speed: need to change this!
//             */
//    }
    private void drawGraph(Graphics g,ArrayList<GraphData> graphData) {
        if(graphData.size()!=0) {
            for (GraphData data : graphData) {
                ArrayList<Integer> values = data.getValues();
                ArrayList<Integer> distances = data.getDistances();
                g.setColor(colors[data.getLapNumber()]);
                for (int i = 1; i < data.getValues().size() && i < data.getDistances().size(); i++) {
                    int distance1 = distances.get(i - 1);
                    if (distance1 > 0) {
                        int distance2 = distances.get(i);
                        int value1 = values.get(i - 1);
                        int value2 = values.get(i);
                        int x1 = 55 + Math.round(distance1 * (460 - 55) / (trackDistance - 5));
                        int y1;
                        int x2 = 55 + Math.round(distance2 * (460 - 55) / (trackDistance - 5));
                        int y2;
                        if (typePanel == 1) {
                            //calculate the pixels for speedGraph
                            y1 = 145 + Math.round(value1 * (30 - 145) / 320);
                            y2 = 145 + Math.round(value2 * (30 - 145) / 320);
                        } else if (typePanel == 2) {
                            y1 = 145 + Math.round((value1 + 100) * (30 - 145) / 200);
                            y2 = 145 + Math.round((value2 + 100) * (30 - 145) / 200);
                        } else {
                            //calculate the pixels for the throttleGraph
                            y1 = 145 + Math.round(value1 * (30 - 145) / 100);
                            y2 = 145 + Math.round(value2 * (30 - 145) / 100);
                        }
                        g.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }
    }










        /*for(HashMap<Integer,Integer> graph: multipleGraph) {
            int graphLength = graph.size();
            g.setColor(Colors[lapNumber]);
            for (int i = 0; i < graphLength; i++) {
                // for every value in ArrayList, display it on the screen
                if (graphLength - i > 1) {//to avoid outOfBoundsError
                    //int value1 = multipleGraph.get(j).get(i);
                    //int value2 = multipleGraph.get(j).get(i + 1);
                    long distance1 = distanceGraph.get(i);
                    long distance2 = distanceGraph.get(i+1);
                    int value1 = graph.get(distance1);
                    int value2 = graph.get(distance2);
                    //long distance1 = multipleDistance.get(j).get(i);
                    //long distance2 = multipleDistance.get(j).get(i + 1);

                    if(distance1 > trackDistance - 5){
                        i = graphLength;
                    }
                    else{
                        int x1 = 55 + Math.round((distance1) * (460 - 55) / (trackDistance-5));
                        int y1;
                        int x2 = 55 + Math.round(distance2 * (460 - 55) / (trackDistance-5));
                        int y2;
                        if (speedPanel) {
                            //calculate the pixels for speedGraph
                            y1 = 145 + Math.round(value1 * (30 - 145) / 320);
                            y2 = 145 + Math.round(value2 * (30 - 145) / 320);
                        } else {
                            //calculate the pixels for the throttleGraph
                            y1 = 145 + Math.round(value1 * (30 - 145) / 100);
                            y2 = 145 + Math.round(value2 * (30 - 145) / 100);
                        }


                        g.drawLine(x1, y1, x2, y2);
                /*
            55 is location of start on x-axis
            460 is location of finish on x-axis
            152 is location of 0 on y-axis
            30 is location of maximum on y-axis
            320 is maximum speed: need to change this!
             */
                        //todo: make maximum speed variable
                  //  }
               // }
           // }
        //}



    private void drawLayout(Graphics g){
        g.setColor(Color.WHITE);
        //y-axis
        g.drawLine(55, 10, 55, 152);
        g.drawLine(55, 10, 50, 15);
        g.drawLine(55, 10, 60, 15);
        g.drawString(title, 5, 20);
        g.drawLine(52, 30, 58, 30);
        if(typePanel==2){
            g.drawLine(52, 145, 58, 145);



            g.drawString("100%", 16, 35); // on steerPanel
            g.drawString("-100%", 16, 150); // on steerPanel
            //x-axis
            g.drawLine(45, 88, 520, 88);
            g.drawLine(520, 88, 515, 83);
            g.drawLine(520, 88, 515, 93);
            g.drawString("Distance", 480, 108);
            g.setColor(Color.BLUE);
            g.drawString("Start", 40, 108);
            g.drawString("Finish", 440, 108);
            //draw indicator of Finish
            g.drawLine(460, 86, 460, 91);
        }else {




            if (typePanel == 1) g.drawString("320", 23, 35); // on speedPanel
            else g.drawString("100%", 16, 35); // on throttlePanel

            //x-axis
            g.drawLine(45, 145, 520, 145);
            g.drawLine(520, 145, 515, 140);
            g.drawLine(520, 145, 515, 150);
            g.drawString("Distance", 480, 165);
            g.setColor(Color.BLUE);
            g.drawString("Start", 40, 165);
            g.drawString("Finish", 440, 165);
            //draw indicator of Finish
            g.drawLine(460, 143, 460, 148);
        }
    }
}
