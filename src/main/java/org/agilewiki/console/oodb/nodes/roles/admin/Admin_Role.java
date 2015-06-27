package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.admin.editRoles.EditRolesBlade;

/**
 * A base role.
 */
public class Admin_Role extends Role_NodeInstance {
    private static Admin_Role admin_role;
    public final static String ID = "$nadmin.role";

    public static Admin_Role get() {
        return admin_role;
    }

    public static void create()
            throws Exception {
        admin_role = new Admin_Role(ID);
    }

    private AdminBlade adminBlade;
    private EmailAddressesBlade emailAddressesBlade;
    private UserBlade userBlade;
    private EditRolesBlade editRolesBlade;
    private RecreateRoleBlade recreateRoleBlade;

    public Admin_Role(String nodeId)
            throws Exception {
        super(nodeId);
        niceRoleName = "Admin";
        adminBlade = new AdminBlade(this, "admin");
        emailAddressesBlade = new EmailAddressesBlade(this, "emailAddresses");
        userBlade = new UserBlade(this, "user");
        editRolesBlade = new EditRolesBlade(this, "editRoles");
        recreateRoleBlade = new RecreateRoleBlade(this, "recreateRole");

        RecreateAdminRole_Node.create();
        ooDb.registerTransaction(RecreateAdminRole_NodeInstance.NAME, RecreateAdminRole_NodeInstance.class);
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
