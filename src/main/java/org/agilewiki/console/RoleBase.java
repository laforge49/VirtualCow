package org.agilewiki.console;

/**
 * Base class for roles.
 */
abstract public class RoleBase implements Role {
    public final SimpleSimon simpleSimon;

    public RoleBase(SimpleSimon simpleSimon) {
        this.simpleSimon = simpleSimon;
    }
}
