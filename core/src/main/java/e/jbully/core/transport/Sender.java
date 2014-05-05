package e.jbully.core.transport;

import e.jbully.core.messages.*;

import java.io.IOException;

public interface Sender {

    void inquire(Inquiry inquiry) throws IOException;

    void declareVictory(Victory victory) throws IOException;

    void sendDeathNotification(DeathNotification deathNotification) throws IOException;

    void reply(Answer answer, String toIp) throws IOException;

    void reply(VictoryAck ack, String toIp) throws IOException;

}
