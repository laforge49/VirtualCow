package org.agilewiki.console.roles.system;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User_NodeInstance;
import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class ServletStart_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "servletStart";

    public ServletStart_NodeInstance() {
    }

    public ServletStart_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    public static String update(AwDb awDb)
            throws Exception {
        MapNode mn = awDb.nilMap;
        return SimpleSimon.simpleSimon.awDb.update(NAME, mn).call();
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        User_NodeInstance.init();
    }
}
