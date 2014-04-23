package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.inventory.GenericItem;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

/**
 * @author Ari Weiland
 */
public class GarbageCan extends Container {

    protected GarbageCan(Location location, Inventory contents) {
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
                if (items.isEmpty()) {
                    screen.getDialogueHandler().displayDialogue(new DialogueText("This garbage can is empty."));
                } else if (items.size() == 1) {
                    screen.getDialogueHandler().displayDialogue(new DialogueText("You find a " + items.get(0) + "."));
                } else {
                    screen.getDialogueHandler().displayDialogue(new DialogueText("You find a lot of items!"));
                }
            }
        };
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return AssetLoader.garbageCan;
    }
}
