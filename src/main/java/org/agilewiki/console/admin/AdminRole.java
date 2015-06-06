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

    public AdminRole(SimpleSimon simpleSimon)
            throws Exception {
        super(simpleSimon);

        adminBlade = new AdminBlade(this, "admin");
        requests.put(adminBlade.page, adminBlade);

        emailAddressesBlade = new EmailAddressesBlade(this, "emailAddresses");
        requests.put(emailAddressesBlade.page, emailAddressesBlade);

        userBlade = new UserBlade(this, "user");
        requests.put(userBlade.page, userBlade);

        editRolesBlade = new EditRolesBlade(this, "editRoles");
        requests.put(editRolesBlade.page, editRolesBlade);
        posts.put(editRolesBlade.page, editRolesBlade);

        recreateRoleBlade = new RecreateRoleBlade(this, "recreateRole");
        requests.put(recreateRoleBlade.page, recreateRoleBlade);
        posts.put(recreateRoleBlade.page, recreateRoleBlade);

        simpleSimon.db.registerTransaction(RecreateAdminRoleTransaction.NAME, RecreateAdminRoleTransaction.class);
        RecreateAdminRoleTransaction.adminRole = this;
    }

    @Override
    public SimpleSimon simpleSimon() {
        return simpleSimon;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateAdminRoleTransaction.NAME;
    }

    @Override
    public String roleName() {
        return "admin";
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
