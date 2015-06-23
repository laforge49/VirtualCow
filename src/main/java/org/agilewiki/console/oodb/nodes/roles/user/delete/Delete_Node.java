package org.agilewiki.console.oodb.nodes.roles.user.delete;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class Delete_Node extends JournalEntry_Node {
    private static Delete_Node delete_node;
    public final static String ID = "$ndelete.node";

    public static Delete_Node get() {
        return delete_node;
    }

    public static void create() {
        delete_node = new Delete_Node(ID);
    }

    public Delete_Node(String nodeId) {
        super(nodeId);
    }

    @Override
    public Node createNode(String nodeId) {
        return new Delete_NodeInstance(nodeId);
    }
}
