package org.agilewiki.console.unRole;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.RoleBase;
import org.agilewiki.console.SimpleSimon;

/**
 * Default role when the user is not logged in.
 */
public class UnRole extends RoleBase {

    private WelcomeBlade welcomeBlade;

    public UnRole(SimpleSimon simpleSimon)
            throws Exception {
        super(simpleSimon);

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
    public SimpleSimon simpleSimon() {
        return simpleSimon;
    }

    @Override
    public String initializeTransactionName() {
        return RecreateUnRoleTransaction.NAME;
    }

    @Override
    public String roleName() {
        return "unRole";
    }

    @Override
    public RequestBlade getDefaultRequestBlade() {
        return welcomeBlade;
    }

    @Override
    public String getDefaultRequestPage() {
        return "welcome";
    }
}
