package com.arnopaja.supermac.world.grid;

/**
 * @author Ari Weiland
 */
public class Building {

    private final String name;

    // tells which floor is at ground level
    // number refers to the array index, not the actual floor number
    // the ground floor is defined as the first floor
    private int groundFloorIndex;
    private Grid[] floors;

    public Building(String name, int floorCount, int groundFloorIndex, int floorWidth, int floorHeight) {
        this.name = name;
        floors = new Grid[floorCount];
        for (Grid floor : floors) {
            floor = new Grid(floorWidth, floorHeight);
        }
        this.groundFloorIndex = groundFloorIndex;
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

    public void setFloorByIndex(int floorIndex, Grid grid) {
        floors[floorIndex] = grid;
    }

    /**
     * Returns the specified floor by floor number.
     * The ground floor is assumed to be floor one, so a call to getFloorByNumber(1) will always
     * return the ground floor, whether or not there is a basement below it.
     *
     * @param floorNumber the number of the floor.  The ground floor specified
     *                    by groundFloorIndex is defined as the first floor
     * @return the Grid representing the specificed floor
     */
    public Grid getFloorByNumber(int floorNumber) {
        return floors[floorNumber-1+groundFloorIndex];
    }

    public int getFloorCount() {
        return floors.length;
    }

    public int getGroundFloorIndex() {
        return groundFloorIndex;
    }

    public void setGroundFloorIndex(int groundFloorIndex) {
        this.groundFloorIndex = groundFloorIndex;
    }

    public String getName() {
        return name;
    }
}
