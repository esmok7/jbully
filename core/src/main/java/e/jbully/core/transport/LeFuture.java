package e.jbully.core.transport;

import e.jbully.core.messages.Message;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LeFuture<T extends Message, R extends Message> {

    private final static Logger log = Logger.getLogger(LeFuture.class);
    private CountDownLatch latch;
    private T message;
    private List<R> responses = new ArrayList<R>();
    private long timeoutMills;
    private Status status = Status.PENDING;


    public LeFuture(T message, int noOfNodes, long timeoutMills) {
        this.latch = new CountDownLatch(noOfNodes);
        this.message = message;
        this.timeoutMills = timeoutMills;
    }

    public void onMessage(R r) {
        if (r.getCorrelationId().equals(message.getCorrelationId())) {
            responses.add(r);
            latch.countDown();
        } else {
            log.warn("Correlation id of received message [" + r + "] does not match to current monitoring correlation id of message["
                    + message + "]. Response may be time out");
        }
    }

    public void await() throws InterruptedException {
        latch.await(timeoutMills, TimeUnit.MILLISECONDS);
        status = Status.COMPLETE;
    }

    public List<R> getResponses() {
        return responses;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        PENDING,
        COMPLETE
    }
}
