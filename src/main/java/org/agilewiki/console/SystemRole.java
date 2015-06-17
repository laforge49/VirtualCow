package org.agilewiki.console;

import java.util.Map;

/**
 * System role represents the general infrastructure.
 */
public class SystemRole extends RoleBase {

    SystemRole(SimpleSimon simpleSimon, String roleId, Map<String, String> parameterse) {
        super(simpleSimon, roleId, parameterse);

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
