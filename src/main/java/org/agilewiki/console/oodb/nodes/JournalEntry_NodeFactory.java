package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.console.oodb.NodeFactory;
import org.agilewiki.console.oodb.OODb;

import java.util.HashMap;

/**
 * The meta data node factory.
 */
public class JournalEntry_NodeFactory implements NodeFactory {
    public JournalEntry_NodeFactory(OODb ooDb) {
        ooDb.registerNodeFactory("journalEntry.node", this);
    }

    @Override
    public Node createNode(String nodeId, String factoryId) {
        return new JournalEntry_Node(nodeId, new HashMap(), factoryId);
    }
}
