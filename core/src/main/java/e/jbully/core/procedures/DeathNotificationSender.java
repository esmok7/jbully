package e.jbully.core.procedures;

import e.jbully.core.Node;
import e.jbully.core.NodeRepository;
import e.jbully.core.messages.DeathNotification;
import e.jbully.core.transport.Sender;
import e.jbully.core.util.CorrelationIdGenerator;
import org.apache.log4j.Logger;

public class DeathNotificationSender {
    private final static Logger log = Logger.getLogger(DeathNotificationSender.class);
    private Sender sender;
    private NodeRepository nodeRepository;

    public void send() {
        try {
            Node me = nodeRepository.getMe();
            final DeathNotification dn = new DeathNotification(me.getId(), me.getPriority(), CorrelationIdGenerator.generate());
            sender.sendDeathNotification(dn);
            log.info("Death notification send [" + dn + "]");
        } catch (Exception e) {
            log.warn("Unable to send death notification due to error", e);
        }
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setNodeRepository(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }
}
