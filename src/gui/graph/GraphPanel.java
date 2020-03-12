package gui.graph;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    //private boolean firstCall;
    private String title;
    private Boolean speedPanel;
    private ArrayList<Integer> graph;
    private long trackDistance;
    private int lapNumber;
    private long currentDistance;
    private boolean newLap;


    public GraphPanel(String title, Boolean speedPanel, ArrayList<Integer> graph){
        super();
        this.title = title;
        this.speedPanel = speedPanel;
        this.graph = graph;
        this.newLap = false;
        //this.firstCall = true;
    }

    public void updateList(ArrayList<Integer> list){
        graph = list;
    }
    public void setTrackDistance(long trackDistance){
        this.trackDistance = trackDistance;
    }
    public void setLapNumber(int lapNumber){
        this.lapNumber = lapNumber;
    }
    public void setCurrentDistance(long currentDistance){
        this.currentDistance = currentDistance;
    }
    public void updateValue(int value){
        graph.add(value);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Color.black);
        switch(lapNumber) {
            case 1:
                g.setColor(Color.YELLOW);
                break;
            case 2:
                g.setColor(Color.GREEN);
                newLap = true;
                break;
            case 3:
                g.setColor(Color.BLUE);
                newLap = true;
                break;

            case 4:
                g.setColor(Color.CYAN);
                newLap = true;
                break;

            case 5:
                g.setColor(Color.MAGENTA);
                newLap = true;
                break;
        }
        drawLayout(g);
        drawGraph(g);
    }

    //todo: make graph in function of distance instead of time
    private void drawGraph(Graphics g){

        int length = graph.size();
        int j = 0;
        for(int i = 0; i<length-1; i++){
            // for every value in ArrayList, display it on the screen
            if(length-i > 1){//to avoid outOfBoundsError

                if(newLap){
                    j = 0;
                    newLap = false;
                }

                int value1 = graph.get(j);
                int value2 = graph.get(j+1);
                int x1= 55+ Math.round(j*(460-55)/(length-1));
                int y1;
                int x2= 55+Math.round((j+1)*(460-55)/(length-1));
                int y2;
                if(speedPanel){
                    //calculate the pixels for speedgraph
                    y1 = 152+ Math.round(value1*(30-152)/320);
                    y2 = 152+ Math.round(value2*(30-152)/320);
                }
                else{
                    y1 = 152 + Math.round(value1*(30-152)/100);
                    y2 = 152 + Math.round(value2*(30-152)/100);
                }
                g.drawLine(x1, y1,x2,y2);
                /*
            55 is location of start on x-axis
            460 is location of finish on x-axis
            152 is location of 0 on y-axis
            30 is location of maximum on y-axis
            320 is maximum speed: need to change this!
             */
                //todo: make maximum speed variable
                //todo: make equation for throttlePanel
                j++;
            }

        }
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
