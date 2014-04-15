package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.battle.characters.EnemyParty;
import com.arnopaja.supermac.battle.characters.MainParty;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.InteractionUtils;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    private final EnemyParty enemyParty;
    private final boolean isBossFight;
    private final TextureRegion background;
    private final Queue<BattleAction> actionQueue;

    private DialogueHandler dialogueHandler;
    private MainParty mainParty;

    public Battle(EnemyParty enemyParty, TextureRegion background) {
        this.enemyParty = enemyParty;
        this.isBossFight = enemyParty.containsBoss();
        this.background = background;
        actionQueue = new PriorityBlockingQueue<BattleAction>(mainParty.getSize() + enemyParty.getSize(),
                new Comparator<BattleAction>() {
                    public int compare(BattleAction a, BattleAction b) {
                        // compare n1 and n2
                        return a.getPriority() - b.getPriority();
                    }
                }
        );
    }

    public void readyBattle(MainParty mainParty, DialogueHandler dialogueHandler) {
        this.mainParty = mainParty;
        this.dialogueHandler = dialogueHandler;
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

    public static class Parser extends SuperParser<Battle> {
        @Override
        public Battle convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            EnemyParty enemy = parseEnemy(object.get("enemy"));
            TextureRegion background = AssetLoader.getBackground(object.getAsJsonPrimitive("background").getAsString());
            return new Battle(enemy, background);
        }

        public EnemyParty parseEnemy(JsonElement element) {
            // TODO: finish me!
            return new EnemyParty();
        }
    }
}
