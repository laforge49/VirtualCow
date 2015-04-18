package org.agilewiki.console;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.utils.immutable.BaseRegistry;
import org.agilewiki.utils.virtualcow.Db;

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
import java.util.Map;

public class SimpleSimon extends HttpServlet {
    private Db db;
    ServletConfig servletConfig;
    ServletContext servletContext;
    Charset utf8 = Charset.forName("UTF-8");

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
            db.open(false);
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
        response.getWriter().println(replace(page, map));
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
        Map<String, String> map = new HashMap<>();
        map.put("subject", subject);
        map.put("body", body);
        if (subject.length() == 0)
            map.put("error", "Subject is required");
        else {
            try {
                String timestampId = new NpjeTransaction().update(db, subject, body, request);
                map.put("success", "posted: " + timestampId);
            } catch (Exception e) {
                throw new ServletException("transaction exception", e);
            }
        }
        response.getWriter().println(replace("post", map));
    }
}
