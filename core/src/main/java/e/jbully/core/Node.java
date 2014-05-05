package e.jbully.core;

public class Node {

    private String id;
    private String ip;
    private int priority;

    public Node(String id, String ip, int priority) {
        this.id = id;
        this.ip = ip;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPriority() {
        return priority;
    }
}