package org.agilewiki.vcow.roles.visitor;

import org.agilewiki.awdb.AwDb;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.roles.VCRole_Node;
import org.agilewiki.vcow.roles.visitor.forgotPassword.ForgotPasswordBlade;
import org.agilewiki.vcow.roles.visitor.login.LoginBlade;
import org.agilewiki.vcow.roles.visitor.newUser.ValidatedBlade;

/**
 * Default role when the user is not logged in.
 */
public class Visitor_Role extends VCRole_Node {
    public final static String ID = "$nvisitor.role";

    public static void create(AwDb awDb) throws Exception {
        awDb.addTimelessNode(new Visitor_Role(ID, FactoryRegistry.MAX_TIMESTAMP));
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

    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return welcomeBlade;
    }
}
