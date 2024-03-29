package org.agilewiki.vcow.roles.admin.editRoles;

import org.agilewiki.awdb.db.immutable.collections.ListNode;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.VCJournalEntry_Node;

import java.util.List;

public class UpdateRoles_Node extends VCJournalEntry_Node {
    public final static String NAME = "updateRoles";

    public UpdateRoles_Node() {
    }

    public UpdateRoles_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String nodeId = mapNode.get(NameIds.NODE_ID).toString();
        ListNode ln = mapNode.getList(NameIds.ADDROLES_ID);
        if (ln != null) {
            List<String> add = ln.flatList();
            for (String userRole : add) {
                getAwDb().createSecondaryId(nodeId, NameIds.ROLE_ID, NameIds.generate(userRole));
            }
        }
        ln = mapNode.getList(NameIds.REMOVEROLES_ID);
        if (ln != null) {
            List<String> remove = ln.flatList();
            for (String userRole : remove) {
                getAwDb().removeSecondaryId(nodeId, NameIds.ROLE_ID, NameIds.generate(userRole));
            }
        }
    }
}
