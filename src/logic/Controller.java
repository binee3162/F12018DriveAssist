package logic;

import data.*;
import gui.graph.GraphInt;
import gui.realtime.RealtimeInt;
import gui.statistic.StatisticInterface;
import logic.receiver.Receiver;

import java.io.IOException;

public class Controller {
    private Receiver receiver;
    private Session session;
    private RealtimeInt realtimeInt;
    private StatisticInterface statisticInt;
    private GraphInt graphInt;
    private RaspberryPiInterface raspberry;

    public Controller() throws IOException {
        realtimeInt=new RealtimeInt("Realtime Interface");
        statisticInt = new StatisticInterface();
        graphInt = new GraphInt("Graph Interface");
        raspberry = new RaspberryPiInterface();
        realtimeInt.setVisible(true);//todo:more neat
        statisticInt.setVisible(true);
        graphInt.setVisible((true));

        receiver=new Receiver(this);
        session=new Session(realtimeInt, statisticInt, graphInt, raspberry);  //todo:other interface
        receiver.receivePacket();
    }

    public void newPacket(Packet packet){
        if(packet!=null){
            switch (packet.getHeader().getPacketId()){

                case 0:
                    session.handleMotionData((PacketMotionData) packet);
                    break;
                case 1:
                    session.handleSessionData((PacketSessionData) packet);
                    break;
                case 2:
                    session.handleLapData((PacketLapData) packet);
                    break;
                case 3:
                    session.handleEventData((PacketEventData) packet);
                    break;
                case 4:
                    session.handleParticipantsData((PacketParticipantsData) packet);
                    break;
                case 5:
                    session.handleCarSetupData((PacketCarSetupData) packet);
                    break;
                case 6:
                    session.handleCarTelemetryData((PacketCarTelemetryData) packet);
                    break;
                case 7:
                    session.handleCarStatusData((PacketCarStatusData) packet);
                    break;

            }

        }
        //todo:
    }

    public static void main(String[] args) throws IOException {
        Controller controller=new Controller();
    }

}
