package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.RoleBase;
import org.agilewiki.console.SimpleSimon;

import java.util.Map;

/**
 * Default role when the user is not logged in.
 */
public class UnRole extends RoleBase {

    private WelcomeBlade welcomeBlade;

    public UnRole(SimpleSimon simpleSimon, String roleId, Map<String, String> parameters)
            throws Exception {
        super(simpleSimon, roleId, parameters);

        welcomeBlade = new WelcomeBlade(this, "welcome");
        ForgotPasswordBlade forgotPasswordBlade = new ForgotPasswordBlade(this, "forgotPassword");
        LoginBlade loginBlade = new LoginBlade(this, "login");
        NewAccountBlade newAccountBlade = new NewAccountBlade(this, "newAccount");
        ForgotBlade forgotBlade = new ForgotBlade(this, "forgot");
        ValidatedBlade validatedBlade = new ValidatedBlade(this, "validated");
        AboutBlade aboutBlade = new AboutBlade(this, "about");
        ContactBlade contactBlade = new ContactBlade(this, "contact");

        simpleSimon.db.registerTransaction(RecreateUnRoleTransaction.NAME, RecreateUnRoleTransaction.class);
        RecreateUnRoleTransaction.unRole = this;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateUnRoleTransaction.NAME;
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return welcomeBlade;
    }
}
