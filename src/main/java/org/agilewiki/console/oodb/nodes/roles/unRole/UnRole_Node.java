package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.oodb.nodes.roles.Role_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.unRole.forgotPassword.ForgotPasswordBlade;
import org.agilewiki.console.oodb.nodes.roles.unRole.login.LoginBlade;
import org.agilewiki.console.oodb.nodes.roles.unRole.newUser.ValidatedBlade;

/**
 * Default role when the user is not logged in.
 */
public class UnRole_Node extends Role_NodeInstance {
    private static UnRole_Node unRole_node;

    public static UnRole_Node get() {
        return unRole_node;
    }

    public static void create() throws Exception {
        unRole_node = new UnRole_Node("$nunRole.node", "$nrole.node");
    }

    private WelcomeBlade welcomeBlade;

    public UnRole_Node(String nodeId, String factoryId)
            throws Exception {
        super(nodeId, factoryId);
        niceRoleName = "unRole";
        welcomeBlade = new WelcomeBlade(this, "welcome");
        ForgotPasswordBlade forgotPasswordBlade = new ForgotPasswordBlade(this, "forgotPassword");
        LoginBlade loginBlade = new LoginBlade(this, "login");
        NewAccountBlade newAccountBlade = new NewAccountBlade(this, "newAccount");
        ForgotBlade forgotBlade = new ForgotBlade(this, "forgot");
        ValidatedBlade validatedBlade = new ValidatedBlade(this, "validated");
        AboutBlade aboutBlade = new AboutBlade(this, "about");
        ContactBlade contactBlade = new ContactBlade(this, "contact");

        RecreateUnRole_Node.create();
        simpleSimon.db.registerTransaction(RecreateUnRole_NodeInstance.NAME, RecreateUnRole_NodeInstance.class);
        RecreateUnRole_NodeInstance.unRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateUnRole_NodeInstance.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return welcomeBlade;
    }
}
