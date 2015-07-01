package org.agilewiki.vcow;

import org.agilewiki.awdb.nodes.JournalEntry_Node;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;

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
        String userId = (String) tMapNode.get(NameIds.USER_KEY);
        if (userId != null) {
            getAwDb().createLnk1(getNodeId(), NameIds.USER_KEY, userId);
        }
    }
}
