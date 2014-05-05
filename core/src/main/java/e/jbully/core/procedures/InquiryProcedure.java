package e.jbully.core.procedures;

import e.jbully.core.NodeRepository;
import e.jbully.core.NodeStateMachine;
import e.jbully.core.messages.Answer;
import e.jbully.core.messages.Inquiry;
import e.jbully.core.messages.Victory;
import e.jbully.core.messages.VictoryAck;
import e.jbully.core.procedures.impl.AggregatingMessageHandler;
import e.jbully.core.transport.LeFuture;
import e.jbully.core.transport.MessageListener;
import e.jbully.core.transport.Sender;
import e.jbully.core.util.CorrelationIdGenerator;
import org.apache.log4j.Logger;

import java.io.IOException;

public class InquiryProcedure {

    private final static Logger log = Logger.getLogger(InquiryProcedure.class);
    private Sender sender;
    private NodeRepository nodeRepository;
    private MessageListener messageListener;

    public void startInquiry() {
        try {
            final int myPriority = nodeRepository.getMe().getPriority();
            final Inquiry inquiry = new Inquiry(nodeRepository.getMe().getId(), myPriority, CorrelationIdGenerator.generate());
            AggregatingMessageHandler<Answer, Inquiry> handler = (AggregatingMessageHandler<Answer, Inquiry>)
                    messageListener.getHandler(Answer.class);
            LeFuture<Inquiry, Answer> future = handler.getResponse(inquiry, Answer.class);
            if (null == future) {
                return;
            }

            sender.inquire(inquiry);
            future.await();

            int leaderPriority = -1;
            int highestPriority = -1;

            for (Answer answer : future.getResponses()) {
                if (answer.getStatus() == NodeStateMachine.NodeStatus.LEADER) {
                    leaderPriority = answer.getPriority();
                }

                if (highestPriority < answer.getPriority()) {
                    highestPriority = answer.getPriority();
                }
            }
            if (leaderPriority < myPriority &&
                    highestPriority < myPriority &&
                    NodeStateMachine.getInstance().getCurrentStatus() != NodeStateMachine.NodeStatus.LEADER) {
                log.info("Sending victory message to get elected as leader. Since leader priority found is [" + leaderPriority
                        + "] and highest priority found is [" + highestPriority + "] and my priority is [" + myPriority + "]");
                declareVictory();

            }

        } catch (Exception e) {
            log.warn("Error occurred while sending inquiry message", e);
        }
    }

    private void declareVictory() throws InterruptedException, IOException {
        final int myPriority = nodeRepository.getMe().getPriority();
        final Victory victory = new Victory(nodeRepository.getMe().getId(), myPriority, CorrelationIdGenerator.generate());
        AggregatingMessageHandler<VictoryAck, Victory> handler = (AggregatingMessageHandler<VictoryAck, Victory>)
                messageListener.getHandler(VictoryAck.class);
        LeFuture<Victory, VictoryAck> future = handler.getResponse(victory, VictoryAck.class);
        if (null == future) {
            log.info("Seems like victory request is already sent. Will not send this victory request");
            return;
        }
        sender.declareVictory(victory);
        future.await();

        boolean acceptedByAll = true;
        for (VictoryAck ack : future.getResponses()) {
            if (ack.getStatus() != VictoryAck.Status.ACCEPTED) {
                log.info("Victory is not accepted by [" + ack + "] ");
                acceptedByAll = false;
            }
        }

        if (acceptedByAll) {
            NodeStateMachine.getInstance().markLeader();
            log.info("Node is elected as leader and will continue to send ussd alive requests");
        }
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setNodeRepository(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
