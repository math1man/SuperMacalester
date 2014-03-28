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

    // TODO: do we maybe want chests facing other directions?
    private static final TextureRegion CLOSED_SPRITE = AssetLoader.chestClosed;
    private static final TextureRegion OPEN_SPRITE = AssetLoader.chestOpen;
    private boolean isOpen = false;

    // TODO: should chests contain money?
    private List<AbstractItem> contents;

    public Chest(Location location, List<AbstractItem> contents) {
        super(true, location, true);
        this.contents = contents;
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

    @Override
    public TextureRegion getSprite(float runTime) {
        return isOpen ? OPEN_SPRITE : CLOSED_SPRITE;
    }
}
