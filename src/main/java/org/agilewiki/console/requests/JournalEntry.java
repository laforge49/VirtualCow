package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.ExceptionHandler;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.Db;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Request for home page.
 */
public class JournalEntry extends NonBlockingBladeBase {
    ServletContext servletContext;
    Db db;

    public JournalEntry(ServletContext servletContext, Db db) throws Exception {
        this.servletContext = servletContext;
        this.db = db;
    }

    public ASig display(AsyncContext asyncContext) {
        return new ASig("displayJournalEntry") {
            HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

            @Override
            protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                                 AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                _asyncRequestImpl.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public Object processException(Exception e) throws Exception {
                        servletContext.log("displayJournalEntry", e);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        asyncContext.complete();
                        _asyncResponseProcessor.processAsyncResponse(null);
                        return null;
                    }
                });
                Map<String, String> map = new HashMap<>();

                String timestamp = request.getParameter("timestamp");
                long longTimestamp;
                if (timestamp != null) {
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
                    longTimestamp = TimestampIds.timestamp(TimestampIds.generate(timestamp));
                } else
                    longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
                String id = TimestampIds.generate(request.getParameter("jeTimestamp"));
                while (true) {
                    try {
                        String time = SimpleSimon.niceTime(id);
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
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + s +
                                                SimpleSimon.encode("" + vla.get(i), s.length() + 4,
                                                        SimpleSimon.ENCODE_MULTIPLE_LINES)); //body text
                                        sb.append("<br />");
                                    }
                                }
                            }
                        }

                        map.put("journalEntry", sb.toString());
                        map.put("jeTimestamp", TimestampIds.value(id));
                        map.put("time", time);
                        response.getWriter().println(SimpleSimon.replace(servletContext, "journalEntry", map));
                        response.setStatus(HttpServletResponse.SC_OK);
                        asyncContext.complete();
                        _asyncResponseProcessor.processAsyncResponse(null);
                        return;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
            }
        };
    }
}
