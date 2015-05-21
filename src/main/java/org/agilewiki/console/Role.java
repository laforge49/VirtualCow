package org.agilewiki.console;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A command set and menus.
 */
public interface Role {
    RequestBlade requestBlade(String page);

    PostRequestBlade postRequestBlade(String page);

    default RequestBlade getDefaultRequestBlade() {
        throw new UnsupportedOperationException();
    }

    default String getDefaultRequestPage() {
        throw new UnsupportedOperationException();
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

    default String menu(HttpServletRequest request, String page, String setTimestamp) {
        throw new UnsupportedOperationException();
    }
}
