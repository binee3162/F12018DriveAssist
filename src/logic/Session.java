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
        realtimeInt.setLapTime((int) currentLapTime);
    }

    public void handleCarSetupData(Packet packet){

    }
    public void handleCarStatusData(Packet packet){

    }
    public void handleCarTelemetryData(PacketCarTelemetryData packet){
        float speed=packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getSpeed();
        log.println( "speed: "+speed);
        realtimeInt.setSpeedLabel((int)speed);

        boolean drs = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).isDrs();
        realtimeInt.setDrsIndicator(drs);

        int amountBrake = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getBrake(); //gives amount of brake between 0 and 100
        realtimeInt.setRegenIndicator(amountBrake, (int) speed);

        WheelData<Float> tire = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getTirePressure();
        realtimeInt.setTyrePressure(3.3f, 6.6f);

    }
    public void handleEventData(PacketEventData packet){

    }
    public void handleParticipantsData(Packet packet){

    }

}

