package org.agilewiki.console;

import org.agilewiki.console.requests.*;
import org.agilewiki.console.transactions.*;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.utils.ids.Timestamp;
import org.agilewiki.utils.immutable.BaseRegistry;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.virtualcow.Db;

import javax.mail.MessagingException;
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
import java.security.NoSuchAlgorithmException;
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
    JournalEntryBlade journalEntryBlade;
    LoginBlade loginBlade;
    LogoutBlade logoutBlade;
    NewEmailAddressBlade newEmailAddressBlade;
    PostBlade postBlade;
    ProfileBlade profileBlade;
    SubjectsBlade subjectsBlade;
    ValidatedBlade validatedBlade;
    WelcomeBlade welcomeBlade;

    Map<String, RequestBlade> unknownRequests;
    Map<String, RequestBlade> guestRequests;

    public static String readResource(ServletContext servletContext, String pageName) throws IOException {
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
                                 String pageName,
                                 Map<String, String> sub) throws IOException {
        String t = readResource(servletContext, pageName);
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
            journalEntryBlade = new JournalEntryBlade(this);
            journalBlade = new JournalBlade(this);
            subjectsBlade = new SubjectsBlade(this);
            logoutBlade = new LogoutBlade(this);
            deleteAccountBlade = new DeleteAccountBlade(this);
            changePasswordBlade = new ChangePasswordBlade(this);
            changeEmailAddressBlade = new ChangeEmailAddressBlade(this);
            validatedBlade = new ValidatedBlade(this);
            forgotPasswordBlade = new ForgotPasswordBlade(this);
            newEmailAddressBlade = new NewEmailAddressBlade(this);
            loginBlade = new LoginBlade(this);
            profileBlade = new ProfileBlade(this);
            welcomeBlade = new WelcomeBlade(this);

            unknownRequests = new HashMap<String, RequestBlade>();
            unknownRequests.put("welcome", welcomeBlade);
            unknownRequests.put("validated", validatedBlade);
            unknownRequests.put("forgotPassword", forgotPasswordBlade);
            unknownRequests.put("login", loginBlade);

            guestRequests = new HashMap<String, RequestBlade>();
            guestRequests.put("welcome", welcomeBlade);
            guestRequests.put("home", homeBlade);
            guestRequests.put("post", postBlade);
            guestRequests.put("journalEntry", journalEntryBlade);
            guestRequests.put("journal", journalBlade);
            guestRequests.put("subjects", subjectsBlade);
            guestRequests.put("deleteAccount", deleteAccountBlade);
            guestRequests.put("changePassword", changePasswordBlade);
            guestRequests.put("changeEmailAddress", changeEmailAddressBlade);
            guestRequests.put("validated", validatedBlade);
            guestRequests.put("forgotPassword", forgotPasswordBlade);
            guestRequests.put("newEmailAddress", newEmailAddressBlade);
            guestRequests.put("login", loginBlade);
            guestRequests.put("profile", profileBlade);

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
            rb.get(page, asyncContext);
            return;
        }

        RequestBlade rb = guestRequests.get(page);
        if (rb == null) {
            page = "home";
            rb = homeBlade;
        }
        AsyncContext asyncContext = request.startAsync();
        rb.get(page, asyncContext);
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
        if (userId == null && !"validated".equals(page) && !"forgotPassword".equals(page)) {
            page = "login";
        }
        try {
            if ("post".equals(page)) {
                AsyncContext asyncContext = request.startAsync();
                postBlade.post(page, asyncContext, userId);
                return;
            }
            if ("logout".equals(page)) {
                AsyncContext asyncContext = request.startAsync();
                logoutBlade.post(page, asyncContext, userId);
                return;
            }
            if ("deleteAccount".equals(page)) {
                AsyncContext asyncContext = request.startAsync();
                deleteAccountBlade.post(page, asyncContext, userId);
                return;
            }
            if ("changePassword".equals(page)) {
                AsyncContext asyncContext = request.startAsync();
                changePasswordBlade.post(page, asyncContext, userId);
                return;
            }
            if ("validated".equals(page)) {
                AsyncContext asyncContext = request.startAsync();
                validatedBlade.post(page, asyncContext, userId);
                return;
            }
            if ("forgotPassword".equals(page)) {
                AsyncContext asyncContext = request.startAsync();
                forgotPasswordBlade.post(page, asyncContext, userId);
                return;
            }
            if ("newEmailAddress".equals(page)) {
                AsyncContext asyncContext = request.startAsync();
                newEmailAddressBlade.post(page, asyncContext, userId);
                return;
            }
            if ("changeEmailAddress".equals(page)) {
                AsyncContext asyncContext = request.startAsync();
                changeEmailAddressBlade.post(page, asyncContext, userId);
                return;
            }
            if ("login".equals(page) && request.getParameter("newAccount") != null) {
                AsyncContext asyncContext = request.startAsync();
                loginBlade.post(page, asyncContext, userId);
                return;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        if ("login".equals(page))
            login(request, response);
        else
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void login(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        String error = null;
        Map<String, String> map = new HashMap<>();
        String login = request.getParameter("login");
        if (login != null && login.equals("Login")) {
            String emailAddress = request.getParameter("emailAddress");
            String password = request.getParameter("password");
            if (emailAddress == null || emailAddress.length() == 0)
                error = "Email address is required";
            else if (password == null || password.length() == 0)
                error = "Password is required";
            else
                error = User.login(db,
                        servletContext,
                        request,
                        response,
                        emailAddress,
                        password);
            if (emailAddress != null)
                map.put("emailAddress", encode(emailAddress, 0, ENCODE_FIELD)); //field
            if (error != null) {
                map.put("error", encode(error, 0, ENCODE_FIELD)); //field
                response.getWriter().println(replace(servletContext, "login", map));
            } else
                response.sendRedirect("?from=login");
            return;
        }
        String forgotPassword = request.getParameter("forgotPassword");
        if (forgotPassword != null && forgotPassword.equals("Verify Address")) {
            String emailAddress = request.getParameter("emailAddress3");
            if (emailAddress == null || emailAddress.length() == 0)
                error = "Email address is required";
            if (emailAddress != null)
                map.put("emailAddress3", encode(emailAddress, 0, ENCODE_FIELD)); //field
            if (error != null) {
                map.put("error3", encode(error, 0, ENCODE_FIELD)); //field
            } else {
                String userId = User.userId(db, emailAddress, FactoryRegistry.MAX_TIMESTAMP);
                String subject = null;
                String body = null;
                boolean go = true;
                String msg = "An email has been sent to verify your address. Please check your inbox.";
                if (userId == null) {
                    go = false;
                } else {
                    try {
                        String token = Tokens.generate(db, emailAddress,
                                1000 * 60 * 60 * 24 + System.currentTimeMillis()); //1 day validity
                        subject = "Forgot Password";
                        body = "<p>To change your password, please click " +
                                "<a href=\"" + self + "?to=forgotPassword&email=" + emailAddress +
                                "&key=" + token + "\">here</a>.</p>" +
                                "<p>--Virtual Cow</p>";
                    } catch (NoSuchAlgorithmException e) {
                        servletContext.log("no such algorithm: SHA-256");
                        go = false;
                        msg = "Unable to send an email to your address at this time. Please try again later.";
                    }
                }
                if (go) {
                    try {
                        boolean sent = true;
                        sent = MailOut.send(emailAddress, subject, body);
                        if (!sent)
                            msg = "Unable to send an email to your address at this time. Please try again later.";
                    } catch (MessagingException me) {
                        msg = me.getMessage();
                    }
                }
                map.put("success3", encode(msg, 0, ENCODE_FIELD)); //field
            }
            response.getWriter().println(replace(servletContext, "login", map));
            return;
        }
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
