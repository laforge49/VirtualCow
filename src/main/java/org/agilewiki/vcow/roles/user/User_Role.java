package org.agilewiki.vcow.roles.user;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.roles.Role_Node;
import org.agilewiki.vcow.roles.user.changePassword.ChangePasswordBlade;
import org.agilewiki.vcow.roles.user.delete.DeleteAccountBlade;
import org.agilewiki.vcow.roles.user.logout.LogoutBlade;
import org.agilewiki.vcow.roles.user.newEmailAddress.NewEmailAddressBlade;

/**
 * A base role.
 */
public class User_Role extends Role_Node {
    public final static String ID = "$nuser.role";

    public static void create(AwDb awDb)
            throws Exception {
        awDb.addTimelessNode(new User_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    private UserBlade userBlade;
    private ChangeEmailAddressBlade changeEmailAddressBlade;
    private ChangePasswordBlade changePasswordBlade;
    private DeleteAccountBlade deleteAccountBlade;
    private LogoutBlade logoutBlade;
    private NewEmailAddressBlade newEmailAddressBlade;

    public User_Role(String nodeId, long timestamp)
            throws Exception {
        super(nodeId, timestamp);
        niceRoleName = "User";
        logoutBlade = new LogoutBlade(this, "logout");
        deleteAccountBlade = new DeleteAccountBlade(this, "deleteAccount");
        changePasswordBlade = new ChangePasswordBlade(this, "changePassword");
        changeEmailAddressBlade = new ChangeEmailAddressBlade(this, "changeEmailAddress");
        newEmailAddressBlade = new NewEmailAddressBlade(this, "newEmailAddress");
        userBlade = new UserBlade(this, "user");
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return userBlade;
    }

    @Override
    public void menuItems(StringBuilder home,
                          String currentPage,
                          String setTimestamp,
                          String timestamp,
                          String setRole) {
        menuItem(home, currentPage, setTimestamp, setRole, userBlade);
        menuItem(home, currentPage, setTimestamp, setRole, changePasswordBlade);
        menuItem(home, currentPage, setTimestamp, setRole, changeEmailAddressBlade);
        menuItem(home, currentPage, setTimestamp, setRole, deleteAccountBlade);
    }
}
