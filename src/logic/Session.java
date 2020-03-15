package logic;

import data.*;
import gui.realtime.RealtimeInt;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class Session {
    private RealtimeInt realtimeInt;


    public Session(RealtimeInt realtimeInt){//todo:other interface
        this.realtimeInt=realtimeInt;
        //do something
    }

    public void handleMotionData(PacketMotionData packet){

    }
    public void handleSessionData(Packet packet){

    }
    public void handleLapData(PacketLapData packet){
        float currentLapTime=packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getCurrentLapTime();
        log.println( "LapTime: "+currentLapTime);

        realtimeInt.setLapTime(""+currentLapTime);

    }
    public void handleCarSetupData(Packet packet){

    }
    public void handleCarStatusData(Packet packet){

    }
    public void handleCarTelemetryData(PacketCarTelemetryData packet){
        float speed=packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getSpeed();
        log.println( "speed: "+speed);
       // realtimeInt.setSpeedLabel((int)speed);
        realtimeInt.setSpeed((int)speed);
        log.println( "hello\n ");



    }
    public void handleEventData(PacketEventData packet){

    }
    public void handleParticipantsData(Packet packet){

    }

}

