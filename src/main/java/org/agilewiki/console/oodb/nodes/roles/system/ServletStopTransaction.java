package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.Map;

/**
 * Run when the servlet has started.
 */
public class ServletStopTransaction extends VCTransaction {
    public final static String NAME = "servletStop";

    public ServletStopTransaction() {
    }

    public ServletStopTransaction(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    public static String update(Db db)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        return db.update(NAME, mn).call();
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
    }
}
