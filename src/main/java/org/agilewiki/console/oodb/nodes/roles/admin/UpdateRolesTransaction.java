package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SecondaryIds;
import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.JournalEntry_Node;
import org.agilewiki.utils.immutable.collections.ListNode;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import java.util.List;
import java.util.Map;

/**
 * Updates the roles of a user.
 */
public class UpdateRolesTransaction extends JournalEntry_Node {
    public final static String NAME = "updateRoles";

    public UpdateRolesTransaction() {
    }

    public UpdateRolesTransaction(String nodeId, Map<String, String> parameters, String factoryId) {
        super(nodeId, parameters, factoryId);
    }

    @Override
    public void process(Db db, MapNode mapNode) {
        String nodeId = mapNode.get("nodeId").toString();
        ListNode ln = mapNode.getList("addRoles");
        if (ln != null) {
            List<String> add = ln.flatList();
            for (String userRole : add) {
                String roleTypeSecondaryId =
                        SecondaryIds.secondaryId(User.ROLE_ID, NameIds.generate(userRole));
                SecondaryIds.createSecondaryId(db, nodeId, roleTypeSecondaryId);
            }
        }
        ln = mapNode.getList("removeRoles");
        if (ln != null) {
            List<String> remove = ln.flatList();
            for (String userRole : remove) {
                String roleTypeSecondaryId =
                        SecondaryIds.secondaryId(User.ROLE_ID, NameIds.generate(userRole));
                SecondaryIds.removeSecondaryId(db, nodeId, roleTypeSecondaryId);
            }
        }
    }
}
