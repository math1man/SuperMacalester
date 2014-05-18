package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.dialogue.DialogueStep;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.interaction.MultiInteraction;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.inventory.GenericItem;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Chest extends Container {

    public static enum ChestColor {
        BROWN(AssetLoader.chestBrownClosed, AssetLoader.chestBrownOpen),
        RED(AssetLoader.chestRedClosed, AssetLoader.chestRedOpen),
        GREEN(AssetLoader.chestGreenClosed, AssetLoader.chestGreenOpen);

        public transient final TextureRegion closed;
        public transient final TextureRegion open;

        ChestColor(TextureRegion closed, TextureRegion open) {
            this.closed = closed;
            this.open = open;
        }

        public static ChestColor getColor(String name) {
            switch (name.toLowerCase().charAt(0)) {
                case 'g':
                    return GREEN;
                case 'r':
                    return RED;
                case 'b':
                default:
                    return BROWN;
            }
        }
    }

    private final ChestColor color;
    private boolean isOpen = false;

    public Chest(Location location, ChestColor color, Inventory contents) {
        super(location, contents);
        this.color = color;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }

    public TextureRegion getSprite() {
        return isOpen ? color.open : color.closed;
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return getSprite();
    }

    @Override
    public void run(GameScreen screen) {
        open();
        Dialogue dialogue;
        if (isEmpty()) {
            dialogue = new DialogueStep("This chest is empty", closeInteraction(), DialogueStyle.WORLD);
        } else {
            // Items go into inventory from chest
            List<GenericItem> items = getContents().getAll();
            List<Object> objects = new ArrayList<Object>(items);
            objects.add("All");
            objects.add("Close");

            List<Interaction> interactions = new ArrayList<Interaction>(objects.size());
            MultiInteraction all = new MultiInteraction();
            for (GenericItem item : items) {
                Interaction temp = takeItemInteraction(item);
                interactions.add(new MultiInteraction(temp, this));
                all.attach(temp);
            }
            interactions.add(all);
            interactions.add(closeInteraction());
            dialogue = new DialogueOptions("Take items?", objects, interactions, DialogueStyle.WORLD);
        }
        dialogue.run(screen);
    }

    private Interaction closeInteraction() {
        final Chest chest = this;
        return new Interaction() {
            public void run(GameScreen screen) {
                Interactions.END_DIALOGUE.run(screen);
                chest.close();
            }
        };
    }

    public static class Parser extends SuperParser<Chest> {
        @Override
        public Chest fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            String color = getString(object, "color");
            Inventory contents = getObject(object, "contents", Inventory.class);
            return new Chest(location, Chest.ChestColor.getColor(color), contents);
        }

        @Override
        public JsonElement toJson(Chest object) {
            JsonObject json = new JsonObject();
            addObject(json, object.getLocation(), Location.class);
            addString(json, "color", object.color.name());
            addObject(json, "contents", object.getContents(), Inventory.class);
            return json;
        }
    }
}
