package e.jbully.core.util;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class CorrelationIdGenerator {
    private static final AtomicInteger i = new AtomicInteger(0);

    public static String generate() {
        return new StringBuilder("" + System.currentTimeMillis()).append(new DecimalFormat("###")
                .format(i.getAndIncrement())).toString();
    }
}
