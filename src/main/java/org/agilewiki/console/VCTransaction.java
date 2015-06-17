package org.agilewiki.console;

import org.agilewiki.console.oodb.Node;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all virtual cow transactions.
 */
public abstract class VCTransaction implements Transaction, Node {
    private String nodeId;
    private Map<String, String> parameters = new HashMap<String, String>();
    private String factoryId = null;
    private long longTimestamp;

    public VCTransaction() {
    }

    public VCTransaction(String nodeId, Map<String, String> parameters, String factoryId, long longTimestamp) {
        this.nodeId = nodeId;
        this.parameters = parameters;
        this.factoryId = factoryId;
        this.longTimestamp = longTimestamp;
        getOODb().addNode(nodeId, this);
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String getFactoryId() {
        return factoryId;
    }

    @Override
    public long getLongTimestamp() {
        return longTimestamp;
    }

    @Override
    public final void transform(Db db, MapNode tMapNode) {
        longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
        String transactionName = tMapNode.get(Db.transactionNameId).toString();
        String secondaryId = SecondaryId.secondaryId(RecreateRoleTransaction.NODETYPE_ID,
                NameId.generate(transactionName + ".node"));
        SecondaryId.createSecondaryId(db, db.getJEName(), secondaryId);
        String userId = (String) tMapNode.get(User.USER_KEY);
        if (userId != null) {
            nodeId = db.getJEName();
            Link1Id.createLink1(db, nodeId, User.USER_KEY, userId);
        }
        process(db, tMapNode);
        getOODb().addNode(nodeId, this);
    }

    public abstract void process(Db db, MapNode tMapNode);
}
