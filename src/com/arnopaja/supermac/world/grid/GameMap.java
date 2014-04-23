package com.arnopaja.supermac.world.grid;

import com.arnopaja.supermac.world.objects.Entity;

import java.util.Collection;

/**
 * @author Ari Weiland
 */
public abstract class GameMap {

    private final String name;
    private final boolean isGrid;

    protected GameMap(String name, boolean isGrid) {
        this.name = name;
        this.isGrid = isGrid;
    }

    public abstract Collection<Entity> getEntities();

    public abstract void clear();

    public abstract Grid getGrid(int index);

    public String getName() {
        return name;
    }

    public boolean isGrid() {
        return isGrid;
    }
}
