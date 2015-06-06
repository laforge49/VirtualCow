package org.agilewiki.console.admin;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Request for a journal entry.
 */
public class UserBlade extends RequestBlade {
    public UserBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    protected String niceName() {
        return "User";
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
                        sb.append("<table><tr>");
                        sb.append("<td><p>Roles</p></td>");
                        if (timestamp == null) {
                            sb.append("<td><a href=\"?from=");
                            sb.append(page);
                            sb.append("&to=editRoles&nodeId=");
                            sb.append(nodeId);
                            sb.append("&role=");
                            sb.append(roleName);
                            sb.append("#rupa\"><img src=\"images/editbutton.jpg\" /></a></td>");
                        }
                        sb.append("</tr></table>\n");
                        sb.append("<table>\n");
                        for (String roleName : simpleSimon.roles.keySet()) {
                            if (!roleName.equals("unRole")) {
                                String niceRoleName = simpleSimon.roles.get(roleName).niceRoleName();
                                sb.append("<tr>");
                                sb.append("<td>");
                                if (SecondaryId.hasSecondaryId(
                                        db,
                                        nodeId,
                                        SecondaryId.secondaryId(User.ROLE_ID, NameId.generate(roleName)),
                                        longTimestamp))
                                    sb.append("&#10004;");
                                else
                                    sb.append("&nbsp;");
                                sb.append("</td>");
                                sb.append("<td>");
                                sb.append(niceRoleName);
                                sb.append("</td>");
                                sb.append("</tr>\n");
                            }
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
