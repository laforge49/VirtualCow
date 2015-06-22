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
public class Visitor_Role extends Role_NodeInstance {
    private static Visitor_Role visitor_role;
    public final static String ID = "$nvisitor.role";

    public static Visitor_Role get() {
        return visitor_role;
    }

    public static void create() throws Exception {
        visitor_role = new Visitor_Role(ID, Role_Node.ID);
    }

    private WelcomeBlade welcomeBlade;

    public Visitor_Role(String nodeId, String factoryId)
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
