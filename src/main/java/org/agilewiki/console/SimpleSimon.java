package org.agilewiki.console;

import org.agilewiki.console.oodb.OODb;
import org.agilewiki.console.oodb.nodes.Metadata_Node;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.console.oodb.nodes.roles.system.ServletStart_Node;
import org.agilewiki.console.oodb.nodes.roles.system.ServletStart_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.system.ServletStop_Node;
import org.agilewiki.console.oodb.nodes.roles.system.ServletStop_NodeInstance;
import org.agilewiki.console.oodb.nodes.roles.user.User_Role;
import org.agilewiki.console.oodb.nodes.roles.visitor.Visitor_Role;
import org.agilewiki.utils.ids.Timestamp;
import org.agilewiki.utils.virtualcow.Db;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class SimpleSimon extends HttpServlet {

    public final static String self = System.getProperties().getProperty("self", "http://localhost/");

    final static Charset utf8 = Charset.forName("UTF-8");
    final static String SPECIAL = "&<>'\"";
    public final static int ENCODE_MULTIPLE_LINES = 1;
    public final static int ENCODE_SINGLE_LINE = 2;
    public final static int ENCODE_FIELD = 3;
    public static SimpleSimon simpleSimon;

    public OODb ooDb;
    public Db db;
    protected ServletConfig servletConfig;
    public ServletContext servletContext;
    public MailOut mailOut;

    public Map<String, Role> roles = new TreeMap<String, Role>();

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

    public SimpleSimon() {
        simpleSimon = this;
    }

    public void init() {
        try {
            servletConfig = getServletConfig();
            servletContext = servletConfig.getServletContext();

            ooDb = new OODb(100000, 10000L);
            db = ooDb.db;

            mailOut = new MailOut();

            try {
                Metadata_Node.create();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            ServletStart_Node.create();
            ooDb.registerTransaction(ServletStart_NodeInstance.NAME, ServletStart_NodeInstance.class);

            ServletStop_Node.create();
            ooDb.registerTransaction(ServletStop_NodeInstance.NAME, ServletStop_NodeInstance.class);

            ServletStart_NodeInstance.update(ooDb);
        } catch (Exception ex) {
            destroy();
        }
    }

    public void destroy() {
        try {
            ServletStop_NodeInstance.update(ooDb);
        } catch (Exception e) {
        }
        ooDb.close();
    }

    public void doGet(HttpServletRequest request,
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
            userId = Tokens.parse(userIdToken);
        if (userId == null) {
            Visitor_Role.get().dispatchGetRequest(request, null);
        } else {
            String roleName = request.getParameter("role");
            if (roleName == null || !User.hasRole(userId, roleName))
                roleName = "profile";
            Role role = roles.get(roleName);
            if (role == null) {
                role = User_Role.get();
            }
            role.dispatchGetRequest(request, userId);
        }
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
            userId = Tokens.parse(userIdToken);

        if (userId == null) {
            Visitor_Role.get().dispatchPostRequest(request, response, null);
        } else {
            String roleName = request.getParameter("role");
            if (roleName == null || !User.hasRole(userId, roleName))
                roleName = "profile";
            Role role = roles.get(roleName);
            if (role == null) {
                role = User_Role.get();
            }
            role.dispatchPostRequest(request, response, userId);
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
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        return formatter.format(date);
    }
}
