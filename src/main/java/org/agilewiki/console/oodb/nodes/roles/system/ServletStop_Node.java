package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class ServletStop_Node extends JournalEntry_Node {
    private static ServletStop_Node servletStop_node;

    public static ServletStop_Node get() {
        return servletStop_node;
    }

    public static void create() {
        servletStop_node = new ServletStop_Node("$nservletStop.node", "$njournalEntry.node");
    }

    public ServletStop_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new ServletStop_NodeInstance(nodeId, factoryId);
    }
}
