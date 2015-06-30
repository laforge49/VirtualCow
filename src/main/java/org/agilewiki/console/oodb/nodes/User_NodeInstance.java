package org.agilewiki.console.oodb.nodes;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.NodeBase;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import java.util.ArrayList;
import java.util.List;

public class User_NodeInstance extends NodeBase {
    public User_NodeInstance(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    public List<Role> roles() {
        while (true) {
            try {
                List<Role> roles = new ArrayList<Role>();
                for (String roleId : nodeValueIdIterable(NameIds.ROLE_ID)) {
                    Role role = SimpleSimon.simpleSimon.roles.get(NameId.name(roleId));
                    if (role != null)
                        roles.add(role);
                }
                return roles;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public boolean hasRole(String role) {
        while (true) {
            try {
                String roleId = NameId.generate(role);
                return nodeHasValueId(NameIds.ROLE_ID, roleId);
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }
}
