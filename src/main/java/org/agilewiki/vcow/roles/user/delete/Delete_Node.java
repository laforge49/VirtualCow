package org.agilewiki.vcow.roles.user.delete;

import org.agilewiki.awdb.Delete;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.VCJournalEntry_Node;

/**
 * Delete a user.
 */
public class Delete_Node extends VCJournalEntry_Node {
    public final static String NAME = "delete";

    public Delete_Node() {
    }

    public Delete_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String id = (String) mapNode.get(NameIds.AN_ID);
        Delete.delete(id);
    }
}
