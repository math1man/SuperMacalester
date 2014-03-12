package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.helpers.Dialogue;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.render.BattleController;
import com.arnopaja.supermac.screen.BaseScreen;

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
            public void runInteraction(BaseScreen screen, DialogueHandler dialogueHandler) {}
        };
    }

    public static Interaction getDialogueInteraction(Dialogue dialogue) {
        return getDialogueInteraction(null, null, dialogue);
    }

    public static Interaction getDialogueInteraction(Entity entity, MainMapCharacter character,
                                                     final Dialogue dialogue) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(BaseScreen screen, DialogueHandler dialogueHandler) {
                screen.dialogue();
                dialogueHandler.displayDialogue(dialogue);
            }
        };
    }

    public static Interaction getBattleInteraction(BattleController battle) {
        return getBattleInteraction(null, null, battle);
    }

    public static Interaction getBattleInteraction(Entity entity, MainMapCharacter character,
                                                   final BattleController battle) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(BaseScreen screen, DialogueHandler dialogueHandler) {
                screen.goToBattle(battle);
            }
        };
    }

    public static Interaction getPreBattleInteraction(Dialogue dialogue, BattleController battle) {
        return getPreBattleInteraction(null, null, dialogue, battle);
    }

    public static Interaction getPreBattleInteraction(Entity entity, MainMapCharacter character,
                                                      final Dialogue dialogue, final BattleController battle) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(BaseScreen screen, DialogueHandler dialogueHandler) {
                screen.prebattle(battle);
                dialogueHandler.displayDialogue(dialogue);
            }
        };
    }

    public abstract void runInteraction(BaseScreen screen, DialogueHandler dialogueHandler);

    public Entity getEntity() {
        return entity;
    }

    public MainMapCharacter getCharacter() {
        return character;
    }
}
