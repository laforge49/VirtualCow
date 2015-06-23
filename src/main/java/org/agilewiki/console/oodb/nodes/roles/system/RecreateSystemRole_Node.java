package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class RecreateSystemRole_Node extends JournalEntry_Node {
    private static RecreateSystemRole_Node recreateSystemRole_node;
    public final static String ID = "$nrecreateSystemRole.node";

    public static RecreateSystemRole_Node get() {
        return recreateSystemRole_node;
    }

    public static void create() {
        recreateSystemRole_node = new RecreateSystemRole_Node(ID);
    }

    public RecreateSystemRole_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new RecreateSystemRole_NodeInstance(nodeId);
    }
}
