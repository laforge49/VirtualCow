package org.agilewiki.console.unRole;

import org.agilewiki.console.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Default role when the user is not logged in.
 */
public class UnRole extends RoleBase {

    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();

    private WelcomeBlade welcomeBlade;

    public UnRole(SimpleSimon simpleSimon)
            throws Exception {
        super(simpleSimon);

        welcomeBlade = new WelcomeBlade(simpleSimon, "welcome");
        requests.put(welcomeBlade.page, welcomeBlade);

        ForgotPasswordBlade forgotPasswordBlade = new ForgotPasswordBlade(simpleSimon, "forgotPassword");
        requests.put(forgotPasswordBlade.page, forgotPasswordBlade);
        posts.put(forgotPasswordBlade.page, forgotPasswordBlade);

        LoginBlade loginBlade = new LoginBlade(simpleSimon, "login");
        requests.put(loginBlade.page, loginBlade);
        posts.put(loginBlade.page, loginBlade);

        NewAccountBlade newAccountBlade = new NewAccountBlade(simpleSimon, "newAccount");
        requests.put(newAccountBlade.page, newAccountBlade);
        posts.put(newAccountBlade.page, newAccountBlade);

        ForgotBlade forgotBlade = new ForgotBlade(simpleSimon, "forgot");
        requests.put(forgotBlade.page, forgotBlade);
        posts.put(forgotBlade.page, forgotBlade);

        ValidatedBlade validatedBlade = new ValidatedBlade(simpleSimon, "validated");
        requests.put(validatedBlade.page, validatedBlade);
        posts.put(validatedBlade.page, validatedBlade);

        AboutBlade aboutBlade = new AboutBlade(simpleSimon, "about");
        requests.put(aboutBlade.page, aboutBlade);

        ContactBlade contactBlade = new ContactBlade(simpleSimon, "contact");
        requests.put(contactBlade.page, contactBlade);

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
    public RequestBlade requestBlade(String page) {
        return requests.get(page);
    }

    @Override
    public PostRequestBlade postRequestBlade(String page) {
        return posts.get(page);
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
