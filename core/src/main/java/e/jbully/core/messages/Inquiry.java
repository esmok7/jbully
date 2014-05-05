package e.jbully.core.messages;

public class Inquiry extends Message {
    public Inquiry(String nodeId, int priority, String correlationId) {
        super(nodeId, priority, correlationId);
    }
}
