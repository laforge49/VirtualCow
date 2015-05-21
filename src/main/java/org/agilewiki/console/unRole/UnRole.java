package org.agilewiki.console.unRole;

import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.requests.PostRequestBlade;
import org.agilewiki.console.requests.RequestBlade;
import org.agilewiki.console.requests.WelcomeBlade;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Default role when the user is not logged in.
 */
public class UnRole implements Role {

    Map<String, RequestBlade> requests = new HashMap<String, RequestBlade>();
    Map<String, PostRequestBlade> posts = new HashMap<String, PostRequestBlade>();

    WelcomeBlade welcomeBlade;

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

    public RequestBlade requestBlade(String page) {
        return requests.get(page);
    }

    public PostRequestBlade postRequestBlade(String page) {
        return posts.get(page);
    }

    public void dispatchGetRequest(HttpServletRequest request, String userId) {
        String page = request.getParameter("to");
        RequestBlade rb = requests.get(page);
        if (rb == null) {
            page = "welcome";
            rb = welcomeBlade;
        }
        AsyncContext asyncContext = request.startAsync();
        rb.get(page, asyncContext, userId);
    }

    public void dispatchPostRequest(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String userId)
            throws ServletException, IOException {
        String page = request.getParameter("to");
        PostRequestBlade rb = posts.get(page);
        if (rb == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        AsyncContext asyncContext = request.startAsync();
        rb.post(page, asyncContext, userId);
    }
}
