package e.jbully.core.procedures;

import e.jbully.core.messages.Message;

public interface MessageHandler<R extends Message> {

    void onMessage(R r);

}
