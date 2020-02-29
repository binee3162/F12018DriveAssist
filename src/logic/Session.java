package logic;

import data.Packet;
import data.PacketCarTelemetryData;
import data.gui.realtime.RealtimeInt;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class Session {
    private RealtimeInt realtimeInt;


    public Session(RealtimeInt realtimeInt){//todo:other interface
        this.realtimeInt=realtimeInt;
        //do something
    }

    public void handleMotionData(Packet packet){

    }
    public void handleSessionData(Packet packet){

    }
    public void handleLapData(Packet packet){

    }
    public void handleCarSetupData(Packet packet){

    }
    public void handleCarStatusData(Packet packet){

    }
    public void handleCarTelemetryData(PacketCarTelemetryData packet){
        float speed=packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getSpeed();
        log.println( "speed: "+speed);
        realtimeInt.setSpeedLabel((int)speed);


    }
    public void handleEventData(Packet packet){

    }
    public void handleParticipantsData(Packet packet){

    }

}

