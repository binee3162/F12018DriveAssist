package gui.graph;

import javax.swing.*;
public class GraphInt extends JFrame{
    private JPanel mainPanel;
    private JPanel speedPanel;
    private JPanel throttlePanel;
    private GraphPanel speed;
    private GraphPanel throttle;

    public  GraphInt(String title){
        super(title);
        speed = new GraphPanel("Speed", true);
        this.speedPanel.add(speed);
        throttle = new GraphPanel("Throttle", false);
        this.throttlePanel.add(throttle);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(550, 400);
    }

    public void setTrackDistance(int trackDistance){
        //total distance of the track
        speed.setTrackDistance(trackDistance);
        throttle.setTrackDistance(trackDistance);
    }
    public void setCurrentDistance(int currentDistance){
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
