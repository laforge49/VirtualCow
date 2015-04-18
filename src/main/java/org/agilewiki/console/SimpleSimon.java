package org.agilewiki.console;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.utils.ids.Timestamp;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
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
        while(true) {
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
            String n = t.substring(k+1, j);
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
            db.open();
            servletConfig = getServletConfig();
            servletContext = servletConfig.getServletContext();
        } catch(Exception ex) {
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
            journal(map);
        response.getWriter().println(replace(page, map));
    }

    void journal(Map<String, String> map) {
        while (true) {
            try {
                StringBuilder sb = new StringBuilder();
                MapAccessor ma = db.mapAccessor();
                long timestamp = FactoryRegistry.MAX_TIMESTAMP;
                for (ListAccessor la: ma.iterable(Timestamp.PREFIX)) {
                    VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                    if (!vmn.isEmpty(timestamp)) {
                        String tsId = la.key().toString();
                        sb.append(tsId);
                        sb.append("<br />");
                        MapAccessor vma = vmn.mapAccessor(timestamp);
                        for (ListAccessor vla: vma) {
                            int sz = vla.size();
                            for (int i = 0; i < sz; ++i) {
                                String s = vla.key() + "[" + i + "] = ";
                                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + s + encode("" + vla.get(i), s.length() + 4));
                                sb.append("<br />");
                            }
                        }
                    }
                }
                map.put("journal", sb.toString());
                return;
            } catch (UnexpectedChecksumException uce) {}
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
        map.put("body", encode(body, 0));
        if (subject.length() == 0)
            map.put("error", "Subject is required");
        else {
            try {
                map.put("subject", encode(subject, 0));
                String timestampId = new NpjeTransaction().update(db, subject, body, request);
                map.put("success", "posted: " + timestampId);
            } catch (Exception e) {
                throw new ServletException("transaction exception", e);
            }
        }
        response.getWriter().println(replace("post", map));
    }

    public String encode(String s, int indent) {
        StringBuilder sb = new StringBuilder();
        for (char c: s.toCharArray()) {
            String a;
            switch(c) {
                case '\n' :
                    a = "<br />";
                    for (int i = 0; i < indent; ++i) {
                        a += "&nbsp;";
                    }
                    break;
                case '&' :
                    a = "&amp;";
                    break;
                case '<' :
                    a = "&lt;";
                    break;
                case '>' :
                    a = "&gt;";
                    break;
                case '\'' :
                    a = "&apos;";
                    break;
                case '"' :
                    a = "&quot;";
                    break;
                default:
                    a = "" + c;
            }
            sb.append(a);
        }
        return sb.toString();
    }
}
