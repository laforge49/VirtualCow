package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.RecreateRoleTransaction;
import org.agilewiki.console.User;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

import java.util.Map;

/**
 * Base class for all virtual cow transactions.
 */
public class JournalEntry_Node extends Node_Node implements Transaction {

    public JournalEntry_Node() {
        super(null, null, null);
    }

    public JournalEntry_Node(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    @Override
    public final void transform(Db db, MapNode tMapNode) {
        String transactionName = tMapNode.get(Db.transactionNameId).toString();
        String secondaryId = SecondaryId.secondaryId(RecreateRoleTransaction.NODETYPE_ID,
                NameId.generate(transactionName + ".node"));
        SecondaryId.createSecondaryId(db, db.getJEName(), secondaryId);
        nodeId = db.getJEName();
        String userId = (String) tMapNode.get(User.USER_KEY);
        if (userId != null) {
            Link1Id.createLink1(db, nodeId, User.USER_KEY, userId);
        }
        process(db, tMapNode);
        getOODb().addNode(nodeId, this);
    }

    public void process(Db db, MapNode tMapNode) {
        throw new UnsupportedOperationException();
    }
}
