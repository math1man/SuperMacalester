package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.battle.characters.Enemy;
import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.battle.characters.Party;
import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.dialogue.DialogueMember;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.dialogue.DialogueStep;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.interaction.MultiInteraction;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.inventory.SpellBook;
import com.arnopaja.supermac.plot.Settings;
import com.arnopaja.supermac.world.grid.RenderGrid;
import com.badlogic.gdx.audio.Music;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class controls what is going on in a battle.
 *
 * @author Ari Weiland
 */
public class Battle implements Controller, Interaction {

    protected final Party<Enemy> enemyParty;
    protected final boolean isBossFight;
    protected final boolean isFleeable;
    protected final Queue<BattleAction> actionQueue;
    protected boolean isReady = false;
    protected Party<Hero> mainParty;
    protected RenderGrid backgroundGrid;
    protected GameScreen screen;

    public Battle(Party<Enemy> enemyParty, boolean isBossFight) {
        this(enemyParty, isBossFight, isBossFight);
    }

    public Battle(Party<Enemy> enemyParty, boolean isBossFight, boolean isFleeable) {
        this.enemyParty = enemyParty;
        this.isBossFight = isBossFight;
        this.isFleeable = isFleeable;
        actionQueue = new PriorityBlockingQueue<BattleAction>(4 + enemyParty.size());
    }

    public void ready(Party<Hero> mainParty, RenderGrid backgroundGrid, GameScreen screen) {
        if (isReady) {
            throw new IllegalStateException("Battle has already been readied!");
        }
        isReady = true;
        this.mainParty = mainParty;
        this.backgroundGrid = backgroundGrid;
        this.screen = screen;
    }

    @Override
    public void update(float delta) {
        if (isReady()) {
            if (mainParty.isDefeated()) {
                mainPartyDefeated();
            } else if (enemyParty.isDefeated()) {
                enemyPartyDefeated();
            } else if (mainParty.hasFled()) {
                mainPartyFled();
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    resetTurnActions();
                } else {
                    action.run(delta).run(screen);
                }
            }
        }
    }

    protected void mainPartyDefeated() {
        end();
        new DialogueStep("You have been defeated!", DialogueStyle.WORLD);
    }

    protected void enemyPartyDefeated() {
        String dialogue = "You are victorious!";
        int earnedExp = 0;
        for (Enemy enemy : enemyParty.getBattleParty()) {
            earnedExp += enemy.getLevel() * 2;
        }
        for (Hero h : mainParty.getActiveParty()) {
            h.incExp(earnedExp);
            dialogue += "<d>" + h + " earned " + earnedExp + " exp!";
            if (h.getExperience() >= h.getNextExp()) {
                int d = h.getExperience() - h.getNextExp();
                h.levelUp();
                h.incExp(d);
                dialogue += h + " gained a level!";
            }
        }
        end();
        new DialogueStep(dialogue, DialogueStyle.WORLD);
    }

    protected void mainPartyFled() {
        end();
        String dialogue = "You have fled like a ";
        if (Settings.isClean()) dialogue += "wuss!";
        else dialogue += "bitch!";
        new DialogueStep(dialogue, DialogueStyle.WORLD);
    }

    protected void resetTurnActions() {
        enemyParty.clearDefend();
        mainParty.clearDefend();
        for (BattleCharacter enemy : enemyParty.getActiveParty()) {
            // TODO: make the enemies more intelligent
            addAction(BattleAction.attack(enemy, mainParty.getRandom()));
        }
        makeActions().run(screen);
    }

    public void addAction(BattleAction action) {
        actionQueue.add(action);
    }

    public boolean isReady() {
        return isReady;
    }

    public void end() {
        mainParty.clearHasFled();
        mainParty.clearDefend();
        mainParty.clearPowerup();
        screen.world();
    }

    @Override
    public Music getMusic() {
        // TODO: handle music via the parser maybe?
//        if (isBossFight()) {
//            return AssetLoader.bossMusic;
//        } else {
            return AssetLoader.battleMusic;
//        }
    }

    public Party<Hero> getMainParty() {
        return mainParty;
    }

    public Party<Enemy> getEnemyParty() {
        return enemyParty;
    }

    public boolean isBossFight() {
        return isBossFight;
    }

    public RenderGrid getBackgroundGrid() {
        return backgroundGrid;
    }

    @Override
    public void run(GameScreen screen) {
        screen.battle(this);
    }

    private DialogueOptions makeActions() {
        Iterator<Hero> heroes = mainParty.getActiveParty().iterator();
        return getOptions(heroes.next(), heroes, Interactions.NULL);
    }

    private DialogueOptions getOptions(Hero hero, Iterator<Hero> heroes, Interaction interaction) {
        DialogueOptions options = createOptions(hero, interaction);
        if (heroes.hasNext()) {
            return getOptions(heroes.next(), heroes, options);
        } else {
            return options;
        }
    }

    private DialogueOptions createOptions(Hero hero, Interaction interaction) {
        List<DialogueMember> members = new ArrayList<DialogueMember>();
        members.add(new DialogueMember("Attack", selectAttack(hero, interaction)));
        members.add(new DialogueMember("Defend", selectDefend(hero, interaction)));
        if (!hero.isOutOfMana() && hero.hasSpells()) {
            members.add(new DialogueMember("Spell", selectSpell(hero, interaction)));
        }
        if (!Inventory.getMain().getAll(Item.class).isEmpty()) {
            members.add(new DialogueMember("Item", selectItem(hero, interaction)));
        }
        if(isFleeable) members.add(new DialogueMember("Flee", selectFlee(hero, interaction)));
        return new DialogueOptions("Action Menu", "What should " + hero + " do?",
                members, DialogueStyle.BATTLE_CONSOLE);
    }

    private Interaction selectAttack(Hero hero, Interaction interaction) {
        return new DialogueOptions("Attack who?", enemyParty.getActiveParty(),
                attacks(hero, interaction), DialogueStyle.BATTLE_CONSOLE);
    }

    private Interaction selectDefend(Hero hero, Interaction interaction) {
        return new MultiInteraction(BattleAction.defend(hero), interaction);
    }

    private Interaction selectSpell(Hero hero, Interaction interaction) {
        SpellBook spells = hero.getSpellBook();
        if (spells.isEmpty()) {
            return Interactions.NULL;
        } else {
            List<Interaction> spellInteractions = new ArrayList<Interaction>(spells.size());
            List<BattleCharacter> targets = new ArrayList<BattleCharacter>(mainParty.getActiveParty());
            targets.addAll(enemyParty.getActiveParty());
            for (Spell spell : spells) {
                if(hero.hasMana(spell.getManaCost())) {
                    spellInteractions.add(new DialogueOptions("Cast on who?",
                        targets,
                        spells(hero, spell, targets, interaction),
                        DialogueStyle.BATTLE_CONSOLE));
                }
            }
            if (spellInteractions.isEmpty()) {
                return Interactions.NULL;
            }
            return new DialogueOptions("Which spell?", spells.asList(),
                    spellInteractions, DialogueStyle.BATTLE_CONSOLE);
        }
    }

    private Interaction selectItem(Hero hero, Interaction interaction) {
        List<Item> items = Inventory.getMain().getAll(Item.class);
        List<Interaction> itemInteractions = new ArrayList<Interaction>(items.size());
        List<BattleCharacter> targets = new ArrayList<BattleCharacter>(mainParty.getActiveParty());
        targets.addAll(enemyParty.getActiveParty());
        for (Item item : items) {
            itemInteractions.add(new DialogueOptions("Use on who?",
                    targets,
                    items(hero, item, targets, interaction),
                    DialogueStyle.BATTLE_CONSOLE));
        }
        return new DialogueOptions("Which item?", items,
                itemInteractions, DialogueStyle.BATTLE_CONSOLE);
    }

    private Interaction selectFlee(Hero hero, Interaction interaction) {
        return new MultiInteraction(BattleAction.flee(hero), interaction);
    }

    private List<Interaction> attacks(Hero hero, Interaction interaction) {
        List<Enemy> enemies = enemyParty.getActiveParty();
        List<Interaction> attacks = new ArrayList<Interaction>(enemies.size());
        for (Enemy enemy : enemies) {
            attacks.add(new MultiInteraction(BattleAction.attack(hero, enemy), interaction));
        }
        return attacks;
    }

    private List<Interaction> spells(Hero hero, Spell spell, List<BattleCharacter> targets, Interaction interaction) {
        List<Interaction> spells = new ArrayList<Interaction>(targets.size());
        for (BattleCharacter target : targets) {
            spells.add(new MultiInteraction(BattleAction.spell(hero, spell, target), interaction));
        }
        return spells;
    }

    private List<Interaction> items(Hero hero, Item item, List<BattleCharacter> targets, Interaction interaction) {
        List<Interaction> items = new ArrayList<Interaction>(targets.size());
        for (BattleCharacter target : targets) {
            items.add(new MultiInteraction(BattleAction.item(hero, item, target), interaction));
        }
        return items;
    }

    public static class Parser extends SuperParser<Battle> {
        @Override
        public Battle fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Party enemy = getObject(object, "enemy", Party.class);
            boolean isBossFight = getBoolean(object, "boss", false);
            if (object.has("fleeable")) {
                boolean isFleeable = getBoolean(object, "fleeable");
                return new Battle(enemy, isBossFight, isFleeable);
            } else {
                return new Battle(enemy, isBossFight);
            }
        }

        @Override
        public JsonElement toJson(Battle object) {
            JsonObject json = new JsonObject();
            addObject(json, "enemy", object.enemyParty, Party.class);
            addBoolean(json, "boss", object.isBossFight);
            addBoolean(json, "fleeable", object.isFleeable);
            return json;
        }
    }
}
