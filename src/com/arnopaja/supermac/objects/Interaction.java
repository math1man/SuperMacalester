package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.helpers.Dialogue;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.screen.WorldScreen;

/**
 * This class dictates how an interaction proceeds via its runInteraction method
 *
 * @author Ari Weiland
 */
public abstract class Interaction {

    private final Entity entity;
    private final MainMapCharacter character;

    public Interaction(Entity entity, MainMapCharacter character) {
        this.entity = entity;
        this.character = character;
    }

    public static Interaction getNullInteraction() {
        return new Interaction(null, null) {
            @Override
            public void runInteraction(WorldScreen screen, DialogueHandler dialogueHandler) {}
        };
    }

    public static Interaction getDialogueInteraction(Entity entity, MainMapCharacter character, final Dialogue dialogue) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(WorldScreen screen, DialogueHandler dialogueHandler) {
                screen.dialogue();
                dialogueHandler.displayDialogue(dialogue);
            }
        };
    }

    public static Interaction getBattleInteraction(Entity entity, MainMapCharacter character) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(WorldScreen screen, DialogueHandler dialogueHandler) {
                screen.goToBattle();
            }
        };
    }

    public static Interaction getPreBattleInteraction(Entity entity, MainMapCharacter character, final Dialogue dialogue) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(WorldScreen screen, DialogueHandler dialogueHandler) {
                screen.prebattle();
                dialogueHandler.displayDialogue(dialogue);
            }
        };
    }

    public abstract void runInteraction(WorldScreen screen, DialogueHandler dialogueHandler);

    public Entity getEntity() {
        return entity;
    }

    public MainMapCharacter getCharacter() {
        return character;
    }
}