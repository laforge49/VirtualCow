package org.agilewiki.console;

import org.agilewiki.console.transactions.*;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.utils.ids.Timestamp;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.BaseRegistry;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.mail.MessagingException;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SimpleSimon extends HttpServlet {
    private Db db;
    ServletConfig servletConfig;
    ServletContext servletContext;
    final static Charset utf8 = Charset.forName("UTF-8");
    final static String SPECIAL = "&<>'\"";
    public final static int ENCODE_MULTIPLE_LINES = 1;
    public final static int ENCODE_SINGLE_LINE = 2;
    public final static int ENCODE_FIELD = 3;

    String readResource(String pageName) throws IOException {
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

    String replace(String pageName, Map<String, String> sub) throws IOException {
        String t = readResource(pageName);
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
            db.registerTransaction(NpjeTransaction.NAME, NpjeTransaction.class);
            db.registerTransaction(ServletStartTransaction.NAME, ServletStartTransaction.class);
            ServletStartTransaction.servletConfig = servletConfig;
            db.registerTransaction(ServletStopTransaction.NAME, ServletStopTransaction.class);
            db.registerTransaction(BadUserAddressTransaction.NAME, BadUserAddressTransaction.class);
            db.registerTransaction(BadUserPasswordTransaction.NAME, BadUserPasswordTransaction.class);
            db.registerTransaction(LoginTransaction.NAME, LoginTransaction.class);
            db.registerTransaction(LogoutTransaction.NAME, LogoutTransaction.class);
            db.registerTransaction(ChangePasswordTransaction.NAME, ChangePasswordTransaction.class);
            db.registerTransaction(NewUserTransaction.NAME, NewUserTransaction.class);
            db.registerTransaction(DeleteTransaction.NAME, DeleteTransaction.class);
            if (Files.exists(dbPath))
                db.open();
            else
                db.open(true);
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
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, String> map = new HashMap<>();
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
        if (userId == null && !"validated".equals(page) && !"forgotPassword".equals(page))
            page = "login";
        else if (page == null || page.equals("home")) {
            page = "home";
            home(map, request);
        } else if (page.equals("journal"))
            journal(map, request);
        else if (page.equals("journalEntry"))
            journalEntry(map, request);
        else if (page.equals("subjects"))
            subjects(map, request);
        else if (page.equals("validated"))
            validated(map, request);
        else if (page.equals("newEmailAddress"))
            newEmailAddress(map, request);
        else if (page.equals("forgotPassword"))
            forgotPassword(map, request);
        response.getWriter().println(replace(page, map));
    }

    void newEmailAddress(Map<String, String> map, HttpServletRequest request) {
        String key = request.getParameter("key");
        String email = request.getParameter("emailAddress");
        map.put("key", key);
        map.put("emailAddress", email);
    }

    void forgotPassword(Map<String, String> map, HttpServletRequest request) {
        String key = request.getParameter("key");
        String email = request.getParameter("email");
        map.put("key", key);
        map.put("email", email);
    }

    void validated(Map<String, String> map, HttpServletRequest request) {
        String key = request.getParameter("key");
        String email = request.getParameter("email");
        map.put("key", key);
        map.put("email", email);
    }

    void home(Map<String, String> map, HttpServletRequest request) throws ServletException {
        String timestamp = request.getParameter("timestamp");
        String dateInString = request.getParameter("date");
        if (dateInString != null && dateInString.length() > 0) {
            Date date;
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            try {
                date = formatter.parse(dateInString);
            } catch (ParseException e) {
                throw new ServletException(e);
            }
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.SECOND, 59);
            long time = calendar.getTimeInMillis() + 999;
            timestamp = TimestampIds.value(TimestampIds.timestampId((time << 10) + 1023));
        }
        if (timestamp == null) {
            map.put("post", "post");
            return;
        }
        map.put("setTimestamp", "&timestamp=" + timestamp);
        map.put("setStartingAt", "&startingAt=" + timestamp);
        map.put("atTime", "at " + niceTime(TimestampIds.generate(timestamp)));
        map.put("clearTime", "<a href=\".\">Clear selected time</a>");
    }

    //MailOut.send("laforge49@gmail.com", "test", "<h1>Hi!</h1>");

    void journalEntry(Map<String, String> map, HttpServletRequest request) {
        String timestamp = request.getParameter("timestamp");
        long longTimestamp;
        if (timestamp != null) {
            map.put("setTimestamp", "&timestamp=" + timestamp);
            map.put("atTime", "at " + niceTime(TimestampIds.generate(timestamp)));
            longTimestamp = TimestampIds.timestamp(TimestampIds.generate(timestamp));
        } else
            longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
        String id = TimestampIds.generate(request.getParameter("jeTimestamp"));
        while (true) {
            try {
                String time = niceTime(id);
                StringBuilder sb = new StringBuilder();
                MapAccessor ma = db.mapAccessor();

                ListAccessor la = ma.listAccessor(id);
                if (la != null) {
                    VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                    if (vmn != null && !vmn.isEmpty(longTimestamp)) {
                        sb.append(time);
                        sb.append("<br />");
                        MapAccessor vma = vmn.mapAccessor(longTimestamp);
                        for (ListAccessor vla : vma) {
                            int sz = vla.size();
                            for (int i = 0; i < sz; ++i) {
                                String s = vla.key() + "[" + i + "] = ";
                                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + s + encode("" + vla.get(i), s.length() + 4, ENCODE_MULTIPLE_LINES)); //body text
                                sb.append("<br />");
                            }
                        }
                    }
                }

                map.put("journalEntry", sb.toString());
                map.put("jeTimestamp", TimestampIds.value(id));
                map.put("time", time);
                return;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    void subjects(Map<String, String> map, HttpServletRequest request) {
        String timestamp = request.getParameter("timestamp");
        long longTimestamp;
        if (timestamp != null) {
            map.put("formTimestamp", "<input type=\"hidden\" name=\"timestamp\" value=\"" + timestamp + "\"/>");
            map.put("setTimestamp", "&timestamp=" + timestamp);
            map.put("atTime", "at " + niceTime(TimestampIds.generate(timestamp)));
            longTimestamp = TimestampIds.timestamp(TimestampIds.generate(timestamp));
        } else
            longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
        String prefix = SecondaryId.SECONDARY_ID + NameIds.SUBJECT;
        String startingAt = request.getParameter("startingAt");
        if (startingAt == null)
            startingAt = "";
        int limit = 25;
        boolean hasMore = false;
        StringBuilder sb = new StringBuilder();
        for (String id : new IdIterable(servletContext, db, prefix, ValueId.generate(startingAt), longTimestamp)) {
            if (limit == 0) {
                hasMore = true;
                startingAt = ValueId.value(id);
                break;
            }
            --limit;
            String line = ValueId.value(id);
            line = line.replaceAll("\r", "");
            if (line.length() > 60)
                line = line.substring(0, 60);
            line = encode(line, 0, ENCODE_SINGLE_LINE); //line text
            sb.append(line);
            sb.append("<br />");
        }
        map.put("subjects", sb.toString());
        map.put("startingAt", hasMore ? encode(startingAt, 0, ENCODE_FIELD) : ""); //field
    }

    void journal(Map<String, String> map, HttpServletRequest request) {
        String timestamp = request.getParameter("timestamp");
        long longTimestamp;
        if (timestamp != null) {
            map.put("setTimestamp", "&timestamp=" + timestamp);
            String timestampId = TimestampIds.generate(timestamp);
            map.put("atTime", "at " + niceTime(timestampId));
            longTimestamp = TimestampIds.timestamp(timestampId);
        }
        longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
        String prefix = Timestamp.PREFIX;
        String startingAt = request.getParameter("startingAt");
        if (startingAt == null)
            startingAt = "";
        int limit = 25;
        boolean hasMore = false;
        StringBuilder sb = new StringBuilder();
        for (String next : new IdIterable(servletContext, db, prefix, startingAt, longTimestamp)) {
            if (limit == 0) {
                hasMore = true;
                startingAt = next;
                break;
            }
            --limit;
            String tsId = TimestampIds.generate(next);
            MapAccessor ma = db.mapAccessor();
            ListAccessor la = ma.listAccessor(tsId);
            VersionedMapNode vmn = (VersionedMapNode) la.get(0);
            sb.append("<a href=\"?from=journal&to=journalEntry&jeTimestamp=" + next);
            if (timestamp != null) {
                sb.append("&timestamp=" + timestamp);
            }
            sb.append("\">" + niceTime(tsId) + "</a>");
            sb.append(' ');
            StringBuilder lb = new StringBuilder();
            String transactionName = vmn.getList(NameIds.TRANSACTION_NAME).flatList(longTimestamp).get(0).toString();
            lb.append(transactionName);
            List subjectList = vmn.getList(NameIds.SUBJECT).flatList(longTimestamp);
            if (subjectList.size() > 0) {
                lb.append(' ');
                String subject = subjectList.get(0).toString();
                lb.append(subject);
                lb.append(" | ");
            }
            List bodyList = vmn.getList(NameIds.BODY).flatList(longTimestamp);
            if (bodyList.size() > 0) {
                if (subjectList.size() == 0) {
                    lb.append(" | ");
                }
                String body = bodyList.get(0).toString();
                lb.append(body);
            }
            String line = lb.toString();
            line = line.replace("\r", "");
            if (line.length() > 60)
                line = line.substring(0, 60);
            line = encode(line, 0, ENCODE_SINGLE_LINE); //line text
            sb.append("<font style=\"font-family:courier\">");
            sb.append(line);
            sb.append("</font>");
            sb.append("<br />");
        }
        map.put("journal", sb.toString());
        map.put("more", hasMore ? "more" : "");
        if (hasMore)
            map.put("setStartingAt", "&startingAt=" + encode(startingAt, 0, ENCODE_FIELD)); //field
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
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
        if ("post".equals(page))
            postJournal(request, response, userId);
        else if ("login".equals(page))
            login(request, response);
        else if ("validated".equals(page))
            validated(request, response);
        else if ("forgotPassword".equals(page))
            forgotPassword(request, response);
        else if ("changePassword".equals(page))
            changePassword(request, response, userId);
        else if ("changeEmailAddress".equals(page))
            changeEmailAddress(request, response, userId);
        else if ("newEmailAddress".equals(page))
            newEmailAddress(request, response, userId);
        else if ("deleteAccount".equals(page))
            deleteAccount(request, response, userId);
        else if ("logout".equals(page))
            logout(request, response, userId);
        else
            throw new ServletException("unknown post: " + page);
    }

    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       String userId)
            throws ServletException, IOException {
        String email = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
        try {
            new LogoutTransaction().update(db, email, userId);
        } catch (Exception e) {
            log("failed update", e);
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        response.sendRedirect("?from=logout");
    }

    public void deleteAccount(HttpServletRequest request,
                              HttpServletResponse response,
                              String userId)
            throws ServletException, IOException {
        String oldPassword = request.getParameter("password");
        Map<String, String> map = new HashMap<>();
        if (oldPassword == null || oldPassword.length() == 0)
            map.put("error", "Enter your password in the old password field");
        else if (!User.confirmPassword(db, servletContext, userId, oldPassword))
            map.put("error", "Incorrect password");
        else {
            String error = null;
            String email = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
            try {
                new DeleteTransaction().update(
                        db,
                        userId,
                        email);
            } catch (Exception e) {
                error = "system error--unable to update database";
                log("update failure", e);
            }
            if (error == null) {
                try {
                    MailOut.send(email,
                            "Delete Account Notification",
                            "<p>Your account has been deleted.</p>" +
                                    "<p>--Virtual Cow</p>");
                } catch (MessagingException me) {
                    log("unable to send to " + email, me);
                }
                response.sendRedirect("?to=login&from=deleteAccount");
                return;
            } else
                map.put("error", error);
        }
        response.getWriter().println(replace("changePassword", map));
    }

    public void changePassword(HttpServletRequest request,
                               HttpServletResponse response,
                               String userId)
            throws ServletException, IOException {
        String oldPassword = request.getParameter("old");
        String newPassword = request.getParameter("new");
        String confirmNewPassword = request.getParameter("confirm");
        Map<String, String> map = new HashMap<>();
        if (oldPassword == null || oldPassword.length() == 0)
            map.put("error", "Enter your password in the old password field");
        else if (newPassword == null || newPassword.length() == 0)
            map.put("error", "Enter your new password in the new password field");
        else if (!newPassword.equals(confirmNewPassword))
            map.put("error", "The new password and confirm new password fields must be the same");
        else if (oldPassword.equals(newPassword))
            map.put("error", "The old password and new password fields must not be the same");
        else if (!User.confirmPassword(db, servletContext, userId, oldPassword))
            map.put("error", "Enter your current password in the old password field");
        else {
            String error = null;
            try {
                new ChangePasswordTransaction().update(
                        db,
                        userId,
                        User.encodePassword(servletContext, userId, newPassword));
            } catch (Exception e) {
                error = "system error--unable to update database";
                log("update failure", e);
            }
            if (error == null) {
                User.send(db,
                        servletContext,
                        userId,
                        "Password Change Notification",
                        "<p>Your password has been changed.</p>" +
                                "<p>--Virtual Cow</p>");
                map.put("success", "The password has been changed.");
            } else
                map.put("error", error);
        }
        response.getWriter().println(replace("changePassword", map));
    }

    public void forgotPassword(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        String email = request.getParameter("email");
        String newPassword = request.getParameter("new");
        String confirmNewPassword = request.getParameter("confirm");
        Map<String, String> map = new HashMap<>();
        if (newPassword == null || newPassword.length() == 0)
            map.put("error", "Enter your new password in the new password field");
        else if (!newPassword.equals(confirmNewPassword))
            map.put("error", "The new password and confirm new password fields must be the same");
        else {
            boolean go = true;
            try {
                go = Tokens.validate(db, email, key);
            } catch (NoSuchAlgorithmException e) {
                servletContext.log("no such algorithm: SHA-256");
                go = false;
            }
            if (!go) {
                String error = "Unable to change your password at this time. Please try again later.";
                map.put("error", encode(error, 0, ENCODE_FIELD)); //field
            } else {
                String error = null;
                String userId = User.userId(db, email, FactoryRegistry.MAX_TIMESTAMP);
                try {
                    new ChangePasswordTransaction().update(
                            db,
                            userId,
                            User.encodePassword(servletContext, userId, newPassword));
                } catch (Exception e) {
                    error = "system error--unable to update database";
                    log("update failure", e);
                }
                if (error == null) {
                    String subject = "Password Change Notification";
                    String body = "<p>Your password has been changed.</p>" +
                            "<p>--Virtual Cow</p>";
                    map.put("success", "The password has been changed");
                    try {
                        MailOut.send(email, subject, body);
                    } catch (MessagingException me) {
                        log("unable to send to " + email, me);
                    }
                    map.put("success", "The password has been changed and you can now " +
                            "<a href=\"?from=validated&to=login\">login</a>.");
                } else
                    map.put("error", error);
            }
        }
        response.getWriter().println(replace("forgotPassword", map));
    }

    public void validated(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        String email = request.getParameter("email");
        String password = request.getParameter("new");
        String confirm = request.getParameter("confirm");
        String error = null;
        if (password == null || password.length() == 0)
            error = "Enter your new password in the new password field";
        else if (!password.equals(confirm))
            error = "The new password and confirm new password fields must be the same";
        Map<String, String> map = new HashMap<>();
        if (error != null) {
            map.put("error", encode(error, 0, ENCODE_FIELD)); //field
        } else {
            boolean go = true;
            try {
                go = Tokens.validate(db, email, key);
            } catch (NoSuchAlgorithmException e) {
                servletContext.log("no such algorithm: SHA-256");
                go = false;
            }
            if (!go) {
                error = "Unable to create your account at this time. Please try again later.";
                map.put("error", encode(error, 0, ENCODE_FIELD)); //field
            } else {
                String userId = RandomIds.randomId.generate();
                String passwordHash = User.encodePassword(servletContext, userId, password);
                if (passwordHash == null) {
                    error = "Unable to create your account at this time. Please try again later.";
                    map.put("error", encode(error, 0, ENCODE_FIELD)); //field
                } else {
                    try {
                        new NewUserTransaction().update(
                                db,
                                userId,
                                email,
                                passwordHash,
                                User.USER_TYPE_ID);
                    } catch (Exception e) {
                        go = false;
                        log("failed update", e);
                    }
                    if (!go) {
                        error = "Unable to create your account at this time. Please try again later.";
                        map.put("error", encode(error, 0, ENCODE_FIELD)); //field
                    } else {
                        map.put("success", "Your account has been created and you can now " +
                                "<a href=\"?from=validated&to=login\">login</a>.");
                    }
                }
            }
        }
        response.getWriter().println(replace("validated", map));
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
                response.getWriter().println(replace("login", map));
            } else
                response.sendRedirect("?from=login");
            return;
        }
        String newAccount = request.getParameter("newAccount");
        if (newAccount != null && newAccount.equals("Validate Address")) {
            String emailAddress = request.getParameter("emailAddress2");
            if (emailAddress == null || emailAddress.length() == 0)
                error = "Email address is required";
            if (emailAddress != null)
                map.put("emailAddress2", encode(emailAddress, 0, ENCODE_FIELD)); //field
            if (error != null) {
                map.put("error2", encode(error, 0, ENCODE_FIELD)); //field
            } else {
                String userId = User.userId(db, emailAddress, FactoryRegistry.MAX_TIMESTAMP);
                String subject = null;
                String body = null;
                boolean go = true;
                if (userId == null) {
                    String self = servletConfig.getInitParameter("self");
                    try {
                        String token = Tokens.generate(db, emailAddress,
                                1000 * 60 * 60 * 24 + System.currentTimeMillis()); //1 day validity
                        subject = "Address Validation Request";
                        body = "<p>To validate your email address and begin opening an account, please click " +
                                "<a href=\"" + self + "?to=validated&email=" + emailAddress +
                                "&key=" + token + "\">here</a>.</p>" +
                                "<p>--Virtual Cow</p>";
                    } catch (NoSuchAlgorithmException e) {
                        servletContext.log("no such algorithm: SHA-256");
                        go = false;
                    }
                } else {
                    subject = "New Account Notification";
                    body = "<p>An attempt was made to open another account with your email address.</p>" +
                            "<p>--Virtual Cow</p>";
                }
                boolean sent = true;
                try {
                    sent = MailOut.send(emailAddress, subject, body);
                    if (go && sent)
                        map.put("success2", encode(
                                "An email has been sent to verify your address. Please check your inbox.",
                                0, ENCODE_FIELD)); //field
                    else
                        map.put("success2", encode(
                                "Unable to send an email to your address at this time. Please try again later.",
                                0, ENCODE_FIELD)); //field
                } catch (MessagingException me) {
                    map.put("success2", encode(me.getMessage(), 0, ENCODE_FIELD)); //field
                }
            }
            response.getWriter().println(replace("login", map));
            return;
        }
        String forgotPassword = request.getParameter("forgotPassword");
        if (forgotPassword != null && forgotPassword.equals("Revalidate Address")) {
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
                    String self = servletConfig.getInitParameter("self");
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
            response.getWriter().println(replace("login", map));
            return;
        }
    }

    public void newEmailAddress(HttpServletRequest request,
                                HttpServletResponse response,
                                String userId)
            throws ServletException, IOException {
        String emailAddress = request.getParameter("emailAddress");
        String token = request.getParameter("key");
        String password = request.getParameter("password");
        Map<String, String> map = new HashMap<>();
        map.put("emailAddress", encode(emailAddress, 0, ENCODE_FIELD)); //field
        map.put("key", encode(token, 0, ENCODE_FIELD)); //field
        if (password == null || password.length() == 0) {
            String error = "Password is required";
            map.put("error", error);
            response.getWriter().println(replace("newEmailAddress", map));
            return;
        }
        if (!User.confirmPassword(db, servletContext, userId, password)) {
            map.put("error", "Password does not match.");
            response.getWriter().println(replace("newEmailAddress", map));
            return;
        }
        Boolean go = false;
        try {
            go = Tokens.validate(db, userId + emailAddress, token);
        } catch (NoSuchAlgorithmException e) {
        }
        if (!go) {
            map.put("success", "Unable to update your account at this time. Please try again later.");
            response.getWriter().println(replace("newEmailAddress", map));
            return;
        }
        String subject = "Address change notification";
        String body = "<p>Your account is now tied to your new email address: " +
                emailAddress + "</p>" +
                "<p>--Virtual Cow</p>";
        go = User.send(db, servletContext, userId, subject, body);
        if (!go)
            log("Unable to send email");
        map.put("success", "The email address for your account has been updated.");
        response.getWriter().println(replace("newEmailAddress", map));
    }

    public void changeEmailAddress(HttpServletRequest request,
                                   HttpServletResponse response,
                                   String userId)
            throws ServletException, IOException {
        String emailAddress = request.getParameter("emailAddress");
        Map<String, String> map = new HashMap<>();
        if (emailAddress != null)
            map.put("emailAddress", encode(emailAddress, 0, ENCODE_FIELD)); //field
        String error = null;
        String oldEmailAddress = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
        if (emailAddress == null || emailAddress.length() == 0) {
            error = "Enter your new email address.";
        } else if (emailAddress.equals(oldEmailAddress)) {
            error = "Your new email address should not be the same as your old email address.";
        }
        if (error != null) {
            map.put("error", error);
            response.getWriter().println(replace("changeEmailAddress", map));
            return;
        }
        String subject = null;
        String body = null;
        String userId2 = User.userId(db, emailAddress, FactoryRegistry.MAX_TIMESTAMP);
        if (userId2 != null) {
            subject = "Notification of attempt to reassign email address";
            body = "<p>There was an attempt made to change the email of an account " +
                    "from " + oldEmailAddress + " to your email account." +
                    "<p>--Virtual Cow</p>";
            boolean sent = false;
            try {
                sent = MailOut.send(emailAddress, subject, body);
            } catch (MessagingException e) {
            }
            String msg;
            if (sent)
                msg = "An email has been sent to verify your new address. Please check your inbox.";
            else {
                msg = "Unable to send an email to your address at this time. Please try again later.";
            }
            map.put("success", msg);
            response.getWriter().println(replace("changeEmailAddress", map));
            return;
        }
        long expTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24; // 1 day
        String token = null;
        try {
            token = Tokens.generate(db, userId + emailAddress, expTime);
        } catch (NoSuchAlgorithmException e) {
        }
        if (token == null) {
            String msg = "Unable to send an email to your address at this time. Please try again later.";
            map.put("success", msg);
            response.getWriter().println(replace("changeEmailAddress", map));
            return;
        }
        String self = servletConfig.getInitParameter("self");
        String href = self +
                "?to=newEmailAddress&emailAddress=" + emailAddress +
                "&key=" + token;
        subject = "Address Validation Request";
        body = "<p>To validate your new email address, please click " +
                "<a href=\"" + href + "\">here</a>.</p>" +
                "<p>--Virtual Cow</p>";
        boolean sent = false;
        try {
            sent = MailOut.send(emailAddress, subject, body);
        } catch (MessagingException e) {
        }
        if (!sent) {
            String msg = "Unable to send an email to your address at this time. Please try again later.";
            map.put("success", msg);
            response.getWriter().println(replace("changeEmailAddress", map));
            return;
        }
        String msg = "An email has been sent to verify your address. Please check your inbox.";
        map.put("success", msg);
        response.getWriter().println(replace("changeEmailAddress", map));
    }

    public void postJournal(HttpServletRequest request,
                            HttpServletResponse response,
                            String userId)
            throws ServletException, IOException {
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
        Map<String, String> map = new HashMap<>();
        map.put("body", encode(body, 0, ENCODE_FIELD)); //text area
        if (subject.length() > 0)
            map.put("subject", encode(subject, 0, ENCODE_FIELD)); //field
        try {
            String timestampId = new NpjeTransaction().update(db, subject, body, request, userId);
            map.put("success", "posted: " + niceTime(timestampId));
        } catch (Exception e) {
            throw new ServletException("transaction exception", e);
        }
        response.getWriter().println(replace("post", map));
    }

    public String encode(String s, int indent, int mode) {
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

    public String niceTime(String timestampId) {
        Date date = new Date(Timestamp.time(timestampId));
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
        return formatter.format(date);
    }
}
