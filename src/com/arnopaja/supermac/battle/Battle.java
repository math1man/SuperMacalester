package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.battle.characters.EnemyParty;
import com.arnopaja.supermac.battle.characters.MainParty;
import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.InteractionUtils;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class controls what is going on in a battle.
 *
 * @author Ari Weiland
 */
public class Battle implements Controller {

    private static final Random battleRandomGen = new Random();

    private final DialogueHandler dialogueHandler;

    private final MainParty mainParty;
    private final EnemyParty enemyParty;
    private final boolean isBossFight;
    private final TextureRegion background;
    private final Queue<BattleAction> actionQueue;

    /**
     * Constructs a battle that is identical to the specified battle.
     * @param battle
     */
    public Battle(Battle battle) {
        this(battle.mainParty, battle.enemyParty, battle.dialogueHandler);
    }

    /**
     * Constructs a battle that is identical to the specified battle,
     * except instead with the specified dialogue handler.
     * @param battle
     * @param dialogueHandler
     */
    public Battle(Battle battle, DialogueHandler dialogueHandler) {
        this(battle.mainParty, battle.enemyParty, dialogueHandler);
    }

    /**
     * Constructs a battle with no associated dialogue handler.
     * WARNING: Calling the update method on battles with no associated
     * dialogue handler will throw null pointer exceptions.  Instead,
     * attach a dialogue handler should be attached by instantiating
     * new Battle(Battle, DialogueHandler).
     * @param mainParty
     * @param enemyParty
     */
    public Battle(MainParty mainParty, EnemyParty enemyParty) {
        this(mainParty, enemyParty, null);
    }

    /**
     * The base Battle constructor.
     * @param mainParty
     * @param enemyParty
     * @param dialogueHandler
     */
    public Battle(MainParty mainParty, EnemyParty enemyParty, DialogueHandler dialogueHandler) {
        this.dialogueHandler = dialogueHandler;
        this.mainParty = mainParty;
        this.enemyParty = enemyParty;
        this.isBossFight = enemyParty.containsBoss();
        // TODO: set up background
        this.background = null;
        actionQueue = new PriorityBlockingQueue<BattleAction>(mainParty.getSize() + enemyParty.getSize(),
                new Comparator<BattleAction>() {
                    public int compare(BattleAction a, BattleAction b) {
                        // compare n1 and n2
                        return a.getPriority() < b.getPriority() ? -1 : a.getPriority() == b.getPriority() ? 0 : 1;
                    }
                }
        );
    }

    @Override
    public void update(float delta) {
        if (canUpdate()) {
            if (mainParty.isDefeated()) {
                // run code for if the main party is defeated
            } else if (enemyParty.isDefeated()) {
                // run code for if the enemy party is defeated
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    setTurnActions();
                } else {
                    dialogueHandler.displayDialogue(action.run(delta));
                }
            }
        }
    }

    private void setTurnActions() {
        BattleCharacter heroes[] = mainParty.getBattleParty();
        BattleCharacter enemies[] = enemyParty.getBattleParty();
        for (BattleCharacter enemy : enemies) {
            // TODO: make the enemies more intelligent?
            addAction(BattleAction.attack(enemy,
                    heroes[battleRandomGen.nextInt(3)]));
        }
        for (BattleCharacter hero : heroes) {
            dialogueHandler.displayDialogue(getActionOptions(hero, enemies));
        }
    }

    private static DialogueOptions getActionOptions(BattleCharacter hero, BattleCharacter[] enemies) {
        Spell[] spells = new Spell[0]; // TODO: get these from wherever
        List<Item> itemList = Inventory.getItemInventory();
        Item[] items = itemList.toArray(new Item[itemList.size()]);
        return new DialogueOptions("What should " + hero + " do?", DialogueOptions.BATTLE_OPTIONS,
                InteractionUtils.makeBattleActions(hero, enemies, spells, items));
    }

    public void addAction(BattleAction action) {
        actionQueue.add(action);
    }

    public boolean canUpdate() {
        return dialogueHandler != null;
    }

    public MainParty getMainParty() {
        return mainParty;
    }

    public EnemyParty getEnemyParty() {
        return enemyParty;
    }

    public boolean isBossFight() {
        return isBossFight;
    }

    public TextureRegion getBackground() {
        return background;
    }
}
