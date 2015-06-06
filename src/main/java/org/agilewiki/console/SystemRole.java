package org.agilewiki.console;

import org.agilewiki.console.admin.RecreateAdminRoleTransaction;

/**
 * System role represents the general infrastructure.
 */
public class SystemRole extends RoleBase {

    SystemRole(SimpleSimon simpleSimon, String roleName, String niceRoleName) {
        super(simpleSimon, roleName, niceRoleName);

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

    @Override
    public String getDefaultRequestPage() {
        return null;
    }
}
