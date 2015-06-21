package org.agilewiki.console.oodb.nodes.roles.user.delete;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class Delete_Node extends JournalEntry_Node {
    private static Delete_Node delete_node;

    public static void create() {
        delete_node = new Delete_Node("$ndelete.node", "$njournalEntry.node");
    }

    public Delete_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new Delete_NodeInstance(nodeId, factoryId);
    }
}
