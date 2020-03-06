package logic;

import data.*;
import data.elements.WheelData;
import gui.realtime.RealtimeInt;
import gui.statistic.StatisticInterface;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class Session {
    private RealtimeInt realtimeInt;
    private StatisticInterface statisticInt;

    public Session(RealtimeInt realtimeInt, StatisticInterface statisticInt){//todo:other interface
        this.realtimeInt = realtimeInt;
        this.statisticInt = statisticInt;
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
        realtimeInt.setSpeedLabel((int)speed);

        WheelData<Float> tire = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getTirePressure();
        //realtimeInt.setTyrePressure((int) tire<1>, );

    }
    public void handleEventData(PacketEventData packet){

    }
    public void handleParticipantsData(Packet packet){

    }

}

