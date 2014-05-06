package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.characters.*;
import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
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
import com.arnopaja.supermac.world.grid.RenderGrid;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class controls what is going on in a battle.
 *
 * @author Ari Weiland
 */
public class Battle implements Controller, Interaction {

    public static final List<String> BATTLE_OPTIONS = Arrays.asList("Attack", "Defend", "Spell", "Item", "Flee");

    protected final EnemyParty enemyParty;
    protected final boolean isBossFight;
    protected final String backgroundName;
    protected final TextureRegion background;
    protected final Queue<BattleAction> actionQueue;
    protected boolean isReady = false;
    protected MainParty mainParty;
    protected RenderGrid backgroundGrid;
    protected GameScreen screen;

    public Battle(EnemyParty enemyParty) {
        this(enemyParty, "");
    }

    public Battle(EnemyParty enemyParty, String backgroundName) {
        this.enemyParty = enemyParty;
        this.isBossFight = enemyParty.containsBoss();
        this.backgroundName = backgroundName;
        this.background = AssetLoader.getBackground(backgroundName);
        //We don't instantiate the mainparty's character list, so we get null pointer exception. WORD.
        actionQueue = new PriorityBlockingQueue<BattleAction>(4 + enemyParty.size(),
                new Comparator<BattleAction>() {
                    public int compare(BattleAction a, BattleAction b) {
                        // compare n1 and n2
                        return b.getPriority() - a.getPriority();
                    }
                }
        );
    }

    public void ready(MainParty mainParty, RenderGrid backgroundGrid, GameScreen screen) {
        if (!isReady) {
            isReady = true;
            this.mainParty = mainParty;
            this.backgroundGrid = backgroundGrid;
            this.screen = screen;
        }
    }

    @Override
    public void update(float delta) {
        if (isReady()) {
            if (mainParty.isDefeated()) {
                // TODO: code specific to defeat
                end();
            } else if (enemyParty.isDefeated()) {
                // TODO: text for victory
                //Calculate experience earned from battle
                int earnedExp = 0;
                for(int i=0;i<enemyParty.size();i++)
                    earnedExp += enemyParty.get(i).getLevel() * 2;
                //Apply this to each surviving character, checking for levelup
                for(Hero h:mainParty.getActiveParty())
                {
                    h.incExp(earnedExp);
                    System.out.println(h + " earned " + earnedExp + " exp!");
                    if(h.getExperience() >= h.getNextExp())
                    {
                        int d = h.getExperience() - h.getNextExp();
                        h.levelUp();
                        h.incExp(d);
                    }
                }
                end();
            } else if (mainParty.partyHasFled()) {
                // TODO: code specific to fleeing
                mainParty.clearHasFled();
                this.end();
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    setTurnActions();
                } else {
                    action.run(delta).run(screen);
                }
            }
        }
    }

    protected void setTurnActions() {
        enemyParty.clearDefend();
        mainParty.clearDefend();
        for (BattleCharacter enemy : enemyParty.getActiveParty()) {
            // TODO: make the enemies more intelligent?
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
        // TODO: general ending code
        screen.world();
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
        return new DialogueOptions("What should " + hero + " do?", BATTLE_OPTIONS,
                Arrays.asList(selectAttack(hero, interaction),
                        selectDefend(hero, interaction),
                        selectSpell(hero, interaction),
                        Interactions.NULL, // selectItem(hero, interaction), TODO: eventually put this back when items work
                        selectFlee(hero, interaction)),
                DialogueStyle.BATTLE_CONSOLE);
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
            for (Spell spell : spells) {
                if(spell.getManaCost() <= hero.getMana())
                {
                    spellInteractions.add(new DialogueOptions("Use " + spell + " on who?",
                        enemyParty.getActiveParty(),
                        spells(hero, spell, interaction),
                        DialogueStyle.BATTLE_CONSOLE));
                }
            }
            if(spellInteractions.isEmpty())
            {
                return new DialogueText(hero + " doesn't have enough mana to use any spells!", createOptions(hero, interaction),
                        DialogueStyle.BATTLE_CONSOLE);
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
            itemInteractions.add(new DialogueOptions("Use " + item + " on who?",
                    targets,
                    items(hero, item, interaction),
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

    private List<Interaction> spells(Hero hero, Spell spell, Interaction interaction) {
        List<Enemy> enemies = enemyParty.getActiveParty();
        List<Interaction> spells = new ArrayList<Interaction>(enemies.size());
        for (Enemy enemy : enemies) {
            spells.add(new MultiInteraction(BattleAction.spell(hero, spell, enemy), interaction));
        }
        return spells;
    }

    private List<Interaction> items(Hero hero, Item item, Interaction interaction) {
        List<Enemy> enemies = enemyParty.getActiveParty();
        List<Interaction> items = new ArrayList<Interaction>(enemies.size());
        for (Enemy enemy : enemies) {
            items.add(new MultiInteraction(BattleAction.item(hero, item, enemy), interaction));
        }
        return items;
    }

    public static class Parser extends SuperParser<Battle> {
        @Override
        public Battle fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            EnemyParty enemy = getObject(object, "enemy", EnemyParty.class);
            return new Battle(enemy);
        }

        @Override
        public JsonElement toJson(Battle object) {
            JsonObject json = new JsonObject();
            addObject(json, "enemy", object.enemyParty, EnemyParty.class);
            return json;
        }
    }
}
