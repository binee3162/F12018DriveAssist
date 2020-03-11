package gui.realtime;


import gui.realtime.map.MapPanel;

import javax.swing.*;
import java.awt.*;

public class RealtimeInt extends JFrame{

    private JPanel mainPanel;
    private JPanel botomPanel;
    private JLabel radioIndicator;
    private JLabel drsIndicator;
    private JLabel regenIndicator;
    private JLabel radioLabel;
    private JLabel drsLabel;
    private JLabel regenLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JProgressBar socBar;
    private JLabel speedLabel;
    private JLabel unity;
    private JPanel pressurePanel;
    private JLabel carImage;
    private JLabel LFtyre;
    private JLabel LBtyre;
    private JLabel RFtyre;
    private JLabel RBtyre;
    private JPanel map;
    private JPanel midPanel;
    private JLabel speedUnit;
    private JLabel lapTimeLabel;
    private JLabel lapTimeText;
    private JLabel power;
    private JPanel speedPanel;

    public RealtimeInt(String title){
        super(title);

        init(); //setup initial values for every component

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(550, 400);
    }

    public void init (){
        //indicator setup
        radioIndicator.setOpaque(true);
        drsIndicator.setOpaque(true);
        regenIndicator.setOpaque(true);
        setIndicator(false, radioIndicator);
        //setIndicator(true, drsIndicator);
        //setIndicator(false, regenIndicator);

        //state of charge setup
        socBar.setUI(new ProgressCircleUI());

        socBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        socBar.setStringPainted(true);
        socBar.setFont(socBar.getFont().deriveFont(24f));

        //lap time setup
        lapTimeLabel.setOpaque(true);
        lapTimeText.setOpaque(true);

        //dummy data
        setSocBar(100);
        //setSpeedLabel(200);
        setTyrePressure(30, 40);
        //setLapTime("2:53");
        setPowerDissipation(200);

        //init map
        MapPanel mapPanel = new MapPanel(3);
        //setContentPane(mapPanel);
        map.add(mapPanel);
    }

    public void setSpeedLabel (int speed) {
        speedLabel.setText(Integer.toString(speed));
    }

    public void setSocBar (int percent) {
        socBar.setValue(percent);
        //set the color of the bar
        int r,g;
        if(percent >= 50) {
            g = 255;
            r = (255 * (100 - percent))/50;
        }
        else {
            r = 255;
            g = (255 * percent )/50;
        }
        socBar.setForeground(new Color(r,g,0));

    }

    public void setDrsIndicator(boolean status){
        setIndicator(status, drsIndicator);
    }

    public void setRegenIndicator(int brake, int speed){
        if(10<brake & 15<speed){                         //Regen active when brakeAmount higher than 10% and speed higher than 15
            setIndicator(true, regenIndicator);
        }else{
            setIndicator(false, regenIndicator);
        }
    }

    public void setIndicator (boolean status, JLabel label) {
        if (status) {
            label.setBackground(Color.GREEN);
            label.setText("ON");
        }
        else {
            label.setBackground(Color.RED);
            label.setText("OFF");
        }
    }

    public void setTyrePressure(float frontTyrePressure, float backTyrePressure) {
        LFtyre.setText(Float.toString(frontTyrePressure));
        RFtyre.setText(Float.toString(frontTyrePressure));
        LBtyre.setText(Float.toString(backTyrePressure));
        RBtyre.setText(Float.toString(backTyrePressure));
    }

    private void createUIComponents() {
        carImage = new JLabel(new ImageIcon("C:\\Users\\Song\\Desktop\\Lecture\\EE5\\formula4\\src\\gui\\realtime\\racecar.png"));

    }

    public void setLapTime (int time) {
        int minutes = time/60;
        int seconds = time - (minutes);
        lapTimeLabel.setText(minutes +":"+ seconds);
    }

    public void setPowerDissipation(int speed) {
        power.setText(speed*1.5 + " kWatt");
    }
}