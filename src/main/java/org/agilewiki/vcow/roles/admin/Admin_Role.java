package org.agilewiki.vcow.roles.admin;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.roles.Role_NodeInstance;
import org.agilewiki.vcow.roles.admin.editRoles.EditRolesBlade;

/**
 * A base role.
 */
public class Admin_Role extends Role_NodeInstance {
    public final static String ID = "$nadmin.role";

    public static void create(AwDb awDb)
            throws Exception {
        awDb.addTimelessNode(new Admin_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
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

        RecreateAdminRole_NodeFactory.create(getAwDb());
        getAwDb().registerTransaction(RecreateAdminRole_Node.NAME, RecreateAdminRole_Node.class);
        RecreateAdminRole_Node.adminRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateAdminRole_Node.NAME;
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
