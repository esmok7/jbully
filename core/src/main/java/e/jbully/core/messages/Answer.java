package e.jbully.core.messages;

import e.jbully.core.NodeStateMachine;

public class Answer extends Message {

    private NodeStateMachine.NodeStatus status;

    public Answer(String nodeId, int priority, String correlationId, NodeStateMachine.NodeStatus status) {
        super(nodeId, priority, correlationId);
        this.status = status;
    }

    public NodeStateMachine.NodeStatus getStatus() {
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