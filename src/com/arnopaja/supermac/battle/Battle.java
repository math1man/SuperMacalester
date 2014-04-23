package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.battle.characters.EnemyParty;
import com.arnopaja.supermac.battle.characters.MainParty;
import com.arnopaja.supermac.helpers.*;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.arnopaja.supermac.inventory.Spell;
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
public class Battle implements Controller, InteractionBuilder {

    private static final Random battleRandomGen = new Random();

    private final EnemyParty enemyParty;
    private final boolean isBossFight;
    private final String backgroundName;
    private final TextureRegion background;
    private final Queue<BattleAction> actionQueue;

    private DialogueHandler dialogueHandler;
    private MainParty mainParty;

    public Battle(EnemyParty enemyParty, String backgroundName) {
        this.enemyParty = enemyParty;
        this.isBossFight = enemyParty.containsBoss();
        this.backgroundName = backgroundName;
        this.background = AssetLoader.getBackground(backgroundName);
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

    @Override
    public Interaction toInteraction() {
        final Battle battle = this;
        return new Interaction(battle) {
            @Override
            public void run(GameScreen screen) {
                screen.goToBattle(battle);
            }
        };
    }

    // getActionOptions and all supporting methods... guh, so long
    private static DialogueOptions getActionOptions(BattleCharacter hero, BattleCharacter[] enemies) {
        List<Spell> spellList = hero.getSpellsList();
        Spell[] spells = spellList.toArray(new Spell[spellList.size()]);
        List<Item> itemList = Inventory.getMain().getAll(Item.class);
        Item[] items = itemList.toArray(new Item[itemList.size()]);
        return new DialogueOptions("What should " + hero + " do?", DialogueOptions.BATTLE_OPTIONS,
                makeBattleActions(hero, enemies, spells, items));
    }
    private static Interaction[] makeBattleActions(BattleCharacter hero, BattleCharacter[] enemies,
                                                   Spell[] spells, Item[] items) {
        Interaction[] interactions = new Interaction[5];
        interactions[0] = selectAttack(hero, enemies);
        interactions[1] = selectDefend(hero);
        interactions[2] = selectSpell(hero, spells, enemies);
        interactions[3] = selectItem(hero, items, enemies);
        interactions[4] = selectFlee(hero);
        return interactions;
    }

    private static Interaction selectAttack(BattleCharacter hero, BattleCharacter[] enemies) {
        return new DialogueOptions("Who do you want to attack?",
                enemies, Interaction.convert(attacks(hero, enemies))).toInteraction();
    }
    private static Interaction selectDefend(BattleCharacter hero) {
        return BattleAction.defend(hero).toInteraction();
    }
    private static Interaction selectSpell(BattleCharacter hero, Spell[] spells, BattleCharacter[] enemies) {
        Interaction[] spellInteractions = new Interaction[spells.length];
        for (int i=0; i<spells.length; i++) {
            spellInteractions[i] = new DialogueOptions("Who do you want to use " + spells[i] + " on?",
                    enemies, Interaction.convert(spells(hero, spells[i], enemies))).toInteraction();
        }
        return new DialogueOptions("What spell do you want to use?", spells, spellInteractions).toInteraction();
    }
    // TODO: can items be used on friends? On self?
    private static Interaction selectItem(BattleCharacter hero, Item[] items, BattleCharacter[] enemies) {
        Interaction[] itemInteractions = new Interaction[items.length];
        for (int i=0; i<items.length; i++) {
            itemInteractions[i] = new DialogueOptions("Who do you want to use " + items[i] + " on?",
                    enemies, Interaction.convert(items(hero, items[i], enemies))).toInteraction();
        }
        return new DialogueOptions("What item do you want to use?", items, itemInteractions).toInteraction();
    }
    private static Interaction selectFlee(BattleCharacter hero) {
        return BattleAction.flee(hero).toInteraction();
    }

    private static BattleAction[] attacks(BattleCharacter source, BattleCharacter[] destinations) {
        int count = destinations.length;
        BattleAction[] attacks = new BattleAction[count];
        for (int i=0; i<count; i++) {
            attacks[i] = BattleAction.attack(source, destinations[i]);
        }
        return attacks;
    }
    private static BattleAction[] spells(BattleCharacter source, Spell spell, BattleCharacter[] destinations) {
        int count = destinations.length;
        BattleAction[] spells = new BattleAction[count];
        for (int i=0; i<count; i++) {
            spells[i] = BattleAction.spell(source, spell, destinations[i]);
        }
        return spells;
    }
    private static BattleAction[] items(BattleCharacter source, Item item, BattleCharacter[] destinations) {
        int count = destinations.length;
        BattleAction[] items = new BattleAction[count];
        for (int i=0; i<count; i++) {
            items[i] = BattleAction.item(source, item, destinations[i]);
        }
        return items;
    }

    public static class Parser extends SuperParser<Battle> {
        @Override
        public Battle fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            EnemyParty enemy = getObject(object, "enemy", EnemyParty.class);
            String background = getString(object, "background");
            return new Battle(enemy, background);
        }

        @Override
        public JsonElement toJson(Battle object) {
            JsonObject json = new JsonObject();
            addObject(json, "enemy", object.enemyParty, EnemyParty.class);
            addString(json, "background", object.backgroundName);
            return json;
        }
    }
}
