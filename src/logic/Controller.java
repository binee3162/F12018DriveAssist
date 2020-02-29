package logic;

import data.*;
import data.gui.realtime.RealtimeInt;
import logic.receiver.Receiver;

import java.io.IOException;

public class Controller {
    private Receiver receiver;
    private Session session;
    private RealtimeInt realtimeInt;

    public Controller() throws IOException {
        realtimeInt=new RealtimeInt("Realtime Interface");
        realtimeInt.setVisible(true);//todo:more neat

        receiver=new Receiver(this);
        session=new Session(realtimeInt);  //todo:other interface
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
