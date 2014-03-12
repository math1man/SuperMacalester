package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Building;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class Door extends Entity {

    private final Building building;
    private final int floor;
    private final Vector2 landingSpace;

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
    public Interaction getInteraction(final MainMapCharacter character) {
        return new Interaction() {
            @Override
            public void run(GameScreen screen, DialogueHandler dialogueHandler) {
                character.changeGrid(building.getFloorByNumber(floor), landingSpace);
            }
        };
    }

    public Building getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    public Vector2 getLandingSpace() {
        return landingSpace;
    }
}
