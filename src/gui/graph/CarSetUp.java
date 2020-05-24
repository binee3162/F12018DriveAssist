package gui.graph;

import database.DbMethod;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarSetUp extends JFrame{
    private JLabel fWing;
    private JLabel onThrottle;
    private JLabel rWIng;
    private JLabel offThrottle;
    private JLabel frontCamber;
    private JLabel rearCamber;
    private JLabel frontToe;
    private JLabel rearToe;
    private JLabel brakePressure;
    private JLabel brakeBias;
    private JLabel frontTyrePressure;
    private JLabel rearTyrePressure;
    private JPanel mainPanel;

    public CarSetUp() {

        super("Car Setup Data");
        setContentPane(mainPanel);
        this.setSize(450,400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

   public void loadSetUpData(String raceID){
       DbMethod db=new DbMethod();
       db.connectDb();
       String sql="SELECT fWing,rWing,onThrottle,offThrottle,frontCamber,rearCamber,frontToe,rearToe,brakePressure,brakeBias,frontTyrePressure,rearTyrePressure from ee5.race where raceID="+raceID+";";
       ResultSet resultSet=db.executeSql(sql);
       try{
           if(resultSet.next()){
               fWing.setText(resultSet.getInt(1)+"");
               rWIng.setText(resultSet.getInt(2)+"");
               onThrottle.setText(resultSet.getInt(3)+"");
               offThrottle.setText(resultSet.getInt(4)+"");
               frontCamber.setText(resultSet.getFloat(5)+"");
               rearCamber.setText(resultSet.getFloat(6)+"");
               frontToe.setText(resultSet.getFloat(7)+"");
               rearToe.setText(resultSet.getFloat(8)+"");
               brakePressure.setText(resultSet.getInt(9)+"");
               brakeBias.setText(resultSet.getInt(10)+"");
               frontTyrePressure.setText(resultSet.getFloat(11)+"");
               rearTyrePressure.setText(resultSet.getFloat(12)+"");


           }
           db.closeConn();
       }catch (SQLException e){
           e.printStackTrace();
       }



   }
}
