package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.nodes.roles.RoleBase;

import java.util.Map;

/**
 * System role represents the general infrastructure.
 */
public class SystemRole extends RoleBase {

    public SystemRole(SimpleSimon simpleSimon, String roleId, Map<String, String> parameters) {
        super(simpleSimon, roleId, parameters);

        RecreateSystemRole_Node.create();
        simpleSimon.db.registerTransaction(RecreateSystemRole_NodeInstance.NAME, RecreateSystemRole_NodeInstance.class);
        RecreateSystemRole_NodeInstance.systemRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateSystemRole_NodeInstance.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return null;
    }
}
