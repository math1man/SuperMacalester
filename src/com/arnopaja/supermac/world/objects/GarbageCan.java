package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.inventory.GenericItem;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * @author Ari Weiland
 */
public class GarbageCan extends Container {

    public GarbageCan(Location location, Inventory contents) {
        super(location, contents);
    }

    @Override
    public Interaction toInteraction() {
        final GarbageCan can = this;
        return new Interaction(can) {
            @Override
            public void run(GameScreen screen) {
                List<GenericItem> items = can.getContents().getAll();
                for (GenericItem item : items) {
                    can.takeItemInteraction(item);
                }
                DialogueText dialogue;
                if (items.isEmpty()) {
                    dialogue = new DialogueText("This garbage can is empty.");
                } else if (items.size() == 1) {
                    dialogue = new DialogueText("You find a " + items.get(0) + ".");
                } else {
                    dialogue = new DialogueText("You find a lot of items!");
                }
                dialogue.toInteraction().run(screen);
            }
        };
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return AssetLoader.garbageCan;
    }


    public static class Parser extends Entity.Parser<GarbageCan> {
        @Override
        public GarbageCan fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            Inventory contents = getObject(object, "contents", Inventory.class);
            return new GarbageCan(location, contents);
        }

        @Override
        public JsonElement toJson(GarbageCan object) {
            JsonObject json = toBaseJson(object);
            addObject(json, "contents", object.getContents(), Inventory.class);
            return json;
        }
    }
}