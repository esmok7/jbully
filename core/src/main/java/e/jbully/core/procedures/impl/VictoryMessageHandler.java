package e.jbully.core.procedures.impl;

import e.jbully.core.Node;
import e.jbully.core.NodeRepository;
import e.jbully.core.NodeStateMachine;
import e.jbully.core.messages.Victory;
import e.jbully.core.messages.VictoryAck;
import e.jbully.core.procedures.MessageHandler;
import e.jbully.core.transport.Sender;
import org.apache.log4j.Logger;

public class VictoryMessageHandler implements MessageHandler<Victory> {

    private final static Logger log = Logger.getLogger(VictoryMessageHandler.class);
    private NodeRepository nodeRepository;
    private Sender sender;

    public VictoryMessageHandler(NodeRepository nodeRepository, Sender sender) {
        this.nodeRepository = nodeRepository;
        this.sender = sender;
    }

    @Override
    public void onMessage(Victory victory) {
        try {
            Node to = nodeRepository.find(victory.getNodeId());
            if (to == null) {
                log.warn("Unable to find node with id[" + victory.getNodeId() + "] when processing victory message["
                        + victory + "]. Might be a configuration issue where nodes are defined");
                return;
            }
            final Node me = nodeRepository.getMe();
            VictoryAck.Status status = VictoryAck.Status.ACCEPTED;

            if (victory.getPriority() < me.getPriority()
                    && NodeStateMachine.getInstance().getCurrentStatus() != NodeStateMachine.NodeStatus.DEAD) {
                status = VictoryAck.Status.REJECT;
            } else {
                NodeStateMachine.getInstance().markAlive();
            }

            log.info("Reply status send for victory message is [" + status + "] correlation id [" + victory.getCorrelationId() + "]");
            VictoryAck ack = new VictoryAck(me.getId(), me.getPriority(), victory.getCorrelationId(), status);

            sender.reply(ack, to.getIp());
        } catch (Exception e) {
            log.warn("Error occurred while processing victory message [" + victory + "]", e);
        }
    }
}
