package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.inventory.AbstractItem;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
    }

    private final TextureRegion closedSprite;
    private final TextureRegion openSprite;
    private boolean isOpen = false;

    private List<AbstractItem> contents;

    public Chest(Location location, ChestColor color, List<AbstractItem> contents) {
        super(true, location, true);
        this.contents = contents;
        closedSprite = color.closed;
        openSprite = color.open;
        setInteraction(Interaction.openChest(this));
    }

    @Override
    public void update(float delta) {
        // does nothing
    }

    public void removeItem(AbstractItem item) {
        contents.remove(item);
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public List<AbstractItem> getContents() {
        return contents;
    }

    public void setContents(List<AbstractItem> contents) {
        this.contents = contents;
    }

    public TextureRegion getSprite() {
        return isOpen ? openSprite : closedSprite;
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return getSprite();
    }
}
