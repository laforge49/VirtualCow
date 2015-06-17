package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.console.VCTransaction;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.Map;

/**
 * Run when the servlet has started.
 */
public class ServletStartTransaction extends VCTransaction {
    public final static String NAME = "servletStart";

    public ServletStartTransaction() {
    }

    public ServletStartTransaction(String nodeId, Map<String, String> parameters, String factoryId, long longTimestamp) {
        super(nodeId, parameters, factoryId, longTimestamp);
    }

    public static String update(Db db)
            throws Exception {
        MapNode mn = db.dbFactoryRegistry.nilMap;
        return db.update(NAME, mn).call();
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        User.init(SimpleSimon.simpleSimon);
    }
}
