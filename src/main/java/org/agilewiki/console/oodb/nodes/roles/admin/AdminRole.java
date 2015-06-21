package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.nodes.roles.RoleBase;
import org.agilewiki.console.oodb.nodes.roles.admin.editRoles.EditRolesBlade;

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

        RecreateAdminRole_Node.create();
        simpleSimon.db.registerTransaction(RecreateAdminRole_NodeInstance.NAME, RecreateAdminRole_NodeInstance.class);
        RecreateAdminRole_NodeInstance.adminRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateAdminRole_NodeInstance.NAME;
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
