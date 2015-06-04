package org.agilewiki.console.unRole;

import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;

import java.util.HashMap;
import java.util.Map;

/**
 * Default role when the user is not logged in.
 */
public class UnRole implements Role {

    private Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    private Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();

    private WelcomeBlade welcomeBlade;

    public UnRole(SimpleSimon simpleSimon)
            throws Exception {
        welcomeBlade = new WelcomeBlade(simpleSimon);

        ForgotPasswordBlade forgotPasswordBlade = new ForgotPasswordBlade(simpleSimon);
        LoginBlade loginBlade = new LoginBlade(simpleSimon);
        NewAccountBlade newAccountBlade = new NewAccountBlade(simpleSimon);
        ForgotBlade forgotBlade = new ForgotBlade(simpleSimon);
        ValidatedBlade validatedBlade = new ValidatedBlade(simpleSimon);
        AboutBlade aboutBlade = new AboutBlade(simpleSimon);
        ContactBlade contactBlade = new ContactBlade(simpleSimon);

        requests.put("welcome", welcomeBlade);
        requests.put("about", aboutBlade);
        requests.put("contact", contactBlade);
        requests.put("login", loginBlade);
        requests.put("newAccount", newAccountBlade);
        requests.put("validated", validatedBlade);
        requests.put("forgot", forgotBlade);
        requests.put("forgotPassword", forgotPasswordBlade);

        posts.put("login", loginBlade);
        posts.put("newAccount", newAccountBlade);
        posts.put("validated", validatedBlade);
        posts.put("forgot", forgotBlade);
        posts.put("forgotPassword", forgotPasswordBlade);
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
