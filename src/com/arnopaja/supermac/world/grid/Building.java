package com.arnopaja.supermac.world.grid;

import com.arnopaja.supermac.world.objects.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Building extends GameMap {

    private final Grid[] floors;
    // tells which floor is at ground level
    // number refers to the array index of the first floor
    private final int firstFloorIndex;

    public Building(String name, Grid[] floors, int firstFloorIndex) {
        super(name, false);
        this.floors = floors;
        this.firstFloorIndex = firstFloorIndex;
    }

    @Override
    public Collection<Entity> getEntities() {
        List<Entity> entities = new ArrayList<Entity>();
        for (Grid floor : floors) {
            entities.addAll(floor.getEntities());
        }
        return entities;
    }

    @Override
    public void clear() {
        for (Grid floor : floors) {
            floor.clear();
        }
    }

    @Override
    public Grid getGrid(int floorNumber) {
        return floors[floorNumber - 1 + firstFloorIndex];
    }

    public int getFloorCount() {
        return floors.length;
    }

    public int getFirstFloorIndex() {
        return firstFloorIndex;
    }
}
