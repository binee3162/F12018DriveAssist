package logic;

import data.*;
import data.elements.*;
import database.UploadLapData;
import database.DbMethod;
import gui.graph.GraphInt;
import gui.realtime.DriverInfo;
import gui.realtime.RealtimeInt;

import javax.swing.text.StyledEditorKit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import static java.time.LocalDateTime.now;

public class Session {
    private RealtimeInt realtimeInt;

    private GraphInt graphInt;
    private RaspberryPiInterface raspberry;
    private static int i=0;
    private Connection connection=null;
    private DbMethod db;
    private ArrayList<Integer> raceID=new ArrayList<>();
    private int playerCarIndex;
    private Boolean participantGet=false;
    private Boolean eventGet=false;
    private Boolean setupGet=false;
    private Boolean sessionGet=false;
    private String datestr;
    private Boolean recordParticipant=false;
    private Boolean telemetryGet=false;
    private Boolean motionDataGet=false;
    private ArrayList<Packet> lapPacket=new ArrayList<>();
    private LinkedList<ArrayList<Packet>> buffer;
    private UploadLapData uploadLapData;
    private DriverInfo driverInfo;
    private String driver_name;
    private String driver_tel;
    private String driver_email;
    private int count=0;
    private int totalDistance;
    private int slowDown=1;
    private Boolean driverInfoGet=false;
    private static final Object obj=new Object();
    public Session(RealtimeInt realtimeInt, GraphInt graphInt, RaspberryPiInterface raspberry) {//todo:other interface
        this.realtimeInt = realtimeInt;

        this.graphInt = graphInt;
        this.raspberry = raspberry;

        db = new DbMethod();
        //do something
        //init data buffer
        for(int i=0;i<3;i++) {
            lapPacket.add(new PacketCarSetupData());
        }
        driverInfo=new DriverInfo();

        driverInfo.setVisible(false);
        driverInfo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                driver_email=driverInfo.getEmail();
                driver_tel=driverInfo.getTel();
                driver_name=driverInfo.getName();
                ResultSet resultSet;
                int driverID=-1;
                if(driver_name!=null) {
                    if (!db.checkIfExist("SELECT * FROM ee5.driver where name = \'" + driver_name + "\';")) {
                        driverID = db.insertReturnPK("INSERT INTO driver (name,email,tel) VALUES ('" + driver_name + "','" + driver_email + "','" + driver_tel + "');");
                    } else {
                        resultSet = db.executeSql("SELECT driverID FROM ee5.driver where name = \'" + driver_name + "\';");
                        try {
                            resultSet.next();
                            driverID = resultSet.getInt(1);
                        } catch (SQLException sqlE) {
                            System.out.println("error while closing connection:" + sqlE);

                        }
                        if(driver_email!=null) db.executeSql("UPDATE ee5.driver SET email='" + driver_email + "' where driverID =" + driverID + ";");
                        if(driver_tel!=null) db.executeSql("UPDATE ee5.driver SET tel='" + driver_tel + "' where driverID =" + driverID + ";");
                    }
                    if (driverID != -1) {
                        db.executeSql("UPDATE ee5.race SET driverID='" + driverID + "' where raceID =" + raceID.get(playerCarIndex) + ";");
                    }
                }
                driverInfoGet=true;
            }
        });
    }

    public void handleMotionData(PacketMotionData packet){
        CarMotionData player=packet.getCarMotionDataList().get(packet.getHeader().getPlayerCarIndex());
        float x=player.getWorldPositionX();
        float y=player.getWorldPositionZ();
        float ForDirX=player.getWorldForwardDirX();
        float ForDirZ=player.getWorldForwardDirZ();
        int degree=(int)Math.abs(Math.toDegrees(Math.atan(ForDirZ / ForDirX)));
        if(ForDirX<0){
            if (ForDirZ>0){
                degree=180-degree;
            }else
                degree=180+degree;
        }else{
            if (ForDirZ>0){}
            else
                degree=360-degree;
        }
        realtimeInt.setPosition(180-degree,(int)y,(int)x);
//        System.out.println(player.getPitch()+"  "+player.getRoll()+" "+player.getYaw()+"\n");
//        System.out.println(x+","+y);

//        float RightDirX=player.getWorldRightDirX();
//        float RightDirZ=player.getWorldRightDirZ();
//        if(i++==5) {
////            Math.toDegrees(Math.atan(ForDirZ / ForDirX))
//            //System.out.println(ForDirX + " " + ForDirY + " " + ForDirZ);
//            System.out.println();
//            //System.out.println(RightDirX+" "+RightDirZ+" "+Math.toDegrees(Math.atan(RightDirZ/RightDirX)));
//            i=0;
//        }
        if(!motionDataGet&&participantGet){
            lapPacket.set(2,packet);
            motionDataGet=true;
        }
    }
    public void handleSessionData(PacketSessionData packet){
        float totalLapDistance = packet.getTrackLength();
        graphInt.setTrackDistance((int) totalLapDistance);


        boolean paused = packet.isGamePaused();
        raspberry.setTSAL(paused);
        int sessionType=packet.getSessionType().ordinal();
        int trackID=packet.getTrackId();
        int weather=packet.getWeather().ordinal();
        int era=packet.getEra().ordinal();
        realtimeInt.setTrack(trackID);
        if(participantGet&&eventGet) {
            raceID.forEach(id -> {
                db.execute("UPDATE ee5.race SET sessionType='" + sessionType + "',trackID='" + trackID + "',totalDistance='" + totalLapDistance+ "',weather='" + weather + "',era='" + era + "' WHERE raceID=" + id + ";");
            });
        }
        if(packet.getSessionType()==SessionType.TIME_TRIAL){
            slowDown=1;
        }else {
            slowDown=5;
        }
      //  UPDATE `ee5`.`driver` SET `tel` = '489415813' WHERE (`driverID` = '0');


    }
    public void handleLapData(PacketLapData packet){
        float currentLapTime=packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getCurrentLapTime();
        realtimeInt.setLapTime((int)currentLapTime);
        int currentLap = packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getCurrentLapNum();
        graphInt.setLapNumber(currentLap);
        float currentDistance = packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getLapDistance();
        graphInt.setCurrentDistance((int) currentDistance);
        DriverStatus d=packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getDriverStatus();
//        checkDistance((int) packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getTotalDistance());

        if (motionDataGet&&telemetryGet&&eventGet&&!d.equals(DriverStatus.IN_GARAGE)){
            lapPacket.set(0,packet);
//            if(!db.uploadLapData(lapPacket,raceID)){
//                System.out.println("LapData error");
//            }
//            UploadLapData uploadLapData =new UploadLapData(connection,lapPacket,raceID);

//            uploadLapData.start();
            if(++count>=slowDown) {
                buffer.addLast((ArrayList<Packet>) lapPacket.clone());
                synchronized (obj) {
                    obj.notify();
                }
                count=0;
            }
            motionDataGet=false;
            telemetryGet=false;

        }
        if(eventGet&&participantGet&&d.equals(DriverStatus.IN_GARAGE)&&!driverInfoGet){
            driverInfo.setVisible(true);
        }
    }
    public void handleCarSetupData(PacketCarSetupData packet){
        if(participantGet&&(!setupGet)){
            if(db.uploadSetupPacket(packet,raceID)){
                setupGet=true;
            }
        }
    }
    public void handleCarStatusData(PacketCarStatusData packet){
        float fuelCapacity = packet.getCarStatuses().get(packet.getHeader().getPlayerCarIndex()).getFuelCapacity();
        float currentFuel = packet.getCarStatuses().get(packet.getHeader().getPlayerCarIndex()).getFuelInTank();
        realtimeInt.setSocBar(fuelCapacity,currentFuel);

    }
    public void handleCarTelemetryData(PacketCarTelemetryData packet){
        float speed=packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getSpeed();
        realtimeInt.setSpeed((int)speed);
        float steer=packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getSteer();
        float brake=packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getBrake();
        raspberry.setBrakeLED(brake);
        boolean drs = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).isDrs();
        realtimeInt.setDrsIndicator(drs);

        int amountBrake = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getBrake(); //gives amount of brake between 0 and 100
        realtimeInt.setRegenIndicator(amountBrake);

        graphInt.setCurrentSpeed((int) speed);
        graphInt.setCurrentSteer((int)steer);
        graphInt.setCurrentBrake((int)brake);
        WheelData<Float> tire = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getTirePressure();
        int fl = Math.round(tire.getFrontLeft());
        int fr = Math.round(tire.getFrontRight());
        int bl = Math.round(tire.getRearLeft());
        int br = Math.round(tire.getRearRight());
        realtimeInt.setTyrePressure(fr, fl, br, bl);
        int throttle = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getThrottle();
        graphInt.setCurrentThrottle(throttle);
        if(!telemetryGet&&participantGet){
            lapPacket.set(1,packet);
            telemetryGet=true;
        }

    }
    public void handleEventData(PacketEventData packet){
        String code=packet.getEventCode();
        playerCarIndex=packet.getHeader().getPlayerCarIndex();
        if(code.equals("SSTA")){

            connection=db.connectDb();
            if(connection!=null) realtimeInt.getDbStatus().setText("DB running");
            else realtimeInt.getDbStatus().setText("DB failed");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = new Date();
            datestr=format.format(date);
           // raceID=db.insertReturnPK("INSERT INTO race (sessionID,time) VALUES ("+packet.getHeader().getSessionUID()+",'"+datestr+"'"+");");
            eventGet= connection != null;

            motionDataGet=false;
            telemetryGet=false;
            setupGet=false;
            participantGet=false;

        }
        if(code.equals("SEND")){
            driver_email=null;
            driver_tel=null;
            driver_name=null;
            eventGet=false;
            participantGet=false;
            realtimeInt.getComStatus().setText(" ");

            graphInt.resetLap();
//            db.closeConn();
//            uploadLapData.interrupt();
        }
    }
    public void handleParticipantsData(PacketParticipantsData packet){
        if(!participantGet && eventGet){
            raceID=db.uploadParticipantPacket(packet,datestr);

            if((raceID!=null)&&(raceID.get(0)!=-1))
            participantGet=true;
            buffer=new LinkedList<>();
            uploadLapData=new UploadLapData(connection,buffer,raceID,obj);
            uploadLapData.setPriority(1);
            uploadLapData.start();
            realtimeInt.getComStatus().setText("Uploading");


        }
    }
//    public void checkDistance(int distance){
//        if(distance<-100&&totalDistance>0){
//            participantGet=false;
//            graphInt.resetLap();
//
//        }
//        totalDistance=distance;
//    }
}




