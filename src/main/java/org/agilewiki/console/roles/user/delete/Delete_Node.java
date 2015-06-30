package org.agilewiki.console.roles.user.delete;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.awdb.Delete;
import org.agilewiki.console.VCJournalEntry_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

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
