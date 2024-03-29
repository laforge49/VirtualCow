package org.agilewiki.vcow.roles;

import org.agilewiki.vcow.PostRequestBlade;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.SimpleSimon;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * A command set and menus.
 */
public interface Role {
    SimpleSimon simpleSimon();

    String roleName();

    String niceRoleName();

    Map<String, RequestBlade> requests();

    Map<String, PostRequestBlade> posts();

    RequestBlade requestBlade(String page);

    PostRequestBlade postRequestBlade(String page);

    RequestBlade getDefaultRequestBlade();

    default String getDefaultRequestPage() {
        return getDefaultRequestBlade().page;
    }

    default void dispatchGetRequest(HttpServletRequest request, String userId) {
        String page = request.getParameter("to");
        if (page == null) {
            page = getDefaultRequestPage();
        }
        RequestBlade rb = requestBlade(page);
        if (rb == null) {
            page = getDefaultRequestPage();
            rb = getDefaultRequestBlade();
        }
        AsyncContext asyncContext = request.startAsync();
        rb.get(page, asyncContext, userId, this);
    }

    default void dispatchPostRequest(HttpServletRequest request,
                                     HttpServletResponse response,
                                     String userId)
            throws ServletException, IOException {
        String page = request.getParameter("to");
        PostRequestBlade rb = postRequestBlade(page);
        if (rb == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        AsyncContext asyncContext = request.startAsync();
        rb.post(page, asyncContext, userId, this);
    }

    default String menu(HttpServletRequest request,
                        String page,
                        String setTimestamp,
                        String timestamp,
                        String setRole) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a>");
        sb.append(niceRoleName());
        sb.append(" Tools &#9660;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>\n");
        sb.append("<ul class=\"sub-menu\">\n");

        menuItems(sb, page, setTimestamp, timestamp, setRole);

        sb.append("</ul>\n");
        return sb.toString();
    }

    default void menuItems(StringBuilder sb,
                           String currentPage,
                           String setTimestamp,
                           String timestamp,
                           String setRole) {
    }

    default void menuItem(StringBuilder sb,
                          String currentPage,
                          String setTimestamp,
                          String setRole,
                          RequestBlade requestBlade) {
        menuItem(sb, currentPage, setTimestamp, setRole, requestBlade, false);
    }

    default void menuItem(StringBuilder sb,
                          String currentPage,
                          String setTimestamp,
                          String setRole,
                          RequestBlade requestBlade,
                          boolean post) {
        String toPage = requestBlade.page;
        String niceName = requestBlade.niceName();
        sb.append("<li>");
        if (post) {
            sb.append("<a>");
        } else {
            sb.append("<a href=\"?from=");
            sb.append(currentPage);
            sb.append("&to=");
            sb.append(toPage);
            sb.append(setTimestamp);
            sb.append(setRole);
            sb.append("#rupa\">");
        }
        if (toPage.equals(currentPage))
            sb.append("<strong>");
        sb.append(niceName);
        if (toPage.equals(currentPage))
            sb.append("</strong>");
        sb.append("</a>");
        sb.append("</li>\n");
    }
}
