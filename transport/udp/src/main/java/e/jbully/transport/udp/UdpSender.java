package e.jbully.transport.udp;

import e.jbully.core.Node;
import e.jbully.core.NodeRepository;
import e.jbully.core.messages.*;
import e.jbully.core.transport.Sender;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender implements Sender {

    private final static Logger log = Logger.getLogger(UdpSender.class);
    private NodeRepository nodeRepository;
    private int port;

    @Override
    public void inquire(Inquiry inquiry) throws IOException {
        log.info("Sending inquiry to all nodes [" + inquiry + "]");
        byte[] data = convert(inquiry);

        for (Node n : nodeRepository.getAll()) {
            send(n.getIp(), data);
        }
    }

    @Override
    public void declareVictory(Victory victory) throws IOException {
        log.info("Sending victory to all nodes [" + victory + "]");
        byte[] data = convert(victory);

        for (Node n : nodeRepository.getAll()) {
            send(n.getIp(), data);
        }
    }

    @Override
    public void sendDeathNotification(DeathNotification deathNotification) throws IOException {
        log.info("Sending death notification to all nodes [" + deathNotification + "]");
        byte[] data = convert(deathNotification);

        for (Node n : nodeRepository.getAll()) {
            send(n.getIp(), data);
        }
    }

    @Override
    public void reply(Answer answer, String toIp) throws IOException {
        log.info("Sending answer [" + answer + "] to ip [" + toIp + "]");
        byte[] data = convert(answer);
        send(toIp, data);
    }

    @Override
    public void reply(VictoryAck ack, String toIp) throws IOException {
        log.info("Sending reply [" + ack + "] to ip [" + toIp + "]");
        byte[] data = convert(ack);
        send(toIp, data);
    }

    private byte[] convert(Message m) throws IOException {
        ByteArrayOutputStream bos = null;
        ObjectOutput out = null;
        try {
            bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(m);
            return bos.toByteArray();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                log.warn("Error occurred while converting message to byte array [" + e.getMessage() + "]");
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                log.warn("Error occurred while converting message to byte array [" + e.getMessage() + "]");
            }
        }
    }


    private void send(String ip, byte[] data) throws IOException {
        DatagramSocket s = null;
        try {
            DatagramPacket req = new DatagramPacket(data, data.length,
                    InetAddress.getByName(ip), port);
            s = new DatagramSocket();
            s.send(req);
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (Exception e) {
                log.warn("Error occurred while closing the client data socket [" + e.getMessage() + "]");
            }
        }
    }

    public void setNodeRepository(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
