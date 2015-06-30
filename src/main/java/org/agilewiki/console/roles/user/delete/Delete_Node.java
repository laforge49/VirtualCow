package org.agilewiki.console.roles.user.delete;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.FactoryRegistry;

public class Delete_Node extends JournalEntry_Node {
    public final static String ID = "$ndelete.node";

    public static void create(OODb ooDb) {
        ooDb.addTimelessNode(new Delete_Node(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    public Delete_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Node createNode(String nodeId, long timestamp) {
        return new Delete_NodeInstance(nodeId, timestamp);
    }
}
