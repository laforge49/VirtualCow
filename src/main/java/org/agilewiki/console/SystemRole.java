package org.agilewiki.console;

import org.agilewiki.console.admin.RecreateAdminRoleTransaction;

/**
 * System role represents the general infrastructure.
 */
public class SystemRole extends RoleBase {

    SystemRole(SimpleSimon simpleSimon, String roleName) {
        super(simpleSimon, roleName);

        simpleSimon.db.registerTransaction(RecreateSystemRoleTransaction.NAME, RecreateSystemRoleTransaction.class);
        RecreateSystemRoleTransaction.systemRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateSystemRoleTransaction.NAME;
    }

    @Override
    public String niceRoleName() {
        return "System";
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
