package e.jbully.core.procedures.impl;

import e.jbully.core.NodeRepository;
import e.jbully.core.messages.Message;
import e.jbully.core.procedures.MessageHandler;
import e.jbully.core.transport.LeFuture;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class AggregatingMessageHandler<R extends Message, T extends Message> implements MessageHandler<R> {

    private final static Logger log = Logger.getLogger(AggregatingMessageHandler.class);
    private NodeRepository repository;
    private long responseAggregationTimeout;
    private Map<Class, LeFuture<T, R>> aggregatorFutureMap = new HashMap<Class, LeFuture<T, R>>();

    public AggregatingMessageHandler(NodeRepository repository, long responseAggregationTimeout) {
        this.repository = repository;
        this.responseAggregationTimeout = responseAggregationTimeout;
    }

    @Override
    public void onMessage(R r) {
        LeFuture<T, R> f = aggregatorFutureMap.get(r.getClass());

        if (null == f) {
            log.warn("Couldn't find aggregator for message[" + r.getClass() + "]. Message is[" + r + "]");
            return;
        }

        f.onMessage(r);
    }

    public LeFuture<T, R> getResponse(T t, Class clazzR) {
        LeFuture<T, R> existing = aggregatorFutureMap.get(clazzR);

        if (null != existing && existing.getStatus() == LeFuture.Status.PENDING) {
            log.info("Pending aggregator for [" + clazzR + "] is already available. Will not send message again");
            return null;
        }

        log.debug("Adding LeFuture to aggregator map with key[" + clazzR + "]");
        LeFuture<T, R> f = new LeFuture<T, R>(t, repository.getAll().size(), responseAggregationTimeout);
        aggregatorFutureMap.put(clazzR, f);
        return f;
    }
}
