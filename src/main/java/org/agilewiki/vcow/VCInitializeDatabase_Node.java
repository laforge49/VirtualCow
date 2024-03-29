package org.agilewiki.vcow;

import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.awdb.nodes.*;
import org.agilewiki.vcow.roles.VCRole_NodeFactory;
import org.agilewiki.vcow.roles.admin.Admin_Role;
import org.agilewiki.vcow.roles.admin.editRoles.UpdateRoles_NodeFactory;
import org.agilewiki.vcow.roles.developer.Developer_Role;
import org.agilewiki.vcow.roles.developer.npje.Npje_NodeFactory;
import org.agilewiki.vcow.roles.system.ServletStart_NodeFactory;
import org.agilewiki.vcow.roles.system.ServletStop_NodeFactory;
import org.agilewiki.vcow.roles.system.System_Role;
import org.agilewiki.vcow.roles.user.User_Role;
import org.agilewiki.vcow.roles.user.changePassword.ChangePassword_NodeFactory;
import org.agilewiki.vcow.roles.user.logout.Logout_NodeFactory;
import org.agilewiki.vcow.roles.user.newEmailAddress.NewEmailAddress_NodeFactory;
import org.agilewiki.vcow.roles.visitor.Visitor_Role;
import org.agilewiki.vcow.roles.visitor.forgotPassword.ForgotPassword_NodeFactory;
import org.agilewiki.vcow.roles.visitor.login.BadUserAddress_NodeFactory;
import org.agilewiki.vcow.roles.visitor.login.BadUserPassword_NodeFactory;
import org.agilewiki.vcow.roles.visitor.login.Login_NodeFactory;
import org.agilewiki.vcow.roles.visitor.newUser.NewUser_NodeFactory;

public class VCInitializeDatabase_Node extends InitializeDatabase_Node {

    public VCInitializeDatabase_Node() {
    }

    public VCInitializeDatabase_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);

        getAwDb().createSecondaryId(Admin_Role.ID, Key_NodeFactory.NODETYPE_ID, VCRole_NodeFactory.ID);
        getAwDb().createSecondaryId(Developer_Role.ID, Key_NodeFactory.NODETYPE_ID, VCRole_NodeFactory.ID);
        getAwDb().createSecondaryId(System_Role.ID, Key_NodeFactory.NODETYPE_ID, VCRole_NodeFactory.ID);
        getAwDb().createSecondaryId(User_Role.ID, Key_NodeFactory.NODETYPE_ID, VCRole_NodeFactory.ID);
        getAwDb().createSecondaryId(Visitor_Role.ID, Key_NodeFactory.NODETYPE_ID, VCRole_NodeFactory.ID);

        Key_NodeFactory.define(NameIds.EMAIL_KEY_ID, User_NodeFactory.ID);
        Key_NodeFactory.define(NameIds.ROLE_KEY_ID, User_NodeFactory.ID);

        Attribute_NodeFactory.define(NameIds.REMOTE_HOST, Delete_NodeFactory.ID);
        Attribute_NodeFactory.define(NameIds.REMOTE_ADDR, Delete_NodeFactory.ID);
        Attribute_NodeFactory.define(NameIds.REMOTE_PORT, Delete_NodeFactory.ID);
        Attribute_NodeFactory.define(NameIds.PASSWORD_KEY, User_NodeFactory.ID);

        Node_NodeFactory.define(NameIds.USERS_SYSTEM_DOMAIN_ID, Domain_NodeFactory.ID, null);
        Node_NodeFactory.define(NameIds.ADMINS_USER_DOMAIN_ID, Domain_NodeFactory.ID, null);
        Node_NodeFactory.define(BadUserAddress_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.SUBJECT, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);
        Node_NodeFactory.define(BadUserPassword_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.SUBJECT, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);
        Node_NodeFactory.define(ChangePassword_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.PASSWORD_KEY);
        Node_NodeFactory.define(ForgotPassword_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.PASSWORD_KEY);
        Node_NodeFactory.define(Login_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.SUBJECT);
        Node_NodeFactory.define(Logout_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.SUBJECT);
        Node_NodeFactory.define(NewEmailAddress_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.EMAIL_ID);
        Node_NodeFactory.define(NewUser_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.EMAIL_ID, NameIds.PASSWORD_KEY, NameIds.ROLE_ID);
        Node_NodeFactory.define(Npje_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.SUBJECT, NameIds.BODY);
        Node_NodeFactory.define(ServletStart_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID);
        Node_NodeFactory.define(ServletStop_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID);
        Node_NodeFactory.define(UpdateRoles_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.NODE_ID, NameIds.ADDROLES_ID, NameIds.REMOVEROLES_ID);

        getAwDb().createLnk1(Developer_Role.ID, Lnk1_NodeFactory.ROLE_OF_REALM_ID, Realm_NodeFactory.SYSTEM_REALM_ID);
        getAwDb().createLnk1(Developer_Role.ID, Lnk1_NodeFactory.ROLE_OF_REALM_ID, Realm_NodeFactory.USER_REALM_ID);
        getAwDb().createLnk1(User_Role.ID, Lnk1_NodeFactory.ROLE_OF_REALM_ID, Realm_NodeFactory.USER_REALM_ID);
        getAwDb().createLnk1(Admin_Role.ID, Lnk1_NodeFactory.ROLE_OF_REALM_ID, Realm_NodeFactory.USER_REALM_ID);
        getAwDb().createLnk1(User_NodeFactory.SYSTEM_USER_ID, Lnk1_NodeFactory.USER_DOMAIN_ID, NameIds.USERS_SYSTEM_DOMAIN_ID);
        getAwDb().createLnk1(User_NodeFactory.SYSTEM_USER_ID, Lnk1_NodeFactory.USER_DOMAIN_ID, NameIds.ADMINS_USER_DOMAIN_ID);
        getAwDb().createLnk1(Group_NodeFactory.USERS_GROUP_ID, Lnk1_NodeFactory.GROUP_DOMAIN_ID, NameIds.USERS_SYSTEM_DOMAIN_ID);
        getAwDb().createLnk1(Group_NodeFactory.ADMINS_GROUP_ID, Lnk1_NodeFactory.GROUP_DOMAIN_ID, NameIds.ADMINS_USER_DOMAIN_ID);
        getAwDb().createLnk1(NameIds.USERS_SYSTEM_DOMAIN_ID, Lnk1_NodeFactory.DOMAIN_FOR_REALM_ID, Realm_NodeFactory.SYSTEM_REALM_ID);
        getAwDb().createLnk1(NameIds.ADMINS_USER_DOMAIN_ID, Lnk1_NodeFactory.DOMAIN_FOR_REALM_ID, Realm_NodeFactory.USER_REALM_ID);
        getAwDb().createLnk1(NameIds.USERS_SYSTEM_DOMAIN_ID, Lnk1_NodeFactory.DOMAIN_FOR_ROLE_ID, Developer_Role.ID);
        getAwDb().createLnk1(NameIds.ADMINS_USER_DOMAIN_ID, Lnk1_NodeFactory.DOMAIN_FOR_ROLE_ID, Admin_Role.ID);
        getAwDb().createLnk1(NameIds.ADMINS_USER_DOMAIN_ID, Lnk1_NodeFactory.DOMAIN_FOR_ROLE_ID, Developer_Role.ID);
    }
}
