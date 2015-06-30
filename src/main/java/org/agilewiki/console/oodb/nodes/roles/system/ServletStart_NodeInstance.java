package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.console.oodb.nodes.User_NodeInstance;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class ServletStart_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "servletStart";

    public ServletStart_NodeInstance() {
    }

    public ServletStart_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    public static String update(OODb ooDb)
            throws Exception {
        MapNode mn = ooDb.nilMap;
        return SimpleSimon.simpleSimon.ooDb.update(NAME, mn).call();
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        User_NodeInstance.init();
    }
}
