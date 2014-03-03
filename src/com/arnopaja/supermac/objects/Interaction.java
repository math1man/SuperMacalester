package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.helpers.DialogueHandler;

/**
 * This class dictates how an interaction proceeds via its runInteraction method
 *
 * @author Ari Weiland
 */
public abstract class Interaction {

    // for all Interactions
    private final Entity entity;
    private final MainMapCharacter character;

    public Interaction(Entity entity, MainMapCharacter character) {
        this.entity = entity;
        this.character = character;
    }

    public static Interaction getNullInteraction() {
        return new Interaction(null, null) {
            @Override
            public void runInteraction(DialogueHandler dialogueHandler) {}
        };
    }

    public static Interaction getDialogueInteraction(Entity entity, MainMapCharacter character, final String dialogue) {
        return new Interaction(entity, character) {
            @Override
            public void runInteraction(DialogueHandler dialogueHandler) {
                // TODO: uncomment the following line when DialogueHandler is up and running
//                dialogueHandler.displayDialogue(dialogue);
                System.out.println(dialogue);
            }
        };
    }

    public abstract void runInteraction(DialogueHandler dialogueHandler);

    public Entity getEntity() {
        return entity;
    }

    public MainMapCharacter getCharacter() {
        return character;
    }
}
