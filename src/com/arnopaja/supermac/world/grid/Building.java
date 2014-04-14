package com.arnopaja.supermac.world.grid;

/**
 * @author Ari Weiland
 */
public class Building {

    private final Grid[] floors;
    // tells which floor is at ground level
    // number refers to the array index of the first floor
    private final int firstFloorIndex;

    public Building(Grid[] floors, int firstFloorIndex) {
        this.floors = floors;
        this.firstFloorIndex = firstFloorIndex;
    }

    /**
     * Returns the specified floor by the array index
     *
     * @param floorIndex the array index of the requested floor
     * @return the Grid representing the specified floor
     */
    public Grid getFloorByIndex(int floorIndex) {
        return floors[floorIndex];
    }

    /**
     * Returns the specified floor by floor number.
     * The ground floor is assumed to be floor one, so a call to getFloorByNumber(1) will always
     * return the ground floor, whether or not there is a basement below it.
     *
     * @param floorNumber the number of the floor.  The ground floor specified
     *                    by firstFloorIndex is defined as the first floor
     * @return the Grid representing the specificed floor
     */
    public Grid getFloorByNumber(int floorNumber) {
        return getFloorByIndex(getIndex(floorNumber, firstFloorIndex));
    }

    public static int getIndex(int floorNumber, int firstFloorIndex) {
        return floorNumber - 1 + firstFloorIndex;
    }

    public static int getFloorNumber(int floorIndex, int firstFloorIndex) {
        return floorIndex + 1 - firstFloorIndex;
    }

    public int getFloorCount() {
        return floors.length;
    }

    public int getFirstFloorIndex() {
        return firstFloorIndex;
    }
}
