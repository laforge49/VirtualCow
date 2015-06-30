package org.agilewiki.console.oodb.nodes.roles.user.newEmailAddress;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class NewEmailAddress_Node extends JournalEntry_Node {
    public final static String ID = "$nnewEmailAddress.node";

    public static void create(OODb ooDb) {
        ooDb.addImmutableNode(new NewEmailAddress_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public NewEmailAddress_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new NewEmailAddress_NodeInstance(nodeId, timestamp);
    }
}
