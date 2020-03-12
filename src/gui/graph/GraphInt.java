package gui.graph;

import javax.swing.*;
import java.util.ArrayList;

public class GraphInt extends JFrame{
    private JPanel mainPanel;
    private JPanel speedPanel;
    private JPanel throttlePanel;
    private GraphPanel speed;
    private GraphPanel throttle;

    public  GraphInt(String title){
        super(title);
        ArrayList<Integer> speedList = new ArrayList<>();
        speed = new GraphPanel("Speed", true, speedList);
        this.speedPanel.add(speed);
        ArrayList<Integer> throttleList = new ArrayList<>();
        throttle = new GraphPanel("Throttle", false, throttleList);
        this.throttlePanel.add(throttle);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(550, 400);
    }

    public void setTrackDistance(long trackDistance){
        //total distance of the track
        speed.setTrackDistance(trackDistance);
        throttle.setTrackDistance(trackDistance);
    }
    public void setCurrentDistance(long currentDistance){
        //distance from the start of the lap
        speed.setCurrentDistance(currentDistance);
        throttle.setCurrentDistance(currentDistance);
    }
    public void setLapNumber(int lapNumber){
        // which track the player is currently on.
        speed.setLapNumber(lapNumber);
        throttle.setLapNumber(lapNumber);
    }
    public void setCurrentSpeed(int currentSpeed){
        speed.updateValue(currentSpeed);
    }
    public void setCurrentThrottle(int currentThrottle){
        throttle.updateValue(currentThrottle);
    }

}
