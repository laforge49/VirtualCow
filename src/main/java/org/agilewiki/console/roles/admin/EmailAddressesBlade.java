package org.agilewiki.console.roles.admin;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.awdb.nodes.Key_NodeFactory;
import org.agilewiki.console.roles.Role;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.immutable.collections.PeekABoo;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class EmailAddressesBlade extends RequestBlade {
    public EmailAddressesBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
        return "Email Addresses";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String startingAt = request.getParameter("startingAt");
                if (startingAt == null)
                    startingAt = "";
                boolean hasMore = false;
                StringBuilder sb;
                while (true) {
                    try {
                        hasMore = false;
                        int limit = 25;
                        sb = new StringBuilder();
                        PeekABoo<String> idPeekABoo = awDb.keyValueIdIterable(Key_NodeFactory.EMAIL_ID, longTimestamp);
                        idPeekABoo.setPosition(startingAt);
                        for (String valueId : idPeekABoo) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = valueId;
                                break;
                            }
                            --limit;
                            String nodeId = awDb.getKeyTargetId(Key_NodeFactory.EMAIL_ID, valueId, longTimestamp);
                            sb.append("<a href=\"?from=secondaryKeys&to=user&nodeId=");
                            sb.append(nodeId);
                            if (timestamp != null) {
                                sb.append("&timestamp=");
                                sb.append(timestamp);
                            }
                            sb.append(setRole + "#rupa\">");
                            sb.append(ValueId.value(valueId));
                            sb.append("</a>");
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("emailAddresses", sb.toString());
                map.put("startingAt", hasMore ? SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD) : ""); //field
                finish();
            }
        }.signal();
    }
}
