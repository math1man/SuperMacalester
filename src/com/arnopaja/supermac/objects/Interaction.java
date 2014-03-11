package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.helpers.Dialogue;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.render.BattleInterface;
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

    public static Interaction getDialogueInteraction(Dialogue dialogue) {
        return getDialogueInteraction(null, null, dialogue);
    }

    public static Interaction getDialogueInteraction(Entity entity, MainMapCharacter character,
                                                     final Dialogue dialogue) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(WorldScreen screen, DialogueHandler dialogueHandler) {
                screen.dialogue();
                dialogueHandler.displayDialogue(dialogue);
            }
        };
    }

    public static Interaction getBattleInteraction(BattleInterface battle) {
        return getBattleInteraction(null, null, battle);
    }

    public static Interaction getBattleInteraction(Entity entity, MainMapCharacter character,
                                                   final BattleInterface battle) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(WorldScreen screen, DialogueHandler dialogueHandler) {
                screen.goToBattle(battle);
            }
        };
    }

    public static Interaction getPreBattleInteraction(Dialogue dialogue, BattleInterface battle) {
        return getPreBattleInteraction(null, null, dialogue, battle);
    }

    public static Interaction getPreBattleInteraction(Entity entity, MainMapCharacter character,
                                                      final Dialogue dialogue, final BattleInterface battle) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(WorldScreen screen, DialogueHandler dialogueHandler) {
                screen.prebattle(battle);
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
