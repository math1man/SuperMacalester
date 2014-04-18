package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.inventory.GenericItem;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Chest extends Entity {

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

    private Inventory contents;

    public Chest(Location location, ChestColor color, Inventory contents) {
        super(true, location, true);
        this.contents = contents;
        this.color = color;
    }

    @Override
    public void update(float delta) {
        // does nothing
    }

    public void removeItem(GenericItem item) {
        contents.take(item);
    }

    public boolean isEmpty() {
        return contents.isEmpty();
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

    public Inventory getContents() {
        return contents;
    }

    public void setContents(Inventory contents) {
        this.contents = contents;
    }

    public TextureRegion getSprite() {
        return isOpen ? color.open : color.closed;
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return getSprite();
    }

    @Override
    public Interaction toInteraction() {
        final Chest chest = this;
        return new Interaction(chest) {
            public void run(GameScreen screen) {
                chest.open();
                Dialogue dialogue;
                if (chest.isEmpty()) {
                    dialogue = new DialogueText(chest.closeInteraction(), "This chest is empty");
                } else {
                    // Items go into inventory from chest
                    List<GenericItem> items = chest.getContents().getAll();
                    int length = items.size() + 2;

                    Object[] objects = Arrays.copyOf(items.toArray(), length);
                    objects[length - 2] = "All";
                    objects[length - 1] = "Close";

                    Interaction[] interactions = new Interaction[length];
                    interactions[length - 2] = Interaction.NULL;
                    for (int i=0; i<length-2; i++) {
                        Interaction temp = chest.takeItemInteraction(items.get(i));
                        interactions[i] = Interaction.combine(temp, chest.toInteraction());
                        interactions[length - 2] = Interaction.combine(interactions[length - 2], temp);
                    }
                    interactions[length - 1] = chest.closeInteraction();

                    dialogue = new DialogueOptions("Take items?", objects, interactions);
                }
                screen.getDialogueHandler().displayDialogue(dialogue);
            }
        };
    }

    private Interaction takeItemInteraction(final GenericItem item) {
        final Chest chest = this;
        return new Interaction(item, chest) {
            @Override
            public void run(GameScreen screen) {
                chest.removeItem(item);          // take item from chest
                Inventory.getMain().store(item); // place in inventory
            }
        };
    }

    private Interaction closeInteraction() {
        final Chest chest = this;
        return new Interaction(chest) {
            public void run(GameScreen screen) {
                Dialogue.CLEAR_DIALOGUE.run(screen);
                chest.close();
            }
        };
    }

    public static class Parser extends Entity.Parser<Chest> {
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
            JsonObject json = toBaseJson(object);
            addString(json, "color", object.color.name());
            addObject(json, "contents", object.contents, Inventory.class);
            return json;
        }
    }
}
