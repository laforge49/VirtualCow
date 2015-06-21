package org.agilewiki.console.oodb.nodes.roles.user.changePassword;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;

public class ChangePassword_Node extends JournalEntry_Node {
    private static ChangePassword_Node changePassword_node;

    public static void create() {
        changePassword_node = new ChangePassword_Node("$nchangePassword.node", "$njournalEntry.node");
    }

    public ChangePassword_Node(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new ChangePassword_NodeInstance(nodeId, factoryId);
    }
}
