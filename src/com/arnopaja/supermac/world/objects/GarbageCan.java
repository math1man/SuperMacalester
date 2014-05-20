package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.dialogue.DialogueStep;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.SuperParser;
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

    private final String color;
    private final TextureRegion sprite;

    public GarbageCan(Location location, Inventory contents, String color) {
        super(location, contents);
        this.color = color;
        this.sprite = AssetLoader.getSprite(color + " garbage can");
    }

    @Override
    public void run(GameScreen screen) {
        List<GenericItem> items = getContents().getAll();
        for (GenericItem item : items) {
            takeItemInteraction(item);
        }
        DialogueStep dialogue;
        if (items.isEmpty()) {
            dialogue = new DialogueStep("This garbage can is empty.", DialogueStyle.WORLD);
        } else if (items.size() == 1) {
            dialogue = new DialogueStep("You find a " + items.get(0) + ".", DialogueStyle.WORLD);
        } else {
            dialogue = new DialogueStep("You find a lot of items!", DialogueStyle.WORLD);
        }
        dialogue.run(screen);
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return sprite;
    }


    public static class Parser extends SuperParser<GarbageCan> {
        @Override
        public GarbageCan fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            Inventory contents = getObject(object, "contents", Inventory.class);
            String color = getString(object, "color");
            return new GarbageCan(location, contents, color);
        }

        @Override
        public JsonElement toJson(GarbageCan object) {
            JsonObject json = new JsonObject();
            addObject(json, object.getLocation(), Location.class);
            addObject(json, "contents", object.getContents(), Inventory.class);
            addString(json, "color", object.color);
            return json;
        }
    }
}
