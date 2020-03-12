package gui.statistic;

import javax.swing.*;
import java.awt.*;

public class StatisticInterface extends JFrame {
    private JPanel mainPanel;
    private JPanel upLeftPanel;
    private JPanel upMidPanel;
    private JPanel upRightPanel;
    private JPanel pressurePanel;
    private JPanel downMidPanel;
    private JPanel downLeftPanel;
    private JLabel LFtyre;
    private JLabel LBtyre;
    private JLabel RFtyre;
    private JLabel RBtyre;
    private JLabel carImage;
    private JLabel maxSpeedLabel;
    private JLabel avgSpeedLabel;
    private JLabel bestLapTimeLabel;
    private JLabel highScoreLabel;
    private JLabel powerConsumptionLabel;

    public StatisticInterface() {
        super();

        init();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(480, 300);
    }

    /*
    public static void main(String[] args) {
        StatisticInterface statInt = new StatisticInterface();
        statInt.setVisible(true);
    }
    */

    private void init() {
        //dummy data
        //setTyrePressure(30, 40);
        setHighScore("1:52 min");
        setPowerConsumption(100);
        setAvgSpeed(100);
        setMaxSpeed(250);
        setBestLapTime("1:55 min");
    }
    public void setHighScore (String time) {
        highScoreLabel.setText(time);
    }

    public void setPowerConsumption (int power) {
        powerConsumptionLabel.setText(Integer.toString(power) + " watt");
    }

    public void setAvgSpeed (int speed) {
        avgSpeedLabel.setText(Integer.toString(speed) + " km/h");
    }

    public void setMaxSpeed (int speed) {
        maxSpeedLabel.setText(Integer.toString(speed) + " km/h");
    }

    public void setBestLapTime (String time) {
        bestLapTimeLabel.setText(time);
    }


    public void setTyrePressure(int RFP, int LFP, int RBP, int LBP) {
        LFtyre.setText(Integer.toString(LFP));
        RFtyre.setText(Integer.toString(RFP));
        LBtyre.setText(Integer.toString(LBP));
        RBtyre.setText(Integer.toString(RBP));
    }

    private void createUIComponents() {
        carImage = new JLabel(new ImageIcon("D:\\school\\GroepT\\Sem6\\7_EE5\\youKnowWhat\\src\\gui\\realtime\\racecar.png"));
    }

}
