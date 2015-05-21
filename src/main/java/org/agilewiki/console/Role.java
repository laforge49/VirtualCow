package org.agilewiki.console;

import org.agilewiki.console.requests.PostRequestBlade;
import org.agilewiki.console.requests.RequestBlade;

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

    default void dispatchGetRequest(HttpServletRequest request, String userId) {
        throw new UnsupportedOperationException();
    }

    default void dispatchPostRequest(HttpServletRequest request,
                                     HttpServletResponse response,
                                     String userId)
            throws ServletException, IOException {
        throw new UnsupportedOperationException();
    }

    default String menu(HttpServletRequest request, String page, String setTimestamp) {
        throw new UnsupportedOperationException();
    }
}
