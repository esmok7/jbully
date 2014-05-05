package e.jbully.core.messages;

public class VictoryAck extends Message {

    private Status status;

    public VictoryAck(String nodeId, int priority, String correlationId, Status status) {
        super(nodeId, priority, correlationId);
        this.status = status;
    }

    public enum Status {
        ACCEPTED,
        REJECT
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("nodeId='").append(nodeId).append('\'');
        sb.append(", priority=").append(priority);
        sb.append(", correlationId='").append(correlationId).append('\'');
        sb.append("status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
