package org.agilewiki.console.roles.admin;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.roles.Role_NodeInstance;
import org.agilewiki.console.roles.admin.editRoles.EditRolesBlade;
import org.agilewiki.utils.immutable.FactoryRegistry;

/**
 * A base role.
 */
public class Admin_Role extends Role_NodeInstance {
    public final static String ID = "$nadmin.role";

    public static void create(OODb ooDb)
            throws Exception {
        ooDb.addTimelessNode(new Admin_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    private AdminBlade adminBlade;
    private EmailAddressesBlade emailAddressesBlade;
    private UserBlade userBlade;
    private EditRolesBlade editRolesBlade;
    private RecreateRoleBlade recreateRoleBlade;

    public Admin_Role(String nodeId, long timestamp)
            throws Exception {
        super(nodeId, timestamp);
        niceRoleName = "Admin";
        adminBlade = new AdminBlade(this, "admin");
        emailAddressesBlade = new EmailAddressesBlade(this, "emailAddresses");
        userBlade = new UserBlade(this, "user");
        editRolesBlade = new EditRolesBlade(this, "editRoles");
        recreateRoleBlade = new RecreateRoleBlade(this, "recreateRole");

        RecreateAdminRole_Node.create(getOoDb());
        getOoDb().registerTransaction(RecreateAdminRole_NodeInstance.NAME, RecreateAdminRole_NodeInstance.class);
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