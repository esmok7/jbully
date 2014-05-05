package e.jbully.core.procedures;

import org.apache.log4j.Logger;

public class InquiryDaemon extends Thread {

    private final static Logger log = Logger.getLogger(InquiryDaemon.class);
    private long initialDelay;
    private long interval;
    private InquiryProcedure inquiryProcedure;

    public void init() {

        this.start();

    }

    @Override
    public void run() {
        try {
            Thread.sleep(initialDelay);
        } catch (InterruptedException e) {
        }

        while (!Thread.interrupted()) {
            try {
                inquiryProcedure.startInquiry();
            } catch (Exception e) {
                log.warn("Error occurred in inquiry procedure", e);
            } finally {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setInquiryProcedure(InquiryProcedure inquiryProcedure) {
        this.inquiryProcedure = inquiryProcedure;
    }
}
