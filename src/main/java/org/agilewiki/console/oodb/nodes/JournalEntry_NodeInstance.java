package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.User;
import org.agilewiki.console.oodb.NodeBase;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

/**
 * Base class for all virtual cow transactions.
 */
public class JournalEntry_NodeInstance extends NodeBase implements Transaction {

    public JournalEntry_NodeInstance() {
        super(null);
    }

    public JournalEntry_NodeInstance(String nodeId) {
        super(nodeId);
    }

    @Override
    public final void transform(Db db, MapNode tMapNode) {
        String transactionName = tMapNode.get(Db.transactionNameId).toString();
        ooDb.createSecondaryId(db.getJEName(), Key_Node.NODETYPE_ID,
                NameId.generate(transactionName + ".node"));
        setNodeId(db.getJEName());
        String userId = (String) tMapNode.get(User.USER_KEY);
        if (userId != null) {
            ooDb.createLnk1(getNodeId(), User.USER_KEY, userId);
        }
        process(db, tMapNode);
        getOODb().addNode(getNodeId(), this);
    }

    public void process(Db db, MapNode tMapNode) {
        throw new UnsupportedOperationException();
    }
}
