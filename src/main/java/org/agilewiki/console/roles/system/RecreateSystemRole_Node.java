package org.agilewiki.console.roles.system;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RecreateRole_Node;
import org.agilewiki.console.User_Node;
import org.agilewiki.awdb.nodes.*;
import org.agilewiki.console.roles.Role;
import org.agilewiki.console.roles.Role_NodeFactory;
import org.agilewiki.console.roles.admin.RecreateAdminRole_NodeFactory;
import org.agilewiki.console.roles.admin.editRoles.UpdateRoles_NodeFactory;
import org.agilewiki.console.roles.developer.RecreateDeveloperRole_NodeFactory;
import org.agilewiki.console.roles.developer.npje.Npje_NodeFactory;
import org.agilewiki.console.roles.user.RecreateUserRole_NodeFactory;
import org.agilewiki.console.roles.user.changePassword.ChangePassword_NodeFactory;
import org.agilewiki.console.roles.user.delete.Delete_NodeFactory;
import org.agilewiki.console.roles.user.logout.Logout_NodeFactory;
import org.agilewiki.console.roles.user.newEmailAddress.NewEmailAddress_NodeFactory;
import org.agilewiki.console.roles.visitor.RecreateVisitorRole_NodeFactory;
import org.agilewiki.console.roles.visitor.forgotPassword.ForgotPassword_NodeFactory;
import org.agilewiki.console.roles.visitor.login.BadUserAddress_NodeFactory;
import org.agilewiki.console.roles.visitor.login.BadUserPassword_NodeFactory;
import org.agilewiki.console.roles.visitor.login.Login_NodeFactory;
import org.agilewiki.console.roles.visitor.newUser.NewUser_NodeFactory;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

public class RecreateSystemRole_Node extends RecreateRole_Node {
    public final static String NAME = "recreateSystemRole";
    public static System_Role systemRole;

    public RecreateSystemRole_Node() {
    }

    public RecreateSystemRole_Node(String nodeId, long timestamp) {
        super(nodeId, timestamp);
    }

    @Override
    public Role role() {
        return systemRole;
    }

    @Override
    public void process(Db db, MapNode tMapNode) {
        super.process(db, tMapNode);

        defineKey(Key_NodeFactory.NODETYPE_KEY_ID, Node_NodeFactory.ID, System_Role.ID);
        defineKey(Key_NodeFactory.SUPERTYPE_KEY_ID, Node_NodeFactory.ID, System_Role.ID);
        defineKey(Key_NodeFactory.ATTRIBUTENAME_KEY_ID, Attribute_NodeFactory.ID, System_Role.ID);
        defineKey(Key_NodeFactory.INVDEPENDENCY_KEY_ID, Lnk1_NodeFactory.ID, System_Role.ID);

        defineKey(NameIds.EMAIL_KEY_ID, User_Node.ID, System_Role.ID);
        defineKey(NameIds.ROLE_KEY_ID, User_Node.ID, System_Role.ID);
        defineKey(NameIds.SUBJECT_KEY_ID, Node_NodeFactory.ID, System_Role.ID);

        defineLnk1(Lnk1_NodeFactory.TARGET_LNK1_ID, null, Node_NodeFactory.ID, Node_NodeFactory.ID, System_Role.ID);
        defineLnk1(NameIds.OFROLE_LNK1_ID, NameIds.OFROLE_ID, Metadata_NodeFactory.ID, Role_NodeFactory.ID, System_Role.ID);
        defineLnk1(Lnk1_NodeFactory.ATTRIBUTEOF_LNK1_ID, Lnk1_NodeFactory.ATTRIBUTEOF_ID, Attribute_NodeFactory.ID, Metadata_NodeFactory.ID, System_Role.ID);
        defineLnk1(Lnk1_NodeFactory.ORIGIN_LNK1_ID, null, Lnk1_NodeFactory.ID, Node_NodeFactory.ID, System_Role.ID);
        defineLnk1(Lnk1_NodeFactory.DESTINATION_LNK1_ID, null, Lnk1_NodeFactory.ID, Node_NodeFactory.ID, System_Role.ID);

        defineLnk1(NameIds.USER_LNK1_ID, null, Metadata_NodeFactory.ID, User_Node.ID, System_Role.ID);

        defineNode(Metadata_NodeFactory.ID, Node_NodeFactory.ID, null, System_Role.ID);

        defineNode(Node_NodeFactory.ID, Node_NodeFactory.ID, Metadata_NodeFactory.ID, System_Role.ID);

        defineNode(Key_NodeFactory.ID, Node_NodeFactory.ID, Metadata_NodeFactory.ID, System_Role.ID);

        defineNode(Lnk1_NodeFactory.ID, Node_NodeFactory.ID, Metadata_NodeFactory.ID, System_Role.ID);

        defineNode(Role_NodeFactory.ID, Node_NodeFactory.ID, Node_NodeFactory.ID, System_Role.ID);

        defineNode(Attribute_NodeFactory.ID, Node_NodeFactory.ID, Node_NodeFactory.ID, System_Role.ID,
                NameIds.SUBJECT);

        defineNode(User_Node.ID, Node_NodeFactory.ID, Node_NodeFactory.ID, System_Role.ID,
                NameIds.SUBJECT, NameIds.PASSWORD_KEY);

        defineNode(JournalEntry_NodeFactory.ID, Node_NodeFactory.ID, Node_NodeFactory.ID, System_Role.ID);

        defineNode(BadUserAddress_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.SUBJECT, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        defineNode(BadUserPassword_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.SUBJECT, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        defineNode(ChangePassword_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.PASSWORD_KEY);

        defineNode(Delete_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.AN_ID, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        defineNode(ForgotPassword_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.PASSWORD_KEY);

        defineNode(Login_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.SUBJECT);

        defineNode(Logout_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.SUBJECT);

        defineNode(NewEmailAddress_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.EMAIL_ID);

        defineNode(NewUser_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.EMAIL_ID, NameIds.PASSWORD_KEY, NameIds.ROLE_ID);

        defineNode(Npje_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.SUBJECT, NameIds.BODY);

        defineNode(RecreateAdminRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        defineNode(RecreateDeveloperRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        defineNode(RecreateSystemRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        defineNode(RecreateUserRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        defineNode(RecreateVisitorRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        defineNode(ServletStart_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID);

        defineNode(ServletStop_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID);

        defineNode(UpdateRoles_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID, System_Role.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.NODE_ID, NameIds.ADDROLES_ID, NameIds.REMOVEROLES_ID);
    }
}
