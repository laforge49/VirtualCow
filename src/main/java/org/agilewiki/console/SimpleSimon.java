package org.agilewiki.console;

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
import java.util.List;
import java.util.Map;

public class SimpleSimon extends HttpServlet {
    private Db db;
    ServletConfig servletConfig;
    ServletContext servletContext;
    final static Charset utf8 = Charset.forName("UTF-8");
    final static String SPECIAL = "&<>'\"";

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
            int k = t.indexOf('{', i);
            if (k == -1)
                break;
            if (k > i)
                sb.append(t.substring(i, k));
            int j = t.indexOf('}', k);
            String n = t.substring(k + 1, j);
            i = j + 1;
            if (sub.containsKey(n))
                sb.append(sub.get(n));
        }
        if (i < t.length())
            sb.append(t.substring(i));
        return sb.toString();
    }

    public void init() {
        try {
            new Plant();
            Path dbPath = Paths.get("vcow.db");
            int maxRootBlockSize = 100000;
            db = new Db(new BaseRegistry(), dbPath, maxRootBlockSize);
            db.registerTransaction(NpjeTransaction.NAME, NpjeTransaction.class);
            if (Files.exists(dbPath))
                db.open();
            else
                db.open(true);
            servletConfig = getServletConfig();
            servletContext = servletConfig.getServletContext();
        } catch (Exception ex) {
            destroy();
        }
    }

    public void destroy() {
        db.close();
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, String> map = new HashMap<>();
        String page = request.getParameter("to");
        if (page == null)
            page = "home";
        else if (page.equals("journal"))
            journal(map, request);
        else if (page.equals("journalEntry"))
            journalEntry(map, request);
        else if (page.equals("subjects"))
            subjects(map, request);
        response.getWriter().println(replace(page, map));
    }

    void journalEntry(Map<String, String> map, HttpServletRequest request) {
        String id = request.getParameter("id");
        long timestamp = FactoryRegistry.MAX_TIMESTAMP;
        while (true) {
            try {
                StringBuilder sb = new StringBuilder();
                MapAccessor ma = db.mapAccessor();

                ListAccessor la = ma.listAccessor(id);
                if (la != null) {
                    VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                    if (vmn != null && !vmn.isEmpty(timestamp)) {
                        sb.append(niceTime(id));
                        sb.append("<br />");
                        MapAccessor vma = vmn.mapAccessor(timestamp);
                        for (ListAccessor vla : vma) {
                            int sz = vla.size();
                            for (int i = 0; i < sz; ++i) {
                                String s = vla.key() + "[" + i + "] = ";
                                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + s + encode("" + vla.get(i), s.length() + 4, true));
                                sb.append("<br />");
                            }
                        }
                    }
                }

                map.put("journalEntry", sb.toString());
                return;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    void subjects(Map<String, String> map, HttpServletRequest request) {
        long timestamp = FactoryRegistry.MAX_TIMESTAMP;
        String prefix = SecondaryId.SECONDARY_ID + NameIds.SUBJECT;
        String startingAt = request.getParameter("startingAt");
        if (startingAt == null)
            startingAt = "";
        int limit = 25;
        boolean hasMore = false;
        StringBuilder sb = new StringBuilder();
        for (String id: new IdIterable(servletContext, db, prefix, ValueId.generate(startingAt), timestamp)) {
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
            line = encode(line, 0, false);
            sb.append(line);
            sb.append("<br />");
        }
        map.put("subjects", sb.toString());
        map.put("startingAt", hasMore ? startingAt : "");
    }

    void journal(Map<String, String> map, HttpServletRequest request) {
        long timestamp = FactoryRegistry.MAX_TIMESTAMP;
        String prefix = Timestamp.PREFIX;
        String oldLast = request.getParameter("last");
        int limit = 25;
        if (oldLast == null || !oldLast.startsWith(prefix))
            oldLast = prefix;
        boolean haveMore = true;
        while (true) {
            try {
                String last = oldLast;
                StringBuilder sb = new StringBuilder();
                MapAccessor ma = db.mapAccessor();
                int jc = 0;

                String next = last;
                while (true) {
                    Comparable hk = ma.higherKey(next);
                    if (hk == null) {
                        last = prefix + "~~~";
                        haveMore = false;
                        break;
                    }
                    next = hk.toString();
                    if (!next.startsWith(prefix)) {
                        last = prefix + "~~~";
                        haveMore = false;
                        break;
                    }
                    ListAccessor la = ma.listAccessor(next);
                    if (la == null)
                        continue;
                    VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                    if (vmn.isEmpty(timestamp))
                        continue;
                    if (++jc > limit) {
                        break;
                    }
                    String tsId = la.key().toString();
                    sb.append(
                            "<a href=\"?from=journal&to=journalEntry&id=" +
                                    tsId +
                                    "\">" +
                                    niceTime(tsId) +
                                    "</a>");
                    sb.append(' ');
                    StringBuilder lb = new StringBuilder();
                    String transactionName = vmn.getList(NameIds.TRANSACTION_NAME).flatList(timestamp).get(0).toString();
                    lb.append(transactionName);
                    List subjectList = vmn.getList(NameIds.SUBJECT).flatList(timestamp);
                    if (subjectList.size() > 0) {
                        lb.append(' ');
                        String subject = subjectList.get(0).toString();
                        lb.append(subject);
                    }
                    lb.append(" | ");
                    List bodyList = vmn.getList(NameIds.BODY).flatList(timestamp);
                    if (bodyList.size() > 0) {
                        String body = bodyList.get(0).toString();
                        lb.append(body);
                    }
                    String line = lb.toString();
                    line = line.replace((CharSequence) "\r", (CharSequence) "");
                    if (line.length() > 60)
                        line = line.substring(0, 60);
                    line = encode(line, 0, false);
                    sb.append("<font style=\"font-family:courier\">");
                    sb.append(line);
                    sb.append("</font>");
                    sb.append("<br />");
                    last = la.key().toString();
                }

                map.put("more", haveMore ? "more" : "");
                map.put("last", last);
                map.put("journal", sb.toString());
                return;
            } catch (UnexpectedChecksumException uce) {
            }
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
        Map<String, String> map = new HashMap<>();
        map.put("body", encode(body, 0, false));
        try {
            if (subject.length() > 0)
                map.put("subject", encode(subject, 0, false));
            String timestampId = new NpjeTransaction().update(db, subject, body, request);
            map.put("success", "posted: " + timestampId);
        } catch (Exception e) {
            throw new ServletException("transaction exception", e);
        }
        response.getWriter().println(replace("post", map));
    }

    public String encode(String s, int indent, boolean lines) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            String a;
            switch (c) {
                case '\n':
                    if (lines) {
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
                    a = "&nbsp;";
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
