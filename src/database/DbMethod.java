package database;

import data.*;
import data.elements.*;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class DbMethod {
    private Connection connection;


    public Connection  connectDb()  {
        String dbName="ee5";
        String url="jdbc:mariadb://192.168.31.76:3306/";
        String userName="song";
        String password="gs980715";
        try{
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
        }catch (Exception e){
            System.out.println("JDBC driver 404");
        }
        try{
            connection= DriverManager.getConnection(url+dbName,userName,password);
        }catch (SQLException e){
            System.out.println("Connection Error: "+url+"\n"+e.getMessage());
        }
        return connection;
    }
    public ResultSet executeSql(String sql){
        Statement statement;
        ResultSet resultSet;
        try{
            statement=connection.createStatement();
        }catch (SQLException e){
            System.out.println("Connection Error: "+e.getMessage());
            return null;
        }
        try{
          resultSet =statement.executeQuery(sql);
        }catch (SQLException e){
            System.out.println("execute Error: "+sql+e.getMessage());
            return null;
        }
        return resultSet;

    }
    public boolean execute(String sql){
        Statement statement = null;
        ResultSet resultSet;
        try{
            statement=connection.createStatement();
        }catch (SQLException e){
            System.out.println("Connection Error: "+e.getMessage());
            return false;
        }
        try{
            assert statement != null;
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("execute Error: "+sql+e.getMessage());
            return false;
        }
        return true;

    }
    public int insertReturnPK(String sql) {
        PreparedStatement statement;
        ResultSet resultSet;
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.execute();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            System.out.println("execute Error: " + sql + e.getMessage());
        }
        return -1;
    }
    //false if not exist
    public Boolean checkIfExist(String sql){
        ResultSet resultSet=executeSql(sql);
        try {
            return resultSet.next();
        }catch (SQLException e){
            System.out.println("execute Error: " + sql + e.getMessage());
        }
        return false;
    }


    public void closeConn(){
        try {
            connection.close();
        }catch (SQLException e){
            System.out.println("error while closing connection:"+e);
        }
    }
    public ArrayList<Integer> uploadParticipantPacket(PacketParticipantsData packet,String datestr){
        List<ParticipantData> participantData;
        participantData=packet.getParticipants();
        ResultSet resultSet;
        ArrayList<Integer> raceID=new ArrayList<>();
        BigInteger sessionID=packet.getHeader().getSessionUID();
        for(int i=0;i<packet.getNumCars();i++){
            String name=participantData.get(i).getName();
            int teamID=participantData.get(i).getTeamId();
            int driverID=-1;
            if(!checkIfExist("SELECT * FROM ee5.driver where name = \'"+name+"\';")){
                driverID=insertReturnPK("INSERT INTO driver (name) VALUES (\'"+name+"\');");
            }else{
                resultSet=executeSql("SELECT driverID FROM ee5.driver where name = \'"+name+"\';");
                try{
                    resultSet.next();
                    driverID=resultSet.getInt(1);
                }catch(SQLException e){
                    System.out.println("error while closing connection:"+e);
                    return null;
                }

            }
            int id=insertReturnPK("INSERT INTO race (sessionID,driverID,time,teamID) VALUES ('"+sessionID+"',"+driverID+",'"+datestr+"',"+teamID+");");
            raceID.add(i,id);
        }
        return raceID;
    }
    public Boolean uploadLapData(ArrayList<Packet> packets,ArrayList<Integer> raceID){
        PacketLapData packetLapData=(PacketLapData)packets.get(0);
        PacketCarTelemetryData packetTelemetryData=(PacketCarTelemetryData) packets.get(1);
        PacketMotionData packetMotionData=(PacketMotionData) packets.get(2);
        LapData lapData;
        CarTelemetryData telemetryData;
        CarMotionData motionData;
        Boolean success=true;
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
            success=execute(sql)&&success;
        }
            return success;
        }



    public Boolean uploadSetupPacket(PacketCarSetupData packet,ArrayList<Integer> raceID){
        List<CarSetupData> carSetupData= packet.getCarSetups();
        int numCar=raceID.size();
        int fWing,rWing,onThrottle,offThrottle,frontSuspension,rearSuspension,frontAntiRollBar,rearAntiRollBar,brakePressure,brakeBias,ballast;
        float frontCamber,rearCamber,frontToe,rearToe,frontTyrePressure,rearTyrePressure,fuelLoad;
        int id;
        Boolean success=true;
        for(int i=0;i<numCar;i++){
            CarSetupData setupData=carSetupData.get(i);
            id=raceID.get(i);
            fWing=setupData.getFrontWing();
            rWing=setupData.getRearWing();
            onThrottle=setupData.getOnThrottle();
            offThrottle=setupData.getOffThrottle();
            frontSuspension=setupData.getFrontSuspension();
            rearSuspension=setupData.getRearSuspension();
            frontAntiRollBar=setupData.getFrontAntiRollBar();
            rearAntiRollBar=setupData.getRearAntiRollBar();
            brakePressure=setupData.getBrakePressure();
            brakeBias=setupData.getBrakeBias();
            ballast=setupData.getBallast();
            frontCamber=setupData.getFrontCamber();
            rearCamber=setupData.getRearCamber();
            frontToe=setupData.getFrontToe();
            rearToe=setupData.getRearToe();
            frontTyrePressure=setupData.getFrontTirePressure();
            rearTyrePressure=setupData.getRearTirePressure();
            fuelLoad=setupData.getFuelLoad();
            String sql="UPDATE ee5.race SET fWing='"+fWing+"',rWing='"+rWing+"',onThrottle='"+onThrottle+"',offThrottle='"+offThrottle+"',frontSusp='"+frontSuspension+"',rearSusp='"+rearSuspension+"',frontAntiRoll='"+frontAntiRollBar+"',rearAntiRoll='"+rearAntiRollBar+"',brakePressure='"+brakePressure+"',brakeBias='"+brakeBias+"',ballast='"+ballast+"',frontCamber='"+frontCamber+"',rearCamber='"+rearCamber+"',frontToe='"+frontToe+"',rearToe='"+rearToe+"',frontTyrePressure='"+frontTyrePressure+"',rearTyrePressure='"+rearTyrePressure+"',fuelLoad='"+fuelLoad+"' WHERE raceID="+id+"; ";
            success=execute(sql)&&success;

        }

        return success;
    }

    public static void main(String[] args) throws SQLException {
        DbMethod t=new DbMethod();
        t.connectDb();
        DbMethod t1=new DbMethod();
        t1.connectDb();
//        int teamID=0;
//        int driverID=0;
//        String datestr = "2020-05-19 01:14:46";
//        int id=t.insertReturnPK("INSERT INTO race (sessionID,driverID,time,teamID) VALUES (123456789,"+driverID+",'"+datestr+"',"+teamID+");");

//        String name="CODEX";
//        int driverID=t.insertReturnPK("INSERT INTO driver (name) VALUES ('"+name+"');");
//        int sessionType=0;
//        int weather=0;
//        int era=1;
//        int id=123456790;
//        int trackID=0;
//        int teamID=0;
//    t.execute("show tables");

        ResultSet resultSet=t.executeSql("SELECT count(driverID) FROM ee5.driver where name = 'Song';");
        resultSet.next();
        System.out.println(resultSet.getString(1));
       // System.out.println(Weather.STORM.ordinal());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        java.util.Date date = new Date();
//        String datestr=format.format(date);
//        System.out.println(datestr);
        //System.out.println(t.insertReturnPK("INSERT INTO race (sessionID,time) VALUES (1,"+"'"+datestr+"'"+");"));
//        ResultSet resultSet=t.executeSql("SHOW tables");
//        while (resultSet.next()){

//                System.out.println(resultSet.getString(1));
//            }

    }


//        String dbName="ee5";
//        String url="jdbc:mysql://192.168.31.76:3306/";
//        String userName="song";
//        String password="gs980715";
//        String sql="SHOW tables";
//        Connection conn=null;
//        try{
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            conn= DriverManager.getConnection(url+dbName,userName,password);
//            Statement statement=conn.createStatement();
//            ResultSet resultSet=statement.executeQuery(sql);
//        try{
//            while (resultSet.next()){
//                System.out.println(resultSet.getString(1));
//            }
//        }catch (Exception e){
//            System.out.println("Error while reading! \n"+e.getMessage());
//        }
//
//            conn.close();
//
//        }catch (SQLException e){
//            System.out.println("Connection Error: "+url+"\n"+e.getMessage());
//        }
//
//
//        return 0;



}