package org.agilewiki.vcow.roles.admin;

import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.User_NodeInstance;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.awdb.db.ids.NameId;
import org.agilewiki.awdb.db.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Request for a journal entry.
 */
public class UserBlade extends RequestBlade {
    public UserBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
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
                User_NodeInstance user_nodeInstance = (User_NodeInstance) awDb.fetchNode(nodeId, longTimestamp);
                String email = user_nodeInstance.getEmailAddress();
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
                            if (!roleName.equals("visitor") && !roleName.equals("system")) {
                                String niceRoleName = simpleSimon.roles.get(roleName).niceRoleName();
                                sb.append("<tr>");
                                sb.append("<td>");
                                if (awDb.nodeHasValueId(nodeId,
                                        NameIds.ROLE_ID,
                                        NameId.generate(roleName),
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
