package org.agilewiki.console.roles.system;

import org.agilewiki.console.awdb.AwDb;
import org.agilewiki.console.VCJournalEntry_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

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
