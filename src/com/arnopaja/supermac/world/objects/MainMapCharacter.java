package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.battle.characters.MainParty;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class MainMapCharacter extends MapCharacter {

    private Direction movingDirection = null;
    private final MainParty party;

    public MainMapCharacter(Location location) {
        this(location, Direction.WEST);
    }

    public MainMapCharacter(Location location, Direction direction) {
        super(location, direction, true, AssetLoader.getCharacter("Tom"));
        party = new MainParty();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (continueMoving()) {
            super.move(movingDirection);
        }
    }

    @Override
    public boolean move(Direction direction) {
        movingDirection = direction;
        return super.move(direction);
    }

    public void stop() {
        movingDirection = null;
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }

    public boolean continueMoving() {
        return movingDirection != null;
    }

    public MainParty getParty() {
        return party;
    }

    public void addToParty(Hero hero) {
        party.addCharacter(hero);
    }

    @Override
    public Interaction toInteraction() {
        Entity entity = getLocation().getAdjacent(getDirection()).getEntity();
        if (entity != null) {
            Interaction interaction = entity.interact();
            if (!interaction.equals(Interaction.NULL) && entity instanceof MapCharacter) {
                ((MapCharacter) entity).setDirection(entity.getDirectionToward(getPosition()));
            }
            return interaction;
        }
        return Interaction.NULL;
    }

    public static class Parser extends Entity.Parser<MainMapCharacter> {
        @Override
        public MainMapCharacter fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            Direction direction = Direction.WEST;
            if (has(object, Direction.class)) {
                direction = getObject(object, Direction.class);
            }
            return new MainMapCharacter(location, direction);
        }

        @Override
        public JsonElement toJson(MainMapCharacter object) {
            JsonObject json = toBaseJson(object);
            addObject(json, object.getDirection(), Direction.class);
            return json;
        }
    }
}
