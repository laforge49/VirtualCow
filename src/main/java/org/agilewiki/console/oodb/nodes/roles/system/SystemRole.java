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

        getOODb().registerNodeFactory("recreateSystemRole.node", new RecreateSystemRoleTransactionFactory());
        simpleSimon.db.registerTransaction(RecreateSystemRoleTransaction.NAME, RecreateSystemRoleTransaction.class);
        RecreateSystemRoleTransaction.systemRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateSystemRoleTransaction.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return null;
    }
}
