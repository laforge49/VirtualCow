package org.agilewiki.console.oodb.nodes.lnk1;

public class Target_Lnk1 extends Lnk1_NodeInstance {
    private static Target_Lnk1 target_lnk1;

    public static Target_Lnk1 get() {
        return target_lnk1;
    }

    public static void create() {
        target_lnk1 = new Target_Lnk1("$ntarget.lnk1", "$nlnk1.node");
    }

    public Target_Lnk1(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
