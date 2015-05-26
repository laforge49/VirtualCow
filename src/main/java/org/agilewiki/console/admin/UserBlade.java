package org.agilewiki.console.admin;

import org.agilewiki.console.*;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Journal;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.List;

/**
 * Request for a journal entry.
 */
public class UserBlade extends RequestBlade {
    public UserBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    protected String groupName() {
        return "admin";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            String nodeId;

            @Override
            protected String setContext() {
                nodeId = request.getParameter("nodeId");
                map.put("nodeId", nodeId);
                return "&nodeId=" + nodeId;
            }

            @Override
            protected void process()
                    throws Exception {
                String email = User.email(db, nodeId, longTimestamp);
                map.put("email", email);
                StringBuilder sb;
                while (true) {
                    try {
                        sb = new StringBuilder();
                        sb.append("<table>\n");
                        sb.append("<caption>Roles ");
                        if (timestamp == null) {
                            sb.append("<a href=\"?from=");
                            sb.append(page);
                            sb.append("&to=editRoles&nodeId=");
                            sb.append(nodeId);
                            sb.append("&role=");
                            sb.append(roleName);
                            sb.append("\">(edit)</a>");
                        }
                        sb.append("</caption>\n");
                        for (String role: simpleSimon.roles.keySet()) {
                            if (role.equals("unRole"))
                                break;
                            sb.append("<tr>");
                            sb.append("<td>");
                            if (SecondaryId.hasSecondaryId(
                                    db,
                                    nodeId,
                                    SecondaryId.secondaryId(User.ROLE_ID, NameId.generate(role)),
                                    longTimestamp))
                                sb.append("x");
                            else
                                sb.append("&nbsp;");
                            sb.append("</td>");
                            sb.append("<td>");
                            sb.append(role);
                            sb.append("</td>");
                            sb.append("</tr>\n");
                        }
                        sb.append("</table>\n");
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("user", sb.toString());
                finish();
            }
        }.signal();
    }
}
