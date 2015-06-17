package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.RecreateSystemRoleTransactionFactory;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.RoleBase;
import org.agilewiki.console.SimpleSimon;

import java.util.Map;

/**
 * A base role.
 */
public class AdminRole extends RoleBase {

    private AdminBlade adminBlade;
    private EmailAddressesBlade emailAddressesBlade;
    private UserBlade userBlade;
    private EditRolesBlade editRolesBlade;
    private RecreateRoleBlade recreateRoleBlade;

    public AdminRole(SimpleSimon simpleSimon, String roleId, Map<String, String> parameters)
            throws Exception {
        super(simpleSimon, roleId, parameters);

        adminBlade = new AdminBlade(this, "admin");
        emailAddressesBlade = new EmailAddressesBlade(this, "emailAddresses");
        userBlade = new UserBlade(this, "user");
        editRolesBlade = new EditRolesBlade(this, "editRoles");
        recreateRoleBlade = new RecreateRoleBlade(this, "recreateRole");

        getOODb().registerNodeFactory("recreateAdminRole.node", new RecreateSystemRoleTransactionFactory());
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
