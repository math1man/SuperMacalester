package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.characters.*;
import com.arnopaja.supermac.helpers.*;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.arnopaja.supermac.inventory.Spell;
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
public class Battle implements Controller, InteractionBuilder {

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
                // TODO: code specific to victory
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
                    action.run(delta).toInteraction().run(screen);
                }
            }
        }
    }

    protected void setTurnActions() {
        for (BattleCharacter enemy : enemyParty.getActiveParty()) {
            // TODO: make the enemies more intelligent?
            addAction(BattleAction.attack(enemy, mainParty.getRandom()));
        }
        makeActions().toInteraction().run(screen);
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
    public Interaction toInteraction() {
        final Battle battle = this;
        return new Interaction(battle) {
            @Override
            public void run(GameScreen screen) {
                screen.battle(battle);
            }
        };
    }

    private DialogueOptions makeActions() {
        Iterator<Hero> heroes = mainParty.getActiveParty().iterator();
        return getOptions(heroes.next(), heroes, Interaction.NULL);
    }

    private DialogueOptions getOptions(Hero hero, Iterator<Hero> heroes, Interaction interaction) {
        DialogueOptions options = new DialogueOptions("What should " + hero + " do?", BATTLE_OPTIONS,
                Arrays.asList(selectAttack(hero, interaction),
                        selectDefend(hero, interaction),
                        selectSpell(hero, interaction),
                        selectItem(hero, interaction),
                        selectFlee(hero, interaction)),
                DialogueStyle.BATTLE_CONSOLE);
        if (heroes.hasNext()) {
            return getOptions(heroes.next(), heroes, options.toInteraction());
        } else {
            return options;
        }
    }

    private Interaction selectAttack(Hero hero, Interaction interaction) {
        return new DialogueOptions("Attack who?", enemyParty.getActiveParty(),
                attacks(hero, interaction), DialogueStyle.BATTLE_CONSOLE).toInteraction();
    }

    private Interaction selectDefend(Hero hero, Interaction interaction) {
        return Interaction.combine(BattleAction.defend(hero), interaction);
    }

    private Interaction selectSpell(Hero hero, Interaction interaction) {
        List<Spell> spells = hero.getSpells();
        List<Interaction> spellInteractions = new ArrayList<Interaction>(spells.size());
        for (Spell spell : spells) {
            spellInteractions.add(new DialogueOptions("Use " + spell + " on who?",
                    enemyParty.getActiveParty(),
                    spells(hero, spell, interaction),
                    DialogueStyle.BATTLE_CONSOLE).toInteraction());
        }
        return new DialogueOptions("Which spell?", spells,
                spellInteractions, DialogueStyle.BATTLE_CONSOLE).toInteraction();
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
                    DialogueStyle.BATTLE_CONSOLE).toInteraction());
        }
        return new DialogueOptions("Which item?", items,
                itemInteractions, DialogueStyle.BATTLE_CONSOLE).toInteraction();
    }

    private Interaction selectFlee(Hero hero, Interaction interaction) {
        return Interaction.combine(BattleAction.flee(hero), interaction);
    }

    private List<Interaction> attacks(Hero hero, Interaction interaction) {
        List<Enemy> enemies = enemyParty.getActiveParty();
        List<Interaction> attacks = new ArrayList<Interaction>(enemies.size());
        for (Enemy enemy : enemies) {
            attacks.add(Interaction.combine(BattleAction.attack(hero, enemy), interaction));
        }
        return attacks;
    }

    private List<Interaction> spells(Hero hero, Spell spell, Interaction interaction) {
        List<Enemy> enemies = enemyParty.getActiveParty();
        List<Interaction> spells = new ArrayList<Interaction>(enemies.size());
        for (Enemy enemy : enemies) {
            spells.add(Interaction.combine(BattleAction.spell(hero, spell, enemy), interaction));
        }
        return spells;
    }

    private List<Interaction> items(Hero hero, Item item, Interaction interaction) {
        List<Enemy> enemies = enemyParty.getActiveParty();
        List<Interaction> items = new ArrayList<Interaction>(enemies.size());
        for (Enemy enemy : enemies) {
            items.add(Interaction.combine(BattleAction.item(hero, item, enemy), interaction));
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
