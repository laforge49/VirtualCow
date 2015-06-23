package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class ServletStop_Node extends JournalEntry_Node {
    private static ServletStop_Node servletStop_node;
    public final static String ID = "$nservletStop.node";

    public static ServletStop_Node get() {
        return servletStop_node;
    }

    public static void create() {
        servletStop_node = new ServletStop_Node(ID);
    }

    public ServletStop_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new ServletStop_NodeInstance(nodeId);
    }
}
