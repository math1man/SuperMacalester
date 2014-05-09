package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.inventory.Armor;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.inventory.Weapon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Hero extends BattleCharacter implements Interaction {

    protected boolean hasFled = false;
    private int experience;
    private int nextExp;

    public Hero(String name, BattleClass battleClass) {
        this(name, battleClass, 1);
    }

    public Hero(String name, BattleClass battleClass, int level) {
        this(name, battleClass, level, -1, -1);
    }

    public Hero(String name, BattleClass battleClass, int level, int health, int mana) {
        super(name, battleClass, level, health, mana);
        this.experience = 0;
        this.nextExp = 16;
    }

    public void setHasFled(boolean a) {
        hasFled = a;
    }

    public boolean getHasFled() {
        return hasFled;
    }

    public void incExp(int i) {experience += i;}

    public int getExperience() {return experience;}

    public int getNextExp() {return nextExp;}

    @Override
    public void levelUp()
    {
        experience = 0;
        nextExp *= 2;
        level++;
        updateStats();
    }

    @Override
    public void run(GameScreen screen) {
        screen.getParty().addCharacter(this);
        new DialogueText(this + " has joined the party!", DialogueStyle.WORLD).run(screen);
    }

    public static class Parser extends SuperParser<Hero> {
        @Override
        public Hero fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = getString(object, "name");
            BattleClass battleClass = getObject(object, BattleClass.class);
            int level = getInt(object, "level");
            int health = getInt(object, "health", -1);
            int mana = getInt(object, "mana", -1);
            Hero hero = new Hero(name, battleClass, level, health, mana);
            if (has(object, Armor.class)) {
                hero.setEquippedArmor(getObject(object, Armor.class));
            }
            if (has(object, Weapon.class)) {
                hero.setEquippedWeapon(getObject(object, Weapon.class));
            }
            if (object.has("spells")) {
                hero.addSpells(getList(object, "spells", Spell.class));
            }
            return hero;
        }

        @Override
        public JsonElement toJson(Hero object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.name);
            addObject(json, object.battleClass, BattleClass.class);
            addInt(json, "level", object.level);
            addFloat(json, "health", object.currentHealth);
            addFloat(json, "mana", object.currentMana);
            if (object.hasEquippedArmor()) {
                addObject(json, object.getEquippedArmor(), Armor.class);
            }
            if (object.hasEquippedWeapon()) {
                addObject(json, object.getEquippedWeapon(), Weapon.class);
            }
            addList(json, "spells", object.getSpellBook().asList(), Spell.class);
            return json;
        }
    }
}
