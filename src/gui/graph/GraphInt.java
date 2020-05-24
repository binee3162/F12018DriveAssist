package gui.graph;

import database.DbMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GraphInt extends JFrame{
    private JPanel mainPanel;
    private JPanel speedPanel;
    private JPanel throttlePanel;
    private JPanel steerPanel;
    private JPanel brakePanel;
    private JButton changeMode;
    private JComboBox lapDisp;
    private JLabel modeIndicator;
    private JButton loadData;
    private GraphPanel brake;
    private GraphPanel speed;
    private GraphPanel steer;
    private GraphPanel throttle;
    private Boolean useDB=false;
    private String raceID;


    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public  GraphInt(String title){
        super(title);
        speed = new GraphPanel("Speed", 1);
        this.speedPanel.add(speed);
        steer = new GraphPanel("Steer", 2);
        this.steerPanel.add(steer);
        throttle = new GraphPanel("Throttle", 3);
        this.throttlePanel.add(throttle);
        brake = new GraphPanel("Brake", 4);
        this.brakePanel.add(brake);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(700, 800);
        lapDisp.setVisible(useDB);

        ChooseFile chooseFile=new ChooseFile();
        chooseFile.setVisible(false);
        Trace trace=new Trace();
        trace.setVisible(false);
        CarSetUp carSetUp=new CarSetUp();
        carSetUp.setVisible(false);
        loadData.setVisible(false);
        modeIndicator.setText("RealTime");

        changeMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource()== changeMode){

                    if(useDB){
                        chooseFile.setVisible(false);
                        useDB=false;
                        speed.setUseDB(false);
                        throttle.setUseDB(false);
                        steer.setUseDB(false);
                        brake.setUseDB(false);
                        modeIndicator.setText("RealTime");
                        loadData.setVisible(false);
                        lapDisp.setVisible(false);
                    }
                    else
                        chooseFile.setVisible(true);


                }
            }
        });
        lapDisp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareGraphFromDB(raceID,lapDisp.getSelectedIndex());
                speed.setUseDB(true);
                throttle.setUseDB(true);
                steer.setUseDB(true);
                brake.setUseDB(true);
                trace.setLap(lapDisp.getSelectedIndex());
            }
        });
        loadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()== loadData){
                    lapDisp.setVisible(true);
                    prepareGraphFromDB(raceID,0);
                    speed.setUseDB(true);
                    throttle.setUseDB(true);
                    steer.setUseDB(true);
                    brake.setUseDB(true);
                    carSetUp.loadSetUpData(raceID);
                    carSetUp.setVisible(true);
                }
            }
        });
        chooseFile.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                raceID=chooseFile.getRaceID();
                if(!raceID.equals("123")){
                    loadData.setVisible(true);
                    useDB=true;
                    modeIndicator.setText("Database");
                    trace.setRaceID(raceID);
                    trace.setVisible(true);


                }

            }
        });
    }

    public void setTrackDistance(int trackDistance){
        //total distance of the track
        speed.setTrackDistance(trackDistance);
        throttle.setTrackDistance(trackDistance);
        steer.setTrackDistance(trackDistance);
        brake.setTrackDistance(trackDistance);
    }
    public void setCurrentDistance(int currentDistance){
        //distance from the start of the lap
        speed.setCurrentDistance(currentDistance);
        throttle.setCurrentDistance(currentDistance);
        steer.setCurrentDistance(currentDistance);
        brake.setCurrentDistance(currentDistance);
    }

    public void setLapNumber(int lapNumber){
        // which track the player is currently on.
        speed.setLapNumber(lapNumber);
        throttle.setLapNumber(lapNumber);
        steer.setLapNumber(lapNumber);
        brake.setLapNumber(lapNumber);
    }
    public void resetLap(){
        mainPanel.repaint();
        mainPanel.revalidate();
        speed = new GraphPanel("Speed", 1);
        speedPanel.removeAll();
        speedPanel.add(speed);
        steer = new GraphPanel("Steer", 2);
        steerPanel.removeAll();
        steerPanel.add(steer);
        throttle = new GraphPanel("Throttle", 3);
        throttlePanel.removeAll();
        throttlePanel.add(throttle);
        brake = new GraphPanel("Brake", 4);
        brakePanel.removeAll();
        brakePanel.add(brake);
        System.out.println("restart");
    }

    public JButton getChangeMode() {
        return changeMode;
    }

    public void setCurrentBrake(int currentBrake){
        brake.updateValue(currentBrake);
    }
    public void setCurrentSteer(int currentSteer){
        steer.updateValue(currentSteer);
    }
    public void setCurrentThrottle(int currentThrottle){
        throttle.updateValue(currentThrottle);
    }
    public void setCurrentSpeed(int currentSpeed){
        speed.updateValue(currentSpeed);
    }
    private void prepareGraphFromDB(String raceID,int lap) {
        DbMethod db = new DbMethod();
        ArrayList<GraphData> steerDB=new ArrayList<>();
        ArrayList<GraphData> speedDB=new ArrayList<>();
        ArrayList<GraphData> throttleDB=new ArrayList<>();
        ArrayList<GraphData> brakeDB=new ArrayList<>();

        db.connectDb();
        String sql;
        if(lap!=0)
             sql = "SELECT lapDistance,lapNumber,speed,throttle,brake,Steer from ee5.lapdata where raceID="+raceID+" and lapNumber="+lap+";";
        else
            sql= "SELECT lapDistance,lapNumber,speed,throttle,brake,Steer from ee5.lapdata where raceID="+raceID+";";
        ResultSet resultSet = db.executeSql(sql);

        try {
            int currentLap=0;
           GraphData  steerData=new GraphData(1);
           GraphData  speedData=new GraphData(1);
           GraphData  throttleData=new GraphData(1);
           GraphData  brakeData=new GraphData(1);
            while (resultSet.next()) {
                int lapDistance=(int)resultSet.getFloat(1);
                int lapNumber=resultSet.getInt(2);
                int speed=(int)resultSet.getFloat(3);
                int throttle=resultSet.getInt(4);
                int brake=resultSet.getInt(5);
                int steer=resultSet.getInt(6);
                if(lapDistance>0){

                    if(currentLap!=lapNumber){

                        if(lap==0){
                            if(currentLap!=lap){

                                steerDB.add(currentLap-1,steerData);
                                speedDB.add(currentLap-1,speedData);
                                throttleDB.add(currentLap-1,throttleData);
                                brakeDB.add(currentLap-1,brakeData);

                            }
                            currentLap++;
                        }

                        else{
                            if(currentLap!=lap){

                                steerDB.add(0,steerData);
                                speedDB.add(0,speedData);
                                throttleDB.add(0,throttleData);
                                brakeDB.add(0,brakeData);

                            }
                            currentLap=lap;
                        }

                        steerData=new GraphData(lapNumber);
                        speedData=new GraphData(lapNumber);
                        throttleData=new GraphData(lapNumber);
                        brakeData=new GraphData(lapNumber);
                    }

                    steerData.setDistance(lapDistance);
                    speedData.setDistance(lapDistance);
                    brakeData.setDistance(lapDistance);
                    throttleData.setDistance(lapDistance);
                    steerData.setValue(steer);
                    speedData.setValue(speed);
                    brakeData.setValue(brake);
                    throttleData.setValue(throttle);
                }

            }
            if(lap==0) {
                if (currentLap != 0) {
                    steerDB.add(currentLap - 1, steerData);
                    speedDB.add(currentLap - 1, speedData);
                    throttleDB.add(currentLap - 1, throttleData);
                    brakeDB.add(currentLap - 1, brakeData);
                }
            }else{
                steerDB.add(0, steerData);
                speedDB.add(0, speedData);
                throttleDB.add(0, throttleData);
                brakeDB.add(0, brakeData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql="SELECT totalDistance from ee5.race where raceID="+raceID+" LIMIT 1;";
        resultSet = db.executeSql(sql);
        int distance=5000;
        try{
            if(resultSet.next())
                distance=resultSet.getInt(1);

        }catch (SQLException e){
            e.printStackTrace();
        }
        speed.setTrackDistance(distance);
        steer.setTrackDistance(distance);
        brake.setTrackDistance(distance);
        throttle.setTrackDistance(distance);
        speed.setDataListPerLapDB(speedDB);
        steer.setDataListPerLapDB(steerDB);
        brake.setDataListPerLapDB(brakeDB);
        throttle.setDataListPerLapDB(throttleDB);

    }
}
