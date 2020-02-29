package logic.receiver;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import logic.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import data.Packet;

/**
 * Receiver class uses non-blocking IO to read data and forming packet from
 * udp stream. These packets is sent to controller though a parallel thread
 * for further packet-type specified process
 *
 * @author Song
 *
 */
public class Receiver {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";
    private static final int DEFAULT_PORT = 20777;
    private static final int MAX_PACKET_SIZE = 1341;

    private String bindAddress;
    private int port;
    private Consumer<Packet> packetConsumer;
    private Controller controller;

    public Receiver(){
        bindAddress = DEFAULT_BIND_ADDRESS;
        port = DEFAULT_PORT;
    }

    public Receiver(Controller controller) {
        this.controller=controller;
        bindAddress = DEFAULT_BIND_ADDRESS;
        port = DEFAULT_PORT;
    }

    /**
     * Create an instance of the UDP server
     *
     * @return
     */
    public static Receiver create() {
        return new Receiver();
    }

    /**
     * Set the bind address
     *
     * @param bindAddress
     * @return the server instance
     */
    public Receiver bindTo(String bindAddress) {
        this.bindAddress = bindAddress;
        return this;
    }

    /**
     * Set the bind port
     *
     * @param port
     * @return the server instance
     */
    public Receiver onPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Set the consumer via a lambda function
     *
     * @param consumer
     * @return the server instance
     */
    public Receiver consumeWith(Consumer<Packet> consumer) {
        packetConsumer = consumer;
        return this; //调用consumer lambda
    }

    /**
     * Start the UDP receiver server
     *
     * @throws IOException           if the server fails to start
     * @throws IllegalStateException if you do not define how the packets should be
     *                               consumed
     */
    public void start() throws IOException {

        if (packetConsumer == null) {
            throw new IllegalStateException("You must define how the packets will be consumed.");
        }

        log.info("F1 2018 - Telemetry UDP Server");

        // Create an executor to process the Packets in a separate thread
        // To be honest, this is probably an over-optimization due to the use of NIO,
        // but it was done to provide a simple way of providing back pressure on the
        // incoming UDP packet handling to allow for long-running processing of the
        // Packet object, if required.
        ExecutorService executor = Executors.newSingleThreadExecutor(); //创建线程

        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.socket().bind(new InetSocketAddress(bindAddress, port));
            log.info("Listening on " + bindAddress + ":" + port + "...");
            ByteBuffer buf = ByteBuffer.allocate(MAX_PACKET_SIZE);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            while (true) {
                channel.receive(buf);
                final Packet packet = PacketDeserializer.read(buf.array());//归类packet
                executor.submit(() -> {
                    packetConsumer.accept(packet);
                });
                buf.clear();
            }
        } finally {
            executor.shutdown();
        }
    }
    /**
     * Sending packets to controller using lambda
     * @throws IOException
     */

    public void receivePacket() throws IOException{
        this.bindTo("0.0.0.0")
                .onPort(20777)
                .consumeWith((p) -> {
                    controller.newPacket(p);//send packet to controller
                })
                .start();
    }

    /**
     * Main class in case you want to run the server independently. Uses defaults
     * for bind address and port, and just logs the incoming packets as a JSON
     * object to the location defined in the logback config
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

       // RealtimeInt realtimeInt =new RealtimeInt("Realtime Interface");
       // realtimeInt.setVisible(true);
        //realtimeInt.setSpeedLabel(300);
        Receiver.create()
                .bindTo("0.0.0.0")
                .onPort(20777)
                .consumeWith((p) -> {
//                    //log.trace(p.toJSON());   //lambda定义consume函数
//                    //p.setInterface(realtimeInt);
//                    p.demo();
                })
                .start();
    }

}

