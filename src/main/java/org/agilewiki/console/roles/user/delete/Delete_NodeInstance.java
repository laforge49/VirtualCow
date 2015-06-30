package org.agilewiki.console.roles.user.delete;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.oodb.Delete;
import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Delete a user.
 */
public class Delete_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "delete";

    public Delete_NodeInstance() {
    }

    public Delete_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String id = (String) mapNode.get(NameIds.AN_ID);
        Delete.delete(id);
    }
}