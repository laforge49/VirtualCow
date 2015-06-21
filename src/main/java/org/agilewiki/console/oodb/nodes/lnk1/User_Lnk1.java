package org.agilewiki.console.oodb.nodes.lnk1;

public class User_Lnk1 extends Lnk1_NodeInstance {
    private static User_Lnk1 user_lnk1;

    public static void create() {
        user_lnk1 = new User_Lnk1("$nuser.lnk1", "$nlnk1.node");
    }

    public User_Lnk1(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
