package org.agilewiki.console.oodb.nodes.roles.visitor.login;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class BadUserAddress_Node extends JournalEntry_Node {
    public final static String ID = "$nbadUserAddress.node";

    public static void create(OODb ooDb) {
        ooDb.addImmutableNode(new BadUserAddress_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public BadUserAddress_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new BadUserAddress_NodeInstance(nodeId, timestamp);
    }
}
