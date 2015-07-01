package org.agilewiki.vcow.roles.developer;

import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.SimpleSimon;
import org.agilewiki.vcow.roles.Role;
import org.agilewiki.awdb.db.immutable.collections.PeekABoo;
import org.agilewiki.awdb.db.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Inverted bi-directional links.
 */
public class InvLinksBlade extends RequestBlade {
    public InvLinksBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
        return "Inverted Links";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            String targetId;
            String labelId;

            @Override
            protected String setContext() {
                targetId = request.getParameter("nodeId");
                labelId = request.getParameter("linkType");
                map.put("nodeId", targetId);
                map.put("linkType", labelId);
                map.put("--nodeId", targetId.substring(2));
                map.put("--linkType", labelId.substring(2));
                return "&nodeId=" + targetId +
                        "&linkType=" + labelId;
            }

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
                        PeekABoo<String> peekABoo = awDb.originIdIterable(targetId, labelId, longTimestamp);
                        peekABoo.setPosition(startingAt);
                        for (String nodeId : peekABoo) {
                            --limit;
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = nodeId;
                                break;
                            }

                            if (!nodeId.startsWith("$t")) {
                                String kindId = awDb.kindId(nodeId, longTimestamp);
                                sb.append(kindId.substring(2));

                                sb.append(" ");

                                sb.append("<a href=\"?from=nodes&to=node&nodeId=");
                                sb.append(nodeId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "\">");
                                sb.append(nodeId.substring(2));
                                sb.append("</a>");
                            }

                            if (nodeId.startsWith("$t")) {
                                String transactionName = (String) awDb.get(nodeId, NameIds.TRANSACTION_NAME, longTimestamp);
                                if (transactionName != null) {
                                    sb.append(transactionName + ".node");
                                    sb.append(' ');
                                }

                                sb.append("<a href=\"?from=nodes&to=node&nodeId=");
                                sb.append(nodeId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "\">");
                                sb.append(SimpleSimon.niceTime(nodeId));
                                sb.append("</a>");
                            }
                            StringBuilder lb = new StringBuilder();
                            String subject = (String) awDb.get(nodeId, NameIds.SUBJECT, longTimestamp);
                            if (subject != null) {
                                lb.append(' ');
                                lb.append(subject);
                                lb.append(" | ");
                            }
                            String body = (String) awDb.get(nodeId, NameIds.BODY, longTimestamp);
                            if (body != null) {
                                if (subject == null) {
                                    lb.append(" | ");
                                }
                                lb.append(body);
                            }
                            String line = lb.toString();
                            line = line.replace("\r", "");
                            if (line.length() > 60)
                                line = line.substring(0, 60);
                            line = SimpleSimon.encode(line, 0, SimpleSimon.ENCODE_SINGLE_LINE); //line text
                            sb.append(line);
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("invLinks", sb.toString());
                map.put("setStartingAt", hasMore ? "&startingAt=" + startingAt : "");
                map.put("more", hasMore ? "<img src=\"images/morebutton.jpg\"/>" : "");
                finish();
            }
        }.signal();
    }
}
