package org.agilewiki.console.oodb.nodes.roles.user.newEmailAddress;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class NewEmailAddress_Node extends JournalEntry_Node {
    private static NewEmailAddress_Node newEmailAddress_node;
    public final static String ID = "$nnewEmailAddress.node";

    public static NewEmailAddress_Node get() {
        return newEmailAddress_node;
    }

    public static void create() {
        newEmailAddress_node = new NewEmailAddress_Node(ID, JournalEntry_Node.ID);
    }

    public NewEmailAddress_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new NewEmailAddress_NodeInstance(nodeId, factoryId);
    }
}
