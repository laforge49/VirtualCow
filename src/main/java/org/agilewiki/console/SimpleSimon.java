package org.agilewiki.console;

import org.agilewiki.console.requests.*;
import org.agilewiki.console.requests.maintenance.*;
import org.agilewiki.console.requests.profile.*;
import org.agilewiki.console.transactions.ServletStartTransaction;
import org.agilewiki.console.transactions.ServletStopTransaction;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.utils.ids.Timestamp;
import org.agilewiki.utils.immutable.BaseRegistry;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleSimon extends HttpServlet {

    public final static String self = System.getProperties().getProperty("self", "http://localhost/");

    public Db db;
    ServletConfig servletConfig;
    public ServletContext servletContext;
    final static Charset utf8 = Charset.forName("UTF-8");
    final static String SPECIAL = "&<>'\"";
    public final static int ENCODE_MULTIPLE_LINES = 1;
    public final static int ENCODE_SINGLE_LINE = 2;
    public final static int ENCODE_FIELD = 3;

    public MailOut mailOut;

    ChangeEmailAddressBlade changeEmailAddressBlade;
    ChangePasswordBlade changePasswordBlade;
    DeleteAccountBlade deleteAccountBlade;
    ForgotPasswordBlade forgotPasswordBlade;
    HomeBlade homeBlade;
    JournalBlade journalBlade;
    NodeBlade nodeBlade;
    LoginBlade loginBlade;
    NewAccountBlade newAccountBlade;
    ForgotBlade forgotBlade;
    LogoutBlade logoutBlade;
    NewEmailAddressBlade newEmailAddressBlade;
    PostBlade postBlade;
    ProfileBlade profileBlade;
    SecondaryKeysBlade secondaryKeysBlade;
    NodesBlade nodesBlade;
    InvLinksBlade invLinksBlade;
    ValidatedBlade validatedBlade;
    WelcomeBlade welcomeBlade;
    AboutBlade aboutBlade;
    ContactBlade contactBlade;

    Map<String, RequestBlade> unknownRequests;
    Map<String, RequestBlade> guestRequests;
    Map<String, PostRequestBlade> unknownPosts;
    Map<String, PostRequestBlade> guestPosts;

    public static String readResource(ServletContext servletContext, String groupName, String pageName) throws IOException {
        if (groupName != null && groupName.length() > 0) {
            pageName = groupName + "/" + pageName;
        }
        InputStream is = servletContext.getResourceAsStream("/WEB-INF/pages/" + pageName + ".html");
        InputStreamReader isr = new InputStreamReader(is, utf8);
        StringBuilder sb = new StringBuilder();
        char[] ca = new char[1000];
        while (true) {
            int l = isr.read(ca, 0, 1000);
            if (l == -1) {
                return sb.toString();
            }
            sb.append(ca, 0, l);
        }
    }

    public static String replace(ServletContext servletContext,
                                 String groupName,
                                 String pageName,
                                 Map<String, String> sub) throws IOException {
        String t = readResource(servletContext, groupName, pageName);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while (i < t.length()) {
            int k = t.indexOf("{{", i);
            if (k == -1)
                break;
            if (k > i)
                sb.append(t.substring(i, k));
            int j = t.indexOf("}}", k);
            String n = t.substring(k + 2, j);
            i = j + 2;
            if (sub.containsKey(n))
                sb.append(sub.get(n));
        }
        if (i < t.length())
            sb.append(t.substring(i));
        return sb.toString();
    }

    public void init() {
        try {
            servletConfig = getServletConfig();
            servletContext = servletConfig.getServletContext();

            new Plant();
            Path dbPath = Paths.get("vcow.db");
            int maxRootBlockSize = 100000;
            db = new Db(new BaseRegistry(), dbPath, maxRootBlockSize);

            db.registerTransaction(ServletStartTransaction.NAME, ServletStartTransaction.class);
            ServletStartTransaction.servletConfig = servletConfig;

            db.registerTransaction(ServletStopTransaction.NAME, ServletStopTransaction.class);

            if (Files.exists(dbPath))
                db.open();
            else
                db.open(true);

            mailOut = new MailOut();

            homeBlade = new HomeBlade(this);
            postBlade = new PostBlade(this);
            nodeBlade = new NodeBlade(this);
            journalBlade = new JournalBlade(this);
            secondaryKeysBlade = new SecondaryKeysBlade(this);
            nodesBlade = new NodesBlade(this);
            invLinksBlade = new InvLinksBlade(this);
            logoutBlade = new LogoutBlade(this);
            deleteAccountBlade = new DeleteAccountBlade(this);
            changePasswordBlade = new ChangePasswordBlade(this);
            changeEmailAddressBlade = new ChangeEmailAddressBlade(this);
            validatedBlade = new ValidatedBlade(this);
            forgotPasswordBlade = new ForgotPasswordBlade(this);
            newEmailAddressBlade = new NewEmailAddressBlade(this);
            loginBlade = new LoginBlade(this);
            forgotBlade = new ForgotBlade(this);
            newAccountBlade = new NewAccountBlade(this);
            profileBlade = new ProfileBlade(this);
            welcomeBlade = new WelcomeBlade(this);
            aboutBlade = new AboutBlade(this);
            contactBlade = new ContactBlade(this);

            unknownRequests = new HashMap<String, RequestBlade>();
            unknownRequests.put("welcome", welcomeBlade);
            unknownRequests.put("about", aboutBlade);
            unknownRequests.put("contact", contactBlade);
            unknownRequests.put("login", loginBlade);
            unknownRequests.put("newAccount", newAccountBlade);
            unknownRequests.put("validated", validatedBlade);
            unknownRequests.put("forgot", forgotBlade);
            unknownRequests.put("forgotPassword", forgotPasswordBlade);

            unknownPosts = new HashMap<String, PostRequestBlade>();
            unknownPosts.put("login", loginBlade);
            unknownPosts.put("newAccount", newAccountBlade);
            unknownPosts.put("validated", validatedBlade);
            unknownPosts.put("forgot", forgotBlade);
            unknownPosts.put("forgotPassword", forgotPasswordBlade);

            guestRequests = new HashMap<String, RequestBlade>();
            guestRequests.put("home", homeBlade);
            guestRequests.put("post", postBlade);
            guestRequests.put("node", nodeBlade);
            guestRequests.put("journal", journalBlade);
            guestRequests.put("secondaryKeys", secondaryKeysBlade);
            guestRequests.put("nodes", nodesBlade);
            guestRequests.put("invLinks", invLinksBlade);
            guestRequests.put("deleteAccount", deleteAccountBlade);
            guestRequests.put("changePassword", changePasswordBlade);
            guestRequests.put("changeEmailAddress", changeEmailAddressBlade);
            guestRequests.put("newEmailAddress", newEmailAddressBlade);
            guestRequests.put("profile", profileBlade);

            guestPosts = new HashMap<String, PostRequestBlade>();
            guestPosts.put("post", postBlade);
            guestPosts.put("logout", logoutBlade);
            guestPosts.put("deleteAccount", deleteAccountBlade);
            guestPosts.put("changePassword", changePasswordBlade);
            guestPosts.put("newEmailAddress", newEmailAddressBlade);
            guestPosts.put("changeEmailAddress", changeEmailAddressBlade);

            ServletStartTransaction.update(db);
        } catch (Exception ex) {
            destroy();
        }
    }

    public void destroy() {
        try {
            ServletStopTransaction.update(db);
        } catch (Exception e) {
        }
        db.close();
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String page = request.getParameter("to");
        String userIdToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    userIdToken = cookie.getValue();
                    break;
                }
            }

        String userId = null;
        if (userIdToken != null)
            userId = Tokens.parse(db, userIdToken);
        if (page == null)
            page = "home";
        if (userId == null) {
            RequestBlade rb = unknownRequests.get(page);
            if (rb == null) {
                page = "welcome";
                rb = welcomeBlade;
            }
            AsyncContext asyncContext = request.startAsync();
            rb.get(page, asyncContext, userId);
            return;
        }

        RequestBlade rb = guestRequests.get(page);
        if (rb == null) {
            page = "home";
            rb = homeBlade;
        }
        AsyncContext asyncContext = request.startAsync();
        rb.get(page, asyncContext, userId);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String userIdToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    userIdToken = cookie.getValue();
                    break;
                }
            }
        String userId = null;
        if (userIdToken != null)
            userId = Tokens.parse(db, userIdToken);
        String page = request.getParameter("to");

        PostRequestBlade pb = null;
        if (userId == null) {
            pb = unknownPosts.get(page);
        } else {
            pb = guestPosts.get(page);
        }
        if (pb != null) {
            AsyncContext asyncContext = request.startAsync();
            pb.post(page, asyncContext, userId);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public static String encode(String s, int indent, int mode) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            String a;
            switch (c) {
                case '\n':
                    if (mode == ENCODE_MULTIPLE_LINES) {
                        a = "<br />";
                        for (int i = 0; i < indent; ++i) {
                            a += "&nbsp;";
                        }
                    } else
                        a = "\n";
                    break;
                case '&':
                    a = "&amp;";
                    break;
                case ' ':
                    if (mode != ENCODE_FIELD)
                        a = "&nbsp;";
                    else
                        a = "" + c;
                    break;
                case '<':
                    a = "&lt;";
                    break;
                case '>':
                    a = "&gt;";
                    break;
                case '\'':
                    a = "&apos;";
                    break;
                case '"':
                    a = "&quot;";
                    break;
                default:
                    a = "" + c;
            }
            sb.append(a);
        }
        return sb.toString();
    }

    public static String niceTime(String timestampId) {
        Date date = new Date(Timestamp.time(timestampId));
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
        return formatter.format(date);
    }
}
