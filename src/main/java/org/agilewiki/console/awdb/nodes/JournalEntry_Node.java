package org.agilewiki.console.awdb.nodes;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.awdb.NodeBase;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

/**
 * Base class for all virtual cow transactions.
 */
public class JournalEntry_Node extends NodeBase implements Transaction {

    public JournalEntry_Node() {
        super(null, 0L);
    }

    public JournalEntry_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public final void transform(Db db, MapNode tMapNode) {
        initialize(db.getJEName(), FactoryRegistry.MAX_TIMESTAMP);
        String transactionName = tMapNode.get(Db.transactionNameId).toString();
        getAwDb().createSecondaryId(db.getJEName(), Key_NodeFactory.NODETYPE_ID,
                NameId.generate(transactionName + ".node"));
        String userId = (String) tMapNode.get(NameIds.USER_KEY);
        if (userId != null) {
            getAwDb().createLnk1(getNodeId(), NameIds.USER_KEY, userId);
        }
        process(db, tMapNode);
        getAwDb().addNode(this);
    }

    public void process(Db db, MapNode tMapNode) {
        throw new UnsupportedOperationException();
    }
}
