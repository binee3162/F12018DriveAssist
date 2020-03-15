package gui.realtime;


import gui.realtime.map.MapPanel;
import gui.realtime.map.DashBoard;

import javax.swing.*;
import java.awt.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

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
    private JLabel lapTimeLabel;
    private JLabel lapTimeText;
    private JPanel speedPanel;
    private DashBoard speedIndicator;

    public RealtimeInt(String title){
        super(title);

        init(); //setup initial values for every component

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(550, 400);
    }

    public static void main(String[] args) {
        JFrame frame = new RealtimeInt("Realtime Interface");

        frame.setVisible(true);
    }

    public void init (){
        //indicator setup
        radioIndicator.setOpaque(true);
        drsIndicator.setOpaque(true);
        regenIndicator.setOpaque(true);
        setIndicator(false, radioIndicator);
        setIndicator(true, drsIndicator);
        setIndicator(false, regenIndicator);

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
        setLapTime("2:53");

        //init speed


            speedIndicator = new DashBoard();
            speedIndicator.setValue("1");
            speedIndicator.setUnit("km/h");
            speedPanel.add(speedIndicator);






        //init map
        MapPanel mapPanel = new MapPanel(3);
        //setContentPane(mapPanel);
        map.add(mapPanel);




    }

    public void setSpeedLabel (int speed) {
        speedLabel.setText(Integer.toString(speed));
    }
    public void setSpeed(int speed){
        speedIndicator.setValue(speed+"");
        repaint();
        log.println( "hello\n ");

    }
    public void setSocBar (int percent) {
        socBar.setValue(percent);
        //set the color of the bar
        int r = 0;
        int g = 0;
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

    public void setTyrePressure(int frontTyrePressure, int backTyrePressure) {
        LFtyre.setText(Integer.toString(frontTyrePressure));
        RFtyre.setText(Integer.toString(frontTyrePressure));
        LBtyre.setText(Integer.toString(backTyrePressure));
        RBtyre.setText(Integer.toString(backTyrePressure));
    }

    private void createUIComponents() {
        carImage = new JLabel(new ImageIcon("C:\\Users\\Song\\Desktop\\Lecture\\EE5\\formula4\\src\\gui\\realtime\\racecar.png"));

    }

    public void setLapTime (String time) {
        lapTimeLabel.setText(time);
    }
}