package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

/**
 * Run when the servlet has started.
 */
public class ServletStop_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "servletStop";

    public ServletStop_NodeInstance() {
    }

    public ServletStop_NodeInstance(String nodeId) {
        super(nodeId);
    }

    public static String update(OODb ooDb)
            throws Exception {
        MapNode mn = ooDb.nilMap;
        return ooDb.update(NAME, mn).call();
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
    }
}
