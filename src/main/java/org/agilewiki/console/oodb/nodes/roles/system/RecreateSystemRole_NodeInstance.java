package org.agilewiki.console.oodb.nodes.roles.system;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RecreateRole_NodeInstance;
import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.*;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.console.oodb.nodes.roles.admin.RecreateAdminRole_Node;
import org.agilewiki.console.oodb.nodes.roles.admin.editRoles.UpdateRoles_Node;
import org.agilewiki.console.oodb.nodes.roles.developer.RecreateDeveloperRole_Node;
import org.agilewiki.console.oodb.nodes.roles.developer.npje.Npje_Node;
import org.agilewiki.console.oodb.nodes.roles.user.RecreateUserRole_Node;
import org.agilewiki.console.oodb.nodes.roles.user.changePassword.ChangePassword_Node;
import org.agilewiki.console.oodb.nodes.roles.user.delete.Delete_Node;
import org.agilewiki.console.oodb.nodes.roles.user.logout.Logout_Node;
import org.agilewiki.console.oodb.nodes.roles.user.newEmailAddress.NewEmailAddress_Node;
import org.agilewiki.console.oodb.nodes.roles.visitor.RecreateVisitorRole_Node;
import org.agilewiki.console.oodb.nodes.roles.visitor.forgotPassword.ForgotPassword_Node;
import org.agilewiki.console.oodb.nodes.roles.visitor.login.BadUserAddress_Node;
import org.agilewiki.console.oodb.nodes.roles.visitor.login.BadUserPassword_Node;
import org.agilewiki.console.oodb.nodes.roles.visitor.login.Login_Node;
import org.agilewiki.console.oodb.nodes.roles.visitor.newUser.NewUser_Node;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateSystemRole_NodeInstance extends RecreateRole_NodeInstance {
    public final static String NAME = "recreateSystemRole";
    public static System_Role systemRole;

    public RecreateSystemRole_NodeInstance() {
    }

    public RecreateSystemRole_NodeInstance(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }

    @Override
    public Role role() {
        return systemRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);

        Key_Node.define(Key_Node.NODETYPE_KEY_ID, Node_Node.ID, System_Role.ID);
        Key_Node.define(Key_Node.SUPERTYPE_KEY_ID, Node_Node.ID, System_Role.ID);
        Key_Node.define(Key_Node.ATTRIBUTENAME_KEY_ID, Attribute_Node.ID, System_Role.ID);
        Key_Node.define(Key_Node.INVDEPENDENCY_KEY_ID, Lnk1_Node.ID, System_Role.ID);

        Key_Node.define(Key_Node.EMAIL_KEY_ID, User_Node.ID, System_Role.ID);
        Key_Node.define(Key_Node.ROLE_KEY_ID, User_Node.ID, System_Role.ID);
        Key_Node.define(Key_Node.SUBJECT_KEY_ID, Node_Node.ID, System_Role.ID);

        Lnk1_Node.define(Lnk1_Node.TARGET_LNK1_ID, null, Node_Node.ID, Node_Node.ID, System_Role.ID);
        Lnk1_Node.define(Lnk1_Node.OFROLE_LNK1_ID, Lnk1_Node.OFROLE_ID, Metadata_Node.ID, Role_Node.ID, System_Role.ID);
        Lnk1_Node.define(Lnk1_Node.ATTRIBUTEOF_LNK1_ID, Lnk1_Node.ATTRIBUTEOF_ID, Attribute_Node.ID, Metadata_Node.ID, System_Role.ID);
        Lnk1_Node.define(Lnk1_Node.ORIGIN_LNK1_ID, null, Lnk1_Node.ID, Node_Node.ID, System_Role.ID);
        Lnk1_Node.define(Lnk1_Node.DESTINATION_LNK1_ID, null, Lnk1_Node.ID, Node_Node.ID, System_Role.ID);

        Lnk1_Node.define(Lnk1_Node.USER_LNK1_ID, null, Metadata_Node.ID, User_Node.ID, System_Role.ID);

        Node_Node.define(Metadata_Node.ID, Node_Node.ID, null, System_Role.ID);

        Node_Node.define(Node_Node.ID, Node_Node.ID, Metadata_Node.ID, System_Role.ID);

        Node_Node.define(Key_Node.ID, Node_Node.ID, Metadata_Node.ID, System_Role.ID);

        Node_Node.define(Lnk1_Node.ID, Node_Node.ID, Metadata_Node.ID, System_Role.ID);

        Node_Node.define(Role_Node.ID, Node_Node.ID, Node_Node.ID, System_Role.ID);

        Node_Node.define(Attribute_Node.ID, Node_Node.ID, Node_Node.ID, System_Role.ID,
                NameIds.SUBJECT);

        Node_Node.define(User_Node.ID, Node_Node.ID, Node_Node.ID, System_Role.ID,
                NameIds.SUBJECT, User.PASSWORD_KEY);

        Node_Node.define(JournalEntry_Node.ID, Node_Node.ID, Node_Node.ID, System_Role.ID);

        Node_Node.define(BadUserAddress_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                NameIds.SUBJECT, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_Node.define(BadUserPassword_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                NameIds.SUBJECT, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_Node.define(ChangePassword_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, User.PASSWORD_KEY);

        Node_Node.define(Delete_Node.ID, Node_Node.ID, JournalEntry_Node.ID, NameIds.AN_ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_Node.define(ForgotPassword_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, User.PASSWORD_KEY);

        Node_Node.define(Login_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.SUBJECT);

        Node_Node.define(Logout_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.SUBJECT);

        Node_Node.define(NewEmailAddress_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, User.EMAIL_ID);

        Node_Node.define(NewUser_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                User.EMAIL_ID, User.PASSWORD_KEY, User.ROLE_ID);

        Node_Node.define(Npje_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.SUBJECT, NameIds.BODY);

        Node_Node.define(RecreateAdminRole_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_Node.define(RecreateDeveloperRole_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_Node.define(RecreateSystemRole_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_Node.define(RecreateUserRole_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_Node.define(RecreateVisitorRole_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_Node.define(ServletStart_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID);

        Node_Node.define(ServletStop_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID);

        Node_Node.define(UpdateRoles_Node.ID, Node_Node.ID, JournalEntry_Node.ID, System_Role.ID,
                User.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.NODE_ID, NameIds.ADDROLES_ID, NameIds.REMOVEROLES_ID);
    }
}
