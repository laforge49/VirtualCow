package org.agilewiki.vcow;

import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.awdb.nodes.JournalEntry_Node;

/**
 * Base class for all virtual cow transactions.
 */
public class VCJournalEntry_Node extends JournalEntry_Node {

    public VCJournalEntry_Node() {
        super(null, 0L);
    }

    public VCJournalEntry_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void transformInit(Db db, MapNode tMapNode) {
        super.transformInit(db, tMapNode);
    }
}
