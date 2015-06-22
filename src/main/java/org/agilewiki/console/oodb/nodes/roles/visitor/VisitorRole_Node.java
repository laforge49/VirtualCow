package org.agilewiki.console.oodb.nodes.roles.visitor;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.nodes.roles.Role_Node;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.visitor.forgotPassword.ForgotPasswordBlade;
import org.agilewiki.console.oodb.nodes.roles.visitor.login.LoginBlade;
import org.agilewiki.console.oodb.nodes.roles.visitor.newUser.ValidatedBlade;

/**
 * Default role when the user is not logged in.
 */
public class VisitorRole_Node extends Role_NodeInstance {
    private static VisitorRole_Node visitorRole_node;
    public final static String ID = "$nvisitorRole.node";

    public static VisitorRole_Node get() {
        return visitorRole_node;
    }

    public static void create() throws Exception {
        visitorRole_node = new VisitorRole_Node(ID, Role_Node.ID);
    }

    private WelcomeBlade welcomeBlade;

    public VisitorRole_Node(String nodeId, String factoryId)
            throws Exception {
        super(nodeId, factoryId);
        niceRoleName = "Visitor";
        welcomeBlade = new WelcomeBlade(this, "welcome");
        ForgotPasswordBlade forgotPasswordBlade = new ForgotPasswordBlade(this, "forgotPassword");
        LoginBlade loginBlade = new LoginBlade(this, "login");
        NewAccountBlade newAccountBlade = new NewAccountBlade(this, "newAccount");
        ForgotBlade forgotBlade = new ForgotBlade(this, "forgot");
        ValidatedBlade validatedBlade = new ValidatedBlade(this, "validated");
        AboutBlade aboutBlade = new AboutBlade(this, "about");
        ContactBlade contactBlade = new ContactBlade(this, "contact");

        RecreateVisitorRole_Node.create();
        simpleSimon.db.registerTransaction(RecreateVisitorRole_NodeInstance.NAME, RecreateVisitorRole_NodeInstance.class);
        RecreateVisitorRole_NodeInstance.visitorRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateVisitorRole_NodeInstance.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return welcomeBlade;
    }
}
