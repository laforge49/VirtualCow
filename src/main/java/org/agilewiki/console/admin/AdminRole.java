package org.agilewiki.console.admin;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.RoleBase;
import org.agilewiki.console.SimpleSimon;

/**
 * A base role.
 */
public class AdminRole extends RoleBase {

    private AdminBlade adminBlade;
    private EmailAddressesBlade emailAddressesBlade;
    private UserBlade userBlade;
    private EditRolesBlade editRolesBlade;
    private RecreateRoleBlade recreateRoleBlade;

    public AdminRole(SimpleSimon simpleSimon, String roleName)
            throws Exception {
        super(simpleSimon, roleName);

        adminBlade = new AdminBlade(this, "admin");
        emailAddressesBlade = new EmailAddressesBlade(this, "emailAddresses");
        userBlade = new UserBlade(this, "user");
        editRolesBlade = new EditRolesBlade(this, "editRoles");
        recreateRoleBlade = new RecreateRoleBlade(this, "recreateRole");

        simpleSimon.db.registerTransaction(RecreateAdminRoleTransaction.NAME, RecreateAdminRoleTransaction.class);
        RecreateAdminRoleTransaction.adminRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateAdminRoleTransaction.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return adminBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "admin";
    }

    @Override
    public String niceRoleName() {
        return "Admin";
    }

    @Override
    public void menuItems(StringBuilder sb,
                          String currentPage,
                          String setTimestamp,
                          String timestamp,
                          String setRole) {
        menuItem(sb, currentPage, setTimestamp, setRole, adminBlade);
        menuItem(sb, currentPage, setTimestamp, setRole, emailAddressesBlade);
        menuItem(sb, currentPage, setTimestamp, setRole, recreateRoleBlade, timestamp != null);
    }
}
