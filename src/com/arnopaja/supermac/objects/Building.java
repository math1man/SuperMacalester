package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.BuildingGrid;

/**
 * @author Ari Weiland
 */
public class Building {

    // tells which floor is at ground level
    // number refers to the array index, not the actual floor number
    // the ground floor is defined as the first floor
    private int groundFloorIndex;
    private BuildingGrid[] floors;

    public Building(int floorCount, int groundFloorIndex, int floorWidth, int floorHeight) {
        floors = new BuildingGrid[floorCount];
        for (BuildingGrid floor : floors) {
            floor = new BuildingGrid(floorWidth, floorHeight);
        }
        this.groundFloorIndex = groundFloorIndex;
    }

    /**
     * Returns the specified floor by the array index
     *
     * @param floorIndex the array index of the requested floor
     * @return the BuildingGrid representing the specified floor
     */
    public BuildingGrid getFloorByIndex(int floorIndex) {
        return floors[floorIndex];
    }

    /**
     * Returns the specified floor by floor number.
     * The ground floor is assumed to be floor one, so a call to getFloorByNumber(1) will always
     * return the ground floor, whether or not there is a basement below it.
     *
     * @param floorNumber the number of the floor.  The ground floor specified
     *                    by groundFloorIndex is defined as the first floor
     * @return the BuildingGrid representing the specificed floor
     */
    public BuildingGrid getFloorByNumber(int floorNumber) {
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
}
