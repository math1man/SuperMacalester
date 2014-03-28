package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.battle.characters.EnemyParty;
import com.arnopaja.supermac.battle.characters.MainParty;
import com.arnopaja.supermac.helpers.*;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.inventory.Item;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Comparator;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class controls what is going on in a battle.
 *
 * @author Ari Weiland
 */
public class BattleController implements BaseController {

    private static Random battleRandomGen = new Random();

    private final DialogueHandler dialogueHandler;

    private final MainParty mainParty;
    private final EnemyParty enemyParty;
    private final boolean isBossFight;
    private final TextureRegion background;
    private final Queue<BattleAction> actionQueue;


    public BattleController(MainParty mainParty, EnemyParty enemyParty) {
        this(null, mainParty, enemyParty);
    }

    public BattleController(DialogueHandler dialogueHandler, BattleController battle) {
        this(dialogueHandler, battle.getMainParty(), battle.getEnemyParty());
    }

    public BattleController(DialogueHandler dialogueHandler, MainParty mainParty, EnemyParty enemyParty) {
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
        if (mainParty.isDefeated()) {
            // run code for if the main party is defeated
        } else if (enemyParty.isDefeated()) {
            // run code for if the enemy party is defeated
        } else {
            BattleAction action = actionQueue.poll();
            if (action == null) {
                setTurnActions();
            } else {
                dialogueHandler.displayDialogue(action.runAction());
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

    private DialogueOptions getActionOptions(BattleCharacter hero, BattleCharacter[] enemies) {
        Spell[] spells = new Spell[0]; // TODO: get these from wherever
        Item[] items = new Item[0];    // TODO: get these from wherever
        return new DialogueOptions("What should " + hero + " do?",
                DialogueOptions.BATTLE_OPTIONS,
                InteractionUtils.selectAttack(hero, enemies),
                InteractionUtils.selectDefend(hero),
                InteractionUtils.selectSpell(hero, spells, enemies),
                InteractionUtils.selectItem(hero, items, enemies),
                InteractionUtils.selectFlee(hero));
    }

    public void addAction(BattleAction action) {
        actionQueue.add(action);
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
