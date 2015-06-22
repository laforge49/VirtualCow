package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class RecreateUnRole_Node extends JournalEntry_Node {
    private static RecreateUnRole_Node recreateUnRole_node;
    public final static String ID = "$nrecreateUnRole.node";

    public static RecreateUnRole_Node get() {
        return recreateUnRole_node;
    }

    public static void create() {
        recreateUnRole_node = new RecreateUnRole_Node(ID, JournalEntry_Node.ID);
    }

    public RecreateUnRole_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new RecreateUnRole_NodeInstance(nodeId, factoryId);
    }
}
