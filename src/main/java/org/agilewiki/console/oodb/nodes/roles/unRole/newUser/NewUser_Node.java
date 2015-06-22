package org.agilewiki.console.oodb.nodes.roles.unRole.newUser;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class NewUser_Node extends JournalEntry_Node {
    private static NewUser_Node newUser_node;
    public final static String ID = "$nnewUser.node";

    public static NewUser_Node get() {
        return newUser_node;
    }

    public static void create() {
        newUser_node = new NewUser_Node(ID, JournalEntry_Node.ID);
    }

    public NewUser_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new NewUser_NodeInstance(nodeId, factoryId);
    }
}
