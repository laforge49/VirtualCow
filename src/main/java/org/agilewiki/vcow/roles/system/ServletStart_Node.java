package org.agilewiki.vcow.roles.system;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.db.ids.NameId;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.vcow.SimpleSimon;
import org.agilewiki.vcow.VCJournalEntry_Node;
import org.agilewiki.vcow.VCUser_Node;

public class ServletStart_Node extends VCJournalEntry_Node {
    public final static String NAME = "servletStart";

    public ServletStart_Node() {
    }

    public ServletStart_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    public static String update(AwDb awDb)
            throws Exception {
        MapNode mn = awDb.nilMap;
        mn = mn.set(NameId.RANDOM_SEED, awDb.randomId.generateSeed());
        return SimpleSimon.simpleSimon.awDb.update(NAME, mn).call();
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        VCUser_Node.servletInit();
    }
}
