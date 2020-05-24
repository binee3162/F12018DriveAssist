package gui.graph;

import gui.realtime.map.MapPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Trace extends JFrame {
    private JPanel mainpanel;
    private JPanel mapPanel;
    private String raceID;
    private FullScaleMap map;

    public Trace()  {
         map=new FullScaleMap();
        setContentPane(mainpanel);

        mapPanel.add(map,BorderLayout.CENTER);

        this.setSize(800,850);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
        mapPanel.removeAll();
        mapPanel.repaint();
        mapPanel.add(map,BorderLayout.CENTER);
        mapPanel.revalidate();
        map.setRaceID(raceID);


    }
    public void setLap(int lap){
        mapPanel.removeAll();
        mapPanel.repaint();
        mapPanel.add(map,BorderLayout.CENTER);
        mapPanel.revalidate();
        map.setLap(lap);
    }
}

