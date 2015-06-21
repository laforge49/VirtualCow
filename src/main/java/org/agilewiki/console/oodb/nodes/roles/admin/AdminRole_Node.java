package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.admin.editRoles.EditRolesBlade;

/**
 * A base role.
 */
public class AdminRole_Node extends Role_NodeInstance {
    private static AdminRole_Node adminRole_node;

    public static AdminRole_Node get() {
        return adminRole_node;
    }

    public static void create()
            throws Exception {
        adminRole_node = new AdminRole_Node("$nadminRole.node", "$nrole.node");
    }

    private AdminBlade adminBlade;
    private EmailAddressesBlade emailAddressesBlade;
    private UserBlade userBlade;
    private EditRolesBlade editRolesBlade;
    private RecreateRoleBlade recreateRoleBlade;

    public AdminRole_Node(String nodeId, String factoryId)
            throws Exception {
        super(nodeId, factoryId);
        niceRoleName = "Admin";
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
