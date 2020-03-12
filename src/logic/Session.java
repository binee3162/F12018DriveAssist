package logic;

import data.*;
import data.elements.WheelData;
import gui.graph.GraphInt;
import gui.realtime.RealtimeInt;
import gui.statistic.StatisticInterface;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class Session {
    private RealtimeInt realtimeInt;
    private StatisticInterface statisticInt;
    private GraphInt graphInt;

    public Session(RealtimeInt realtimeInt, StatisticInterface statisticInt, GraphInt graphInt){//todo:other interface
        this.realtimeInt = realtimeInt;
        this.statisticInt = statisticInt;
        this.graphInt = graphInt;
        //do something
    }

    public void handleMotionData(PacketMotionData packet){

    }
    public void handleSessionData(PacketSessionData packet){
        float totalLapDistance = packet.getTrackLength();
        graphInt.setTrackDistance((long) totalLapDistance);

    }
    public void handleLapData(PacketLapData packet){
        float currentLapTime=packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getCurrentLapTime();
        log.println( "LapTime: "+currentLapTime);

        realtimeInt.setLapTime(""+currentLapTime);
        int currentLap = packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getCurrentLapNum();
        graphInt.setLapNumber(currentLap);
        float currentDistance = packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getLapDistance();
        graphInt.setCurrentDistance((long) currentDistance);

    }
    public void handleCarSetupData(Packet packet){

    }
    public void handleCarStatusData(Packet packet){

    }
    public void handleCarTelemetryData(PacketCarTelemetryData packet){
        float speed=packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getSpeed();
        log.println( "speed: "+speed);
        realtimeInt.setSpeedLabel((int)speed);
        graphInt.setCurrentSpeed((int) speed);
        WheelData<Float> tire = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getTirePressure();
        realtimeInt.setTyrePressure(3.3f, 6.6f);
        int throttle = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getThrottle();
        graphInt.setCurrentThrottle(throttle);

    }
    public void handleEventData(PacketEventData packet){

    }
    public void handleParticipantsData(Packet packet){

    }

}

