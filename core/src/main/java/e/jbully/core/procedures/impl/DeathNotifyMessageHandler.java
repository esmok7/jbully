package e.jbully.core.procedures.impl;

import e.jbully.core.Node;
import e.jbully.core.NodeRepository;
import e.jbully.core.messages.DeathNotification;
import e.jbully.core.procedures.InquiryProcedure;
import e.jbully.core.procedures.MessageHandler;
import org.apache.log4j.Logger;

public class DeathNotifyMessageHandler implements MessageHandler<DeathNotification> {

    private final static Logger log = Logger.getLogger(DeathNotifyMessageHandler.class);
    private InquiryProcedure inquiryProcedure;
    private NodeRepository nodeRepository;

    public DeathNotifyMessageHandler(InquiryProcedure inquiryProcedure, NodeRepository nodeRepository) {
        this.inquiryProcedure = inquiryProcedure;
        this.nodeRepository = nodeRepository;
    }

    @Override
    public void onMessage(DeathNotification dn) {
        try {
            final Node me = nodeRepository.getMe();
            if (dn.getPriority() > me.getPriority()) {
                log.info("Node where death notification [" + dn + "] is sent has higher priority. Will initiate inquiry flow to check leader status");
                inquiryProcedure.startInquiry();
            }
        } catch (Exception e) {
            log.warn("Unable to process death notification [" + dn + "] due to error", e);
        }
    }
}
