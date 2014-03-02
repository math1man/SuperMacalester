package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Building;
import com.arnopaja.supermac.grid.Grid;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class Door extends Entity {

    private Building building;
    private int floor;
    private Vector2 landingSpace;

    public Door(Grid grid, Vector2 position, Building building, int floor, Vector2 landingSpace) {
        super(false, grid, position, null, true);
        this.building = building;
        this.floor = floor;
        this.landingSpace = landingSpace;
    }

    @Override
    public void update(float delta) {
        // nothing happens
    }

    @Override
    public void interact(MainMapCharacter character) {
        character.changeGrid(building.getFloorByNumber(floor), landingSpace);
    }
}
