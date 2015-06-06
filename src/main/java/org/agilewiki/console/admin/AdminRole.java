package org.agilewiki.console.admin;

import org.agilewiki.console.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A base role.
 */
public class AdminRole extends RoleBase {

    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();
    private AdminBlade adminBlade;
    private EmailAddressesBlade emailAddressesBlade;
    private UserBlade userBlade;
    private EditRolesBlade editRolesBlade;
    private RecreateRoleBlade recreateRoleBlade;

    public AdminRole(SimpleSimon simpleSimon)
            throws Exception {
        super(simpleSimon);
        adminBlade = new AdminBlade(simpleSimon);
        requests.put("admin", adminBlade);

        emailAddressesBlade = new EmailAddressesBlade(simpleSimon);
        requests.put("emailAddresses", emailAddressesBlade);

        userBlade = new UserBlade(simpleSimon);
        requests.put("user", userBlade);

        editRolesBlade = new EditRolesBlade(simpleSimon);
        requests.put("editRoles", editRolesBlade);
        posts.put("editRoles", editRolesBlade);

        recreateRoleBlade = new RecreateRoleBlade(simpleSimon);
        requests.put("recreateRole", recreateRoleBlade);
        posts.put("recreateRole", recreateRoleBlade);

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
    public RequestBlade requestBlade(String page) {
        return requests.get(page);
    }

    @Override
    public PostRequestBlade postRequestBlade(String page) {
        return posts.get(page);
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
        menuItem(sb, currentPage, setTimestamp, setRole, "admin", adminBlade.niceName());
        menuItem(sb, currentPage, setTimestamp, setRole, "emailAddresses", emailAddressesBlade.niceName());
        menuItem(sb, currentPage, setTimestamp, setRole, "recreateRole", recreateRoleBlade.niceName(), timestamp != null);
    }
}
