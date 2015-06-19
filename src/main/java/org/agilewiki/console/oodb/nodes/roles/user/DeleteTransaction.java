package org.agilewiki.console.oodb.nodes.roles.user;

import org.agilewiki.console.Delete;
import org.agilewiki.console.NameIds;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.Map;

/**
 * Delete a user.
 */
public class DeleteTransaction extends JournalEntry_Node {
    public final static String NAME = "delete";

    public DeleteTransaction() {
    }

    public DeleteTransaction(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String id = (String) mapNode.get(NameIds.AN_ID);
        Delete.delete(db, id);
    }
}
