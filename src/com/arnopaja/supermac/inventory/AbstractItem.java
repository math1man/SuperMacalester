package com.arnopaja.supermac.inventory;

/**
 * @author Ari Weiland
 */
public abstract class AbstractItem {

    private final int universalID;
    private final String name;
    private final int value;

    protected AbstractItem(int universalID, String name, int value) {
        this.universalID = universalID;
        this.name = name;
        this.value = value;
    }

    public int getUniversalID() {
        return universalID;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getName();
    }
}
