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
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie: cookies) {
                if(cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                    break;
                }
            }
        if (userId == null)
            page = "login";
        else if (page == null || page.equals("home")) {
            page = "home";
            home(map, request);
        }
        else if (page.equals("journal"))
            journal(map, request);
        else if (page.equals("journalEntry"))
            journalEntry(map, request);
        else if (page.equals("subjects"))
            subjects(map, request);
        response.getWriter().println(replace(page, map));
    }

    void home(Map<String, String> map, HttpServletRequest request) throws ServletException {
        String timestamp = request.getParameter("timestamp");
        String dateInString = request.getParameter("date");
        if (dateInString != null && dateInString.length() > 0) {
            log(dateInString);
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
        String page = request.getParameter("to");
        if ("post".equals(page))
            postJournal(request, response);
        else if ("login".equals(page))
            login(request, response);
        else if ("logout".equals(page))
            logout(request, response);
        else
            throw new ServletException("unknown post: " + page);
    }

    public void logout(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie: cookies) {
                if(cookie.getName().equals("userId")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        response.sendRedirect("?from=logout");
    }

    public void login(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        String emailAddress = request.getParameter("emailAddress");
        String password = request.getParameter("password");
        String error = null;
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
        Map<String, String> map = new HashMap<>();
        if (emailAddress != null)
            map.put("emailAddress", encode(emailAddress, 0, ENCODE_FIELD)); //field
        if (error != null) {
            map.put("error", encode(error, 0, ENCODE_FIELD)); //field
            response.getWriter().println(replace("login", map));
        } else
            response.sendRedirect("?from=login");
    }

    public void postJournal(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
        Map<String, String> map = new HashMap<>();
        map.put("body", encode(body, 0, ENCODE_FIELD)); //text area
        if (subject.length() > 0)
            map.put("subject", encode(subject, 0, ENCODE_FIELD)); //field
        try {
            String timestampId = new NpjeTransaction().update(db, subject, body, request);
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
