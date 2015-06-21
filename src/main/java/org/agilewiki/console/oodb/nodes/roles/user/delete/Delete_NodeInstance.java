package org.agilewiki.console.oodb.nodes.roles.user.delete;

import org.agilewiki.console.Delete;
import org.agilewiki.console.NameIds;
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

    public Delete_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String id = (String) mapNode.get(NameIds.AN_ID);
        Delete.delete(db, id);
    }
}
