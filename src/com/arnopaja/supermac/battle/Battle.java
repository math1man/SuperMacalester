package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.characters.*;
import com.arnopaja.supermac.helpers.*;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.arnopaja.supermac.inventory.Spell;
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
    protected GameScreen screen;
    protected MainParty mainParty;

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
                        return a.getPriority() - b.getPriority();
                    }
                }
        );
    }

    public void ready(MainParty mainParty, GameScreen screen) {
        if (!isReady) {
            isReady = true;
            this.mainParty = mainParty;
            this.screen = screen;
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
        return new DialogueOptions("Who do you want to attack?", enemyParty.getActiveParty(),
                attacks(hero, interaction), DialogueStyle.BATTLE_CONSOLE).toInteraction();
    }

    private Interaction selectDefend(Hero hero, Interaction interaction) {
        return Interaction.combine(BattleAction.defend(hero), interaction);
    }

    private Interaction selectSpell(Hero hero, Interaction interaction) {
        List<Spell> spells = hero.getSpellsList();
        List<Interaction> spellInteractions = new ArrayList<Interaction>(spells.size());
        for (Spell spell : spells) {
            spellInteractions.add(new DialogueOptions("Who do you want to use " + spell + " on?",
                    enemyParty.getActiveParty(),
                    spells(hero, spell, interaction),
                    DialogueStyle.BATTLE_CONSOLE).toInteraction());
        }
        return new DialogueOptions("What spell do you want to use?", spells,
                spellInteractions, DialogueStyle.BATTLE_CONSOLE).toInteraction();
    }
    // TODO: can items be used on friends? On self?
    private Interaction selectItem(Hero hero, Interaction interaction) {
        List<Item> items = Inventory.getMain().getAll(Item.class);
        List<Interaction> itemInteractions = new ArrayList<Interaction>(items.size());
        for (Item item : items) {
            itemInteractions.add(new DialogueOptions("Who do you want to use " + item + " on?",
                    enemyParty.getActiveParty(),
                    items(hero, item, interaction),
                    DialogueStyle.BATTLE_CONSOLE).toInteraction());
        }
        return new DialogueOptions("What item do you want to use?", items,
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
