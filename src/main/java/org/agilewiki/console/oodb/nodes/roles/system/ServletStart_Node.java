package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class ServletStart_Node extends JournalEntry_Node {
    private static ServletStart_Node servletStart_node;
    public final static String ID = "$nservletStart.node";

    public static ServletStart_Node get() {
        return servletStart_node;
    }

    public static void create() {
        servletStart_node = new ServletStart_Node(ID);
    }

    public ServletStart_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new ServletStart_NodeInstance(nodeId);
    }
}
