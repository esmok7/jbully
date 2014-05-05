package e.jbully.core;

import java.util.concurrent.atomic.AtomicReference;

public class NodeStateMachine {

    private static final NodeStateMachine instance = new NodeStateMachine();
    private AtomicReference<NodeStatus> currentStatus = new AtomicReference<NodeStatus>(NodeStatus.ALIVE);

    private NodeStateMachine() {

    }

    public static NodeStateMachine getInstance() {
        return instance;
    }

    public void markAlive() {
        currentStatus.get().markAlive();
    }

    public void markLeader() {
        currentStatus.get().markLeader();
    }

    public void markDead() {
        currentStatus.get().markDead();
    }

    public NodeStatus getCurrentStatus() {
        return currentStatus.get();
    }

    public enum NodeStatus {
        LEADER,
        ALIVE,
        DEAD {
            public void markAlive() {
                //nothing to do
            }

            public void markLeader() {
                //nothing to do
            }
        };

        public void markAlive() {
            instance.currentStatus.set(ALIVE);
        }

        public void markLeader() {
            instance.currentStatus.set(LEADER);
        }

        public void markDead() {
            instance.currentStatus.set(DEAD);
        }
    }
}