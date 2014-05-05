package e.jbully.core.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {

    protected String nodeId;
    protected int priority;
    protected String correlationId;

    protected Message(String nodeId, int priority, String correlationId) {
        this.nodeId = nodeId;
        this.priority = priority;
        this.correlationId = correlationId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public int getPriority() {
        return priority;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("nodeId='").append(nodeId).append('\'');
        sb.append(", priority=").append(priority);
        sb.append(", correlationId='").append(correlationId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
