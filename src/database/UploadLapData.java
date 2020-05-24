package database;

import data.Packet;
import data.PacketCarTelemetryData;
import data.PacketLapData;
import data.PacketMotionData;
import data.elements.CarMotionData;
import data.elements.CarTelemetryData;
import data.elements.LapData;
import sun.security.provider.AuthPolicyFile;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;


public class UploadLapData extends Thread {
    private Connection connection;
    private LinkedList<ArrayList<Packet>> buffer;
    private ArrayList<Integer> raceID;
    private static Object obj;

    public UploadLapData(Connection connection, LinkedList<ArrayList<Packet>> buffer, ArrayList<Integer> raceID, Object obj){
        this.connection=connection;
        this.buffer=buffer;
        this.raceID=raceID;
        UploadLapData.obj =obj;
    }
    @Override
    public void run() {
        synchronized (obj) {
            try {
                while (true) {
                    if(buffer.size()!=0){
                        uploadPacket(buffer.getFirst());
                        buffer.removeFirst();
                    }else{
                        obj.wait(100000);
                    }
                                    }
            } catch (InterruptedException e) {
                System.out.println("thread out");
            }
        }


    }



    private void uploadPacket(ArrayList<Packet> packets){
        PacketLapData packetLapData=(PacketLapData)packets.get(0);
        PacketCarTelemetryData packetTelemetryData=(PacketCarTelemetryData) packets.get(1);
        PacketMotionData packetMotionData=(PacketMotionData) packets.get(2);
        LapData lapData;
        CarTelemetryData telemetryData;
        CarMotionData motionData;
        float lapTime,sector1Time,sector2Time,lapDistance,totalDistance,X,Y,Z;
        int lapNum,speed,throttle,steer,brake,currentLapInvalid,penalties;
        int numCar=raceID.size();
        for(int i =0; i<numCar;i++){
            lapData=packetLapData.getLapDataList().get(i);
            telemetryData=packetTelemetryData.getCarTelemetryData().get(i);
            motionData=packetMotionData.getCarMotionDataList().get(i);
            int id=raceID.get(i);

            X=motionData.getWorldPositionX();
            Y=motionData.getWorldPositionY();
            Z=motionData.getWorldPositionZ();

            speed=telemetryData.getSpeed();
            throttle=telemetryData.getThrottle();
            steer=telemetryData.getSteer();
            brake=telemetryData.getBrake();

            lapTime=lapData.getCurrentLapTime();
            currentLapInvalid=lapData.isCurrentLapInvalid()?1:0;
            penalties=lapData.getPenalties();
            sector1Time=lapData.getSector1Time();
            sector2Time=lapData.getSector2Time();
            lapDistance=lapData.getLapDistance();
            totalDistance=lapData.getTotalDistance();
            lapNum=lapData.getCurrentLapNum();
            String sql="INSERT INTO lapdata (raceID,lapNumber,lapTime,sector1Time,sector2Time,currentLapInvalid,penalties,totalDistance,lapDistance,speed,throttle,brake,steer,X,Y,Z) VALUES ("+id +","+lapNum +","+lapTime +","+sector1Time +","+sector2Time +","+currentLapInvalid +","+penalties +","+totalDistance +","+lapDistance +","+speed +","+throttle +","+brake +","+steer +","+X +","+ Y+","+ Z +" );";
            Statement statement = null;
            ResultSet resultSet;
            try{
                statement=connection.createStatement();
            }catch (SQLException e){
                System.out.println("Connection Error: "+e.getMessage());
            }
            try{
                assert statement != null;
                statement.execute(sql);
            }catch (SQLException e){
                System.out.println("execute Error: "+sql+e.getMessage());

            }

        }
    }

}
