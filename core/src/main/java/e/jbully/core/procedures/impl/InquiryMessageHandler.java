package e.jbully.core.procedures.impl;

import e.jbully.core.Node;
import e.jbully.core.NodeRepository;
import e.jbully.core.NodeStateMachine;
import e.jbully.core.messages.Answer;
import e.jbully.core.messages.Inquiry;
import e.jbully.core.procedures.MessageHandler;
import e.jbully.core.transport.Sender;
import org.apache.log4j.Logger;

public class InquiryMessageHandler implements MessageHandler<Inquiry> {

    private final static Logger log = Logger.getLogger(InquiryMessageHandler.class);
    private NodeRepository nodeRepository;
    private Sender sender;

    public InquiryMessageHandler(NodeRepository nodeRepository, Sender sender) {
        this.nodeRepository = nodeRepository;
        this.sender = sender;
    }

    @Override
    public void onMessage(Inquiry r) {
        try {
            Node to = nodeRepository.find(r.getNodeId());
            if (to == null) {
                log.warn("Unable to find node with id[" + r.getNodeId() + "] when processing inquiry message[" + r
                        + "]. Might be a configuration issue where nodes are defined");
                return;
            }

            final Node me = nodeRepository.getMe();
            final Answer answer = new Answer(me.getId(), me.getPriority(), r.getCorrelationId(),
                    NodeStateMachine.getInstance().getCurrentStatus());
            sender.reply(answer, to.getIp());
        } catch (Exception e) {
            log.warn("Error occurred while processing inquiry [" + r + "]", e);
        }
    }
}
