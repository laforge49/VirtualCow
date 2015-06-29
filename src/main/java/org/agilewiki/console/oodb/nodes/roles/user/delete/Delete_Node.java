package org.agilewiki.console.oodb.nodes.roles.user.delete;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Delete_Node extends JournalEntry_Node {
    private static Delete_Node delete_node;
    public final static String ID = "$ndelete.node";

    public static Delete_Node get() {
        return delete_node;
    }

    public static void create() {
        delete_node = new Delete_Node(ID, FactoryRegistry.MAX_TIMESTAMP);
    }

    public Delete_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Delete_NodeInstance(nodeId, timestamp);
    }
}
