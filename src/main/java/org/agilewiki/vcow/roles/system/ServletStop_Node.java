package org.agilewiki.vcow.roles.system;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.vcow.VCJournalEntry_Node;

/**
 * Run when the servlet has started.
 */
public class ServletStop_Node extends VCJournalEntry_Node {
    public final static String NAME = "servletStop";

    public ServletStop_Node() {
    }

    public ServletStop_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    public static String update(AwDb awDb)
            throws Exception {
        MapNode mn = awDb.nilMap;
        return awDb.update(NAME, mn).call();
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
    }
}
