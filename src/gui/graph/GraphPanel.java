package gui.graph;

import org.apache.commons.lang3.ObjectUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphPanel extends JPanel {

    //private boolean firstCall;
    private String title;
    private Boolean speedPanel;
    private HashMap<Long, Integer> graph;
    private long trackDistance;
    private int previousLapNumber;
    private int lapNumber;
    private long currentDistance;
    private boolean newLap;
    private ArrayList<Long> distanceGraph;
    private Color[] Colors;
    private ArrayList<HashMap<Long, Integer>> multipleGraph;
    private ArrayList<ArrayList<Long>> multipleDistance;

    public GraphPanel(String title, Boolean speedPanel, HashMap<Long, Integer> graph){
        super();
        this.title = title;
        this.speedPanel = speedPanel;
        this.graph = graph;
        this.newLap = false;
        distanceGraph = new ArrayList<>();
        previousLapNumber = 0;
        Colors = new Color[]{Color.GREEN, Color.GREEN, Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY};
    }


    public void setTrackDistance(long trackDistance){
        this.trackDistance = trackDistance;
    }
    public void setLapNumber(int lapNumber){
        if(previousLapNumber == 0) this.previousLapNumber = lapNumber;
        this.lapNumber = lapNumber;
        if(lapNumber > previousLapNumber){
            newLap = true;

        }
    }
    public void setCurrentDistance(long currentDistance){
        this.currentDistance = currentDistance;
        distanceGraph.add(currentDistance);
    }
    public void updateValue(int value){
        graph.put(currentDistance, value);
        //if(multipleGraph.get(lapNumber) == null){
          //  multipleGraph.add(lapNumber, new ArrayList<>());
        //}
        multipleGraph.get(lapNumber).put(currentDistance, value);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Color.black);
        drawLayout(g);
        drawGraph(g);
    }

    private void drawGraph(Graphics g){

        int graphLength = graph.size();
        int j = lapNumber;
        //for(int j = 0; j < lapNumber; j++) {

            for (int i = 0; i < graphLength - 1; i++) {
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

                        g.setColor(Colors[j]);
                        g.drawLine(x1, y1, x2, y2);
                /*
            55 is location of start on x-axis
            460 is location of finish on x-axis
            152 is location of 0 on y-axis
            30 is location of maximum on y-axis
            320 is maximum speed: need to change this!
             */
                        //todo: make maximum speed variable
                    }

                }

            }
        //}
    }


    private void drawLayout(Graphics g){
        g.setColor(Color.WHITE);
        //y-axis
        g.drawLine(55, 10, 55, 152);
        g.drawLine(55, 10, 50, 15);
        g.drawLine(55, 10, 60, 15);
        g.drawString(title, 5, 20);

        g.drawLine(52, 30, 58, 30);
        if(speedPanel) g.drawString("320", 23, 35); // on speedPanel
        else g.drawString("100%", 16, 35); // on throttlePanel

        //x-axis
        g.drawLine(45, 145, 520, 145);
        g.drawLine(520, 145, 515, 140);
        g.drawLine(520,145,515,150);
        g.drawString("Distance", 480, 165);
        g.setColor(Color.BLUE);
        g.drawString("Start", 40, 165);
        g.drawString("Finish", 440, 165);
        //draw indicator of Finish
        g.drawLine(460, 143, 460, 148);
    }
}
