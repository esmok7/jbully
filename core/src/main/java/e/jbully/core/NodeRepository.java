package e.jbully.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NodeRepository {

    private Map<String, Node> nodeMap = new HashMap<>();
    private Node me;

    public Node find(String id) {
        return nodeMap.get(id);
    }

    public Collection<Node> getAll() {
        return nodeMap.values();
    }

    public Node getMe() {
        return me;
    }

    public void setNodeMap(Map<String, Node> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public void setMe(Node me) {
        this.me = me;
    }
}
