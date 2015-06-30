package org.agilewiki.console.roles.admin.editRoles;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.awdb.nodes.JournalEntry_NodeInstance;
import org.agilewiki.utils.immutable.collections.ListNode;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.List;

public class UpdateRoles_NodeInstance extends JournalEntry_NodeInstance {
    public final static String NAME = "updateRoles";

    public UpdateRoles_NodeInstance() {
    }

    public UpdateRoles_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String nodeId = mapNode.get(NameIds.NODE_ID).toString();
        ListNode ln = mapNode.getList(NameIds.ADDROLES_ID);
        if (ln != null) {
            List<String> add = ln.flatList();
            for (String userRole : add) {
                getOoDb().createSecondaryId(nodeId, NameIds.ROLE_ID, NameIds.generate(userRole));
            }
        }
        ln = mapNode.getList(NameIds.REMOVEROLES_ID);
        if (ln != null) {
            List<String> remove = ln.flatList();
            for (String userRole : remove) {
                getOoDb().removeSecondaryId(nodeId, NameIds.ROLE_ID, NameIds.generate(userRole));
            }
        }
    }
}
