package e.jbully.transport.udp;

import e.jbully.core.messages.Message;
import e.jbully.core.transport.MessageListener;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.*;

public class UdpServer extends Thread {

    private final static Logger log = Logger.getLogger(UdpServer.class);
    private String host;
    private int port;
    private DatagramSocket socket;
    private MessageListener messageListener;

    public void init() throws UnknownHostException, SocketException {
        socket = new DatagramSocket(new InetSocketAddress(InetAddress.getByName(host), port));
        this.start();
    }

    public void shutdown() {
        try {
            socket.close();
        } catch (Exception e) {
            log.info("Error occurred while shutdown udp server [" + e.getMessage() + "]");
        }
    }

    public void run() {
        while (!Thread.interrupted()) {
            ObjectInput in = null;
            try {
                byte[] data = new byte[1024];

                DatagramPacket req = new DatagramPacket(data, data.length);
                socket.receive(req);
                in = new ObjectInputStream(new ByteArrayInputStream(data));
                Message message = (Message) in.readObject();
                log.debug("Ussd leader election message received [" + message + "]");
                messageListener.onMessage(message);
            } catch (Exception e) {
                log.warn("Error occurred in udp server", e);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e) {
                    log.info("Error while trying to close udp input stream", e);
                }
            }
        }

    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
