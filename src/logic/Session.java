package logic;

import data.*;
import data.elements.CarStatusData;
import data.elements.WheelData;
import gui.graph.GraphInt;
import gui.realtime.RealtimeInt;
import gui.statistic.StatisticInterface;
import logic.RaspberryPiInterface;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class Session {
    private RealtimeInt realtimeInt;
    private StatisticInterface statisticInt;
    private GraphInt graphInt;
    private RaspberryPiInterface raspberry;

    public Session(RealtimeInt realtimeInt, StatisticInterface statisticInt, GraphInt graphInt, RaspberryPiInterface raspberry){//todo:other interface
        this.realtimeInt = realtimeInt;
        this.statisticInt = statisticInt;
        this.graphInt = graphInt;
        this.raspberry = raspberry;
        //do something
    }

    public void handleMotionData(PacketMotionData packet){

    }
    public void handleSessionData(PacketSessionData packet){
        float totalLapDistance = packet.getTrackLength();
        graphInt.setTrackDistance((int) totalLapDistance);

        boolean statusGame = packet.isNetworkGame();
        boolean paused = packet.isGamePaused();
        //raspberry.setTSAL(statusGame, paused); //uncomment when using the raspberry pi

    }
    public void handleLapData(PacketLapData packet){
        float currentLapTime=packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getCurrentLapTime();
        realtimeInt.setLapTime((int)currentLapTime);
        int currentLap = packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getCurrentLapNum();
        graphInt.setLapNumber(currentLap);
        float currentDistance = packet.getLapDataList().get(packet.getHeader().getPlayerCarIndex()).getLapDistance();
        graphInt.setCurrentDistance((int) currentDistance);

    }
    public void handleCarSetupData(Packet packet){

    }
    public void handleCarStatusData(PacketCarStatusData packet){
        float fuelCapacity = packet.getCarStatuses().get(packet.getHeader().getPlayerCarIndex()).getFuelCapacity();
        float currentFuel = packet.getCarStatuses().get(packet.getHeader().getPlayerCarIndex()).getFuelInTank();
        realtimeInt.setSocBar(fuelCapacity,currentFuel);

    }
    public void handleCarTelemetryData(PacketCarTelemetryData packet){
        float speed=packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getSpeed();
        realtimeInt.setSpeedLabel((int)speed);

        boolean drs = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).isDrs();
        realtimeInt.setDrsIndicator(drs);

        int amountBrake = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getBrake(); //gives amount of brake between 0 and 100
        realtimeInt.setRegenIndicator(amountBrake);

        graphInt.setCurrentSpeed((int) speed);
        WheelData<Float> tire = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getTirePressure();
        int fl = Math.round(tire.getFrontLeft());
        int fr = Math.round(tire.getFrontRight());
        int bl = Math.round(tire.getRearLeft());
        int br = Math.round(tire.getRearRight());
        realtimeInt.setTyrePressure(fr, fl, br, bl);
        statisticInt.setTyrePressure(fr, fl, br, bl);
        int throttle = packet.getCarTelemetryData().get(packet.getHeader().getPlayerCarIndex()).getThrottle();
        graphInt.setCurrentThrottle(throttle);

    }
    public void handleEventData(PacketEventData packet){

    }
    public void handleParticipantsData(Packet packet){

    }

}

