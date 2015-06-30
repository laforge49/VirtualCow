package org.agilewiki.console.oodb.nodes.roles.visitor;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.visitor.forgotPassword.ForgotPasswordBlade;
import org.agilewiki.console.oodb.nodes.roles.visitor.login.LoginBlade;
import org.agilewiki.console.oodb.nodes.roles.visitor.newUser.ValidatedBlade;
import org.agilewiki.utils.immutable.FactoryRegistry;

/**
 * Default role when the user is not logged in.
 */
public class Visitor_Role extends Role_NodeInstance {
    public final static String ID = "$nvisitor.role";

    public static void create(OODb ooDb) throws Exception {
        ooDb.addTimelessNode(new Visitor_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
    }

    private WelcomeBlade welcomeBlade;

    public Visitor_Role(String nodeId, long timestamp)
            throws Exception {
        super(nodeId, timestamp);
        niceRoleName = "Visitor";
        welcomeBlade = new WelcomeBlade(this, "welcome");
        ForgotPasswordBlade forgotPasswordBlade = new ForgotPasswordBlade(this, "forgotPassword");
        LoginBlade loginBlade = new LoginBlade(this, "login");
        NewAccountBlade newAccountBlade = new NewAccountBlade(this, "newAccount");
        ForgotBlade forgotBlade = new ForgotBlade(this, "forgot");
        ValidatedBlade validatedBlade = new ValidatedBlade(this, "validated");
        AboutBlade aboutBlade = new AboutBlade(this, "about");
        ContactBlade contactBlade = new ContactBlade(this, "contact");

        RecreateVisitorRole_Node.create(getOoDb());
        getOoDb().registerTransaction(RecreateVisitorRole_NodeInstance.NAME, RecreateVisitorRole_NodeInstance.class);
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
