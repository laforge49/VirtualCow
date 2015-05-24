package org.agilewiki.console;

import org.agilewiki.console.admin.AdminRole;
import org.agilewiki.console.maintenance.MaintenanceRole;
import org.agilewiki.console.profile.ProfileRole;
import org.agilewiki.console.unRole.UnRole;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.utils.ids.Timestamp;
import org.agilewiki.utils.immutable.BaseRegistry;
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
    protected ServletConfig servletConfig;
    public ServletContext servletContext;
    final static Charset utf8 = Charset.forName("UTF-8");
    final static String SPECIAL = "&<>'\"";
    public final static int ENCODE_MULTIPLE_LINES = 1;
    public final static int ENCODE_SINGLE_LINE = 2;
    public final static int ENCODE_FIELD = 3;

    public MailOut mailOut;

    public Map<String, Role> roles = new HashMap<String, Role>();
    public UnRole unRole;
    public ProfileRole profileRole;
    public MaintenanceRole maintenanceRole;
    public AdminRole adminRole;

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

            if (Files.exists(dbPath))
                db.open();
            else
                db.open(true);

            mailOut = new MailOut();

            try {
                unRole = new UnRole(this);
                roles.put(unRole.roleName(), unRole);
                profileRole = new ProfileRole(this);
                roles.put(profileRole.roleName(), profileRole);
                maintenanceRole = new MaintenanceRole(this);
                roles.put(maintenanceRole.roleName(), maintenanceRole);
                adminRole = new AdminRole(this);
                roles.put(adminRole.roleName(), adminRole);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            db.registerTransaction(ServletStartTransaction.NAME, ServletStartTransaction.class);
            ServletStartTransaction.simpleSimon = this;

            db.registerTransaction(ServletStopTransaction.NAME, ServletStopTransaction.class);

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
        if (userId == null) {
            unRole.dispatchGetRequest(request, null);
        } else {
            String roleName = request.getParameter("role");
            if (roleName == null || !User.hasRole(db, userId, roleName))
                roleName = "profile";
            Role role = roles.get(roleName);
            if (role == null) {
                role = profileRole;
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
            userId = Tokens.parse(db, userIdToken);

        if (userId == null) {
            unRole.dispatchPostRequest(request, response, null);
        } else {
            String roleName = request.getParameter("role");
            if (roleName == null || !User.hasRole(db, userId, roleName))
                roleName = "profile";
            Role role = roles.get(roleName);
            if (role == null) {
                role = profileRole;
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
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
        return formatter.format(date);
    }
}
