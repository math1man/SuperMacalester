package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.battle.characters.EnemyParty;
import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.battle.characters.MainParty;
import com.arnopaja.supermac.helpers.*;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class controls what is going on in a battle.
 *
 * @author Ari Weiland
 */
public class Battle implements Controller, InteractionBuilder {

    private final EnemyParty enemyParty;
    private final boolean isBossFight;
    private final String backgroundName;
    private final TextureRegion background;
    private final Queue<BattleAction> actionQueue;

    private boolean isReady = false;
    private DialogueHandler dialogueHandler;
    private MainParty mainParty;

    public Battle(EnemyParty enemyParty, String backgroundName) {
        this.enemyParty = enemyParty;
        this.isBossFight = enemyParty.containsBoss();
        this.backgroundName = backgroundName;
        this.background = AssetLoader.getBackground(backgroundName);
        actionQueue = new PriorityBlockingQueue<BattleAction>(mainParty.size() + enemyParty.size(),
                new Comparator<BattleAction>() {
                    public int compare(BattleAction a, BattleAction b) {
                        // compare n1 and n2
                        return a.getPriority() - b.getPriority();
                    }
                }
        );
    }

    public void ready(MainParty mainParty, DialogueHandler dialogueHandler) {
        if (!isReady) {
            isReady = true;
            this.mainParty = mainParty;
            this.dialogueHandler = dialogueHandler;
        }
    }

    @Override
    public void update(float delta) {
        if (isReady()) {
            if (mainParty.isDefeated()) {
                // run code for if the main party is defeated
            } else if (enemyParty.isDefeated()) {
                // run code for if the enemy party is defeated
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    setTurnActions();
                } else {
                    dialogueHandler.display(action.run(delta));
                }
            }
        }
    }

    private void setTurnActions() {
        for (BattleCharacter enemy : enemyParty) {
            // TODO: make the enemies more intelligent?
            addAction(BattleAction.attack(enemy, mainParty.getRandom()));
        }
        dialogueHandler.display(makeActions());
    }

    public void addAction(BattleAction action) {
        actionQueue.add(action);
    }

    public boolean isReady() {
        return isReady;
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

    private DialogueOptions makeActions() {
        Iterator<Hero> heroes = mainParty.iterator();
        return getOptions(heroes.next(), heroes, Interaction.NULL);
    }

    private DialogueOptions getOptions(Hero hero, Iterator<Hero> heroes, Interaction interaction) {
        DialogueOptions options = new DialogueOptions("What should " + hero + " do?",
                DialogueOptions.BATTLE_OPTIONS,
                selectAttack(hero, interaction),
                selectDefend(hero, interaction),
                selectSpell(hero, interaction),
                selectItem(hero, interaction), selectFlee(hero, interaction));
        if (heroes.hasNext()) {
            return getOptions(heroes.next(), heroes, options.toInteraction());
        } else {
            return options;
        }
    }

    private Interaction selectAttack(Hero hero, Interaction interaction) {
        return new DialogueOptions("Who do you want to attack?",
                enemyParty.toArray(), attacks(hero, interaction)).toInteraction();
    }

    private Interaction selectDefend(Hero hero, Interaction interaction) {
        return Interaction.combine(BattleAction.defend(hero), interaction);
    }

    private Interaction selectSpell(Hero hero, Interaction interaction) {
        List<Spell> spells = hero.getSpellsList();
        Interaction[] spellInteractions = new Interaction[spells.size()];
        for (int i=0; i<spells.size(); i++) {
            spellInteractions[i] = new DialogueOptions("Who do you want to use " + spells.get(i) + " on?",
                    enemyParty.toArray(), spells(hero, spells.get(i), interaction)).toInteraction();
        }
        return new DialogueOptions("What spell do you want to use?", spells, spellInteractions).toInteraction();
    }
    // TODO: can items be used on friends? On self?
    private Interaction selectItem(Hero hero, Interaction interaction) {
        List<Item> items = Inventory.getMain().getAll(Item.class);
        Interaction[] itemInteractions = new Interaction[items.size()];
        for (int i=0; i<items.size(); i++) {
            itemInteractions[i] = new DialogueOptions("Who do you want to use " + items.get(i) + " on?",
                    enemyParty.toArray(), items(hero, items.get(i), interaction)).toInteraction();
        }
        return new DialogueOptions("What item do you want to use?", items, itemInteractions).toInteraction();
    }

    private Interaction selectFlee(Hero hero, Interaction interaction) {
        return Interaction.combine(BattleAction.flee(hero), interaction);
    }

    private Interaction[] attacks(Hero hero, Interaction interaction) {
        int count = enemyParty.size();
        Interaction[] attacks = new Interaction[count];
        for (int i=0; i<count; i++) {
            attacks[i] = Interaction.combine(BattleAction.attack(hero, enemyParty.get(i)), interaction);
        }
        return attacks;
    }

    private Interaction[] spells(Hero hero, Spell spell, Interaction interaction) {
        int count = enemyParty.size();
        Interaction[] spells = new Interaction[count];
        for (int i=0; i<count; i++) {
            spells[i] = Interaction.combine(BattleAction.spell(hero, spell, enemyParty.get(i)), interaction);
        }
        return spells;
    }

    private Interaction[] items(Hero hero, Item item, Interaction interaction) {
        int count = enemyParty.size();
        Interaction[] items = new Interaction[count];
        for (int i=0; i<count; i++) {
            items[i] = Interaction.combine(BattleAction.item(hero, item, enemyParty.get(i)), interaction);
        }
        return Interaction.convert(items);
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
