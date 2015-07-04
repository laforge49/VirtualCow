package org.agilewiki.vcow.roles.system;

import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.awdb.db.virtualcow.Db;
import org.agilewiki.awdb.nodes.*;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.RecreateRole_Node;
import org.agilewiki.vcow.VCUser_NodeFactory;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.vcow.roles.Role_NodeFactory;
import org.agilewiki.vcow.roles.admin.RecreateAdminRole_NodeFactory;
import org.agilewiki.vcow.roles.admin.editRoles.UpdateRoles_NodeFactory;
import org.agilewiki.vcow.roles.developer.RecreateDeveloperRole_NodeFactory;
import org.agilewiki.vcow.roles.developer.npje.Npje_NodeFactory;
import org.agilewiki.vcow.roles.user.RecreateUserRole_NodeFactory;
import org.agilewiki.vcow.roles.user.changePassword.ChangePassword_NodeFactory;
import org.agilewiki.vcow.roles.user.delete.Delete_NodeFactory;
import org.agilewiki.vcow.roles.user.logout.Logout_NodeFactory;
import org.agilewiki.vcow.roles.user.newEmailAddress.NewEmailAddress_NodeFactory;
import org.agilewiki.vcow.roles.visitor.RecreateVisitorRole_NodeFactory;
import org.agilewiki.vcow.roles.visitor.forgotPassword.ForgotPassword_NodeFactory;
import org.agilewiki.vcow.roles.visitor.login.BadUserAddress_NodeFactory;
import org.agilewiki.vcow.roles.visitor.login.BadUserPassword_NodeFactory;
import org.agilewiki.vcow.roles.visitor.login.Login_NodeFactory;
import org.agilewiki.vcow.roles.visitor.newUser.NewUser_NodeFactory;

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


        Key_NodeFactory.define(NameIds.EMAIL_KEY_ID, VCUser_NodeFactory.ID);
        Key_NodeFactory.define(NameIds.ROLE_KEY_ID, VCUser_NodeFactory.ID);

        Lnk1_NodeFactory.define(NameIds.USER_LNK1_ID, null, Metadata_NodeFactory.ID, VCUser_NodeFactory.ID);

        Node_NodeFactory.define(Metadata_NodeFactory.ID, Node_NodeFactory.ID, null);

        Node_NodeFactory.define(Node_NodeFactory.ID, Node_NodeFactory.ID, Metadata_NodeFactory.ID);

        Node_NodeFactory.define(Key_NodeFactory.ID, Node_NodeFactory.ID, Metadata_NodeFactory.ID);

        Node_NodeFactory.define(Lnk1_NodeFactory.ID, Node_NodeFactory.ID, Metadata_NodeFactory.ID);

        Node_NodeFactory.define(Role_NodeFactory.ID, Node_NodeFactory.ID, Node_NodeFactory.ID);

        Node_NodeFactory.define(Attribute_NodeFactory.ID, Node_NodeFactory.ID, Node_NodeFactory.ID,
                NameIds.SUBJECT);

        Node_NodeFactory.define(VCUser_NodeFactory.ID, Node_NodeFactory.ID, Node_NodeFactory.ID,
                NameIds.SUBJECT, NameIds.PASSWORD_KEY);

        Node_NodeFactory.define(JournalEntry_NodeFactory.ID, Node_NodeFactory.ID, Node_NodeFactory.ID);

        Node_NodeFactory.define(BadUserAddress_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.SUBJECT, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_NodeFactory.define(BadUserPassword_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.SUBJECT, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_NodeFactory.define(ChangePassword_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT, NameIds.PASSWORD_KEY);

        Node_NodeFactory.define(Delete_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.AN_ID, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

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

        Node_NodeFactory.define(RecreateAdminRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_NodeFactory.define(RecreateDeveloperRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_NodeFactory.define(RecreateSystemRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_NodeFactory.define(RecreateUserRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_NodeFactory.define(RecreateVisitorRole_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT);

        Node_NodeFactory.define(ServletStart_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID);

        Node_NodeFactory.define(ServletStop_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID);

        Node_NodeFactory.define(UpdateRoles_NodeFactory.ID, Node_NodeFactory.ID, JournalEntry_NodeFactory.ID,
                NameIds.USER_KEY, NameIds.REMOTE_HOST, NameIds.REMOTE_ADDR, NameIds.REMOTE_PORT,
                NameIds.NODE_ID, NameIds.ADDROLES_ID, NameIds.REMOVEROLES_ID);
    }
}
