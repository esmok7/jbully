package e.jbully.core.transport;

import e.jbully.core.NodeRepository;
import e.jbully.core.messages.*;
import e.jbully.core.procedures.InquiryProcedure;
import e.jbully.core.procedures.MessageHandler;
import e.jbully.core.procedures.impl.AggregatingMessageHandler;
import e.jbully.core.procedures.impl.DeathNotifyMessageHandler;
import e.jbully.core.procedures.impl.InquiryMessageHandler;
import e.jbully.core.procedures.impl.VictoryMessageHandler;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MessageListener {

    private final static MessageListener instance = new MessageListener();
    private final static Logger log = Logger.getLogger(MessageListener.class);

    private Map<Class, MessageHandler> handlers = new HashMap<Class, MessageHandler>();
    private NodeRepository nodeRepository;
    private Sender sender;
    private long responseAggregationTimeout;
    private InquiryProcedure inquiryProcedure;

    public void init() {
        handlers.put(Answer.class, new AggregatingMessageHandler<Answer, Inquiry>(nodeRepository, responseAggregationTimeout));
        handlers.put(VictoryAck.class, new AggregatingMessageHandler<VictoryAck, Victory>(nodeRepository, responseAggregationTimeout));
        handlers.put(Inquiry.class, new InquiryMessageHandler(nodeRepository, sender));
        handlers.put(Victory.class, new VictoryMessageHandler(nodeRepository, sender));
        handlers.put(DeathNotification.class, new DeathNotifyMessageHandler(inquiryProcedure, nodeRepository));
    }

    public MessageHandler getHandler(Class clazz) {
        return handlers.get(clazz);
    }

    public void onMessage(Message o) {
        MessageHandler handler = handlers.get(o.getClass());

        if (null == handler) {
            log.warn("Couldn't find handler for message - " + o);
        } else {
            handler.onMessage(o);
        }
    }

    public void setNodeRepository(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public void setResponseAggregationTimeout(long responseAggregationTimeout) {
        this.responseAggregationTimeout = responseAggregationTimeout;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setInquiryProcedure(InquiryProcedure inquiryProcedure) {
        this.inquiryProcedure = inquiryProcedure;
    }
}
