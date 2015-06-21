package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class ServletStart_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "servletStart";

    public ServletStart_NodeInstance() {
    }

    public ServletStart_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
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
