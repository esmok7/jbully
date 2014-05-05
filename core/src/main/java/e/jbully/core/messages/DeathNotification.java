package e.jbully.core.messages;

public class DeathNotification extends Message {

    public DeathNotification(String nodeId, int priority, String correlationId) {
        super(nodeId, priority, correlationId);
    }
}
