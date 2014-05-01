package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.*;
import com.arnopaja.supermac.inventory.Item;
import com.arnopaja.supermac.inventory.Spell;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
/**
 * @author Ari Weiland
 */
public class BattleTest extends Battle {

    //TRUE = Battle will set hero turns automatically (debug only)
    //FALSE = Prompt for user input
    public static final boolean autoBattle = true;

    public BattleTest(EnemyParty enemyParty, String backgroundName) {
        super(enemyParty, backgroundName);
    }

    @Override
    public void update(float delta) {
        if (isReady()) {
            if (mainParty.isDefeated()) {
                // run code for if the main party is defeated
                System.out.println("You have lost");
                setOver();
            } else if (enemyParty.isDefeated()) {
                // run code for if the enemy party is defeated'
                System.out.println("You have won!");
                setOver();
            } else if (mainParty.partyHasFled()) {
                mainParty.clearHasFled();
                this.setOver();
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    System.out.println("\nSetting turn actions");
                    setTurnActions();
                } else {
                    /*Final Fantasy Style Death Exception:
                    Any action which is set to be performed against a dead character and has no effect
                    will result in a skipped turn for the source, ignoring that battleaction

                    If any of the following conditions are met, the turn is skipped for this action:

                    source of the action is fainted
                    destination is fainted, action taken is NOT an item OR a healing spell
                    (non-resurrection items/spells are to be consumed as normal, but without effect)
                     */
                    //TODO: Items, item types.
                    if(action.getSource().isFainted()) return;
                    if(action.getDestination() != null && action.getDestination().isFainted() && action.getType() != BattleAction.ActionType.ITEM && action.getType() != BattleAction.ActionType.SPELL) return;
                    if(action.getType() == BattleAction.ActionType.SPELL && action.getSpell().isBlack()) return;
                    if(action.getType() == BattleAction.ActionType.ITEM && action.getItem().getType() != Item.ItemType.HEAL) return;
                    System.out.println(action.run(delta));
                }
            }
        }
    }

    @Override
    protected void setTurnActions() {
        enemyParty.clearDefend();
        mainParty.clearDefend();
        // implement me!
        Random r = new Random();
        //Set enemy actions
        BattleAction a;
        for (Enemy e:enemyParty.getActiveParty()) {
//            System.out.println(e);
            a = BattleAction.attack(e,mainParty.getRandom());
            addAction(a);
            System.out.println(a);
        }
        //Set friendly actions
        for (Hero h:mainParty.getActiveParty()) {
            //System.out.println(h);
            if(autoBattle)
            {
                if(h.getMana() > h.getSpell(0).getManaCost()){
                    a = BattleAction.spell(h, h.getSpell(0), enemyParty.getRandom());
                } else {
                    a = BattleAction.attack(h,enemyParty.getRandom());
                }
                addAction(a);
                System.out.println(a);
            }
            else
            {
                Scanner input = new Scanner(System.in);
                Character userInput;
                int index;
                BattleAction.ActionType at;
                a = null;
                while(a == null)
                {
                    System.out.println("What will " + h.getName() + " do?");
                    System.out.println("a = ATTACK, d = DEFEND, s = SPELL, f = FLEE");
                    userInput = input.next(".").charAt(0);
                    switch(userInput)
                    {
                        case 'a':
                            while(a == null)
                            {
                                System.out.println("Attack which character?");
                                for(Enemy e:enemyParty.getActiveParty())
                                {
                                    System.out.println(enemyParty.getIndex(e) + ": " + e.getName());
                                }
                                userInput = input.next(".").charAt(0);
                                index = Integer.parseInt(userInput.toString());
                                if(enemyParty.get(index) != null && !enemyParty.get(index).isFainted())
                                {
                                    a = BattleAction.attack(h,enemyParty.get(index));
                                }
                            }
                            break;
                        case 'd':
                            a = BattleAction.defend(h);
                            break;
                        case 's':
                            Spell spell;
                            while(a == null)
                            {
                                System.out.println("Use which spell?");
                                for(Spell s:h.getSpellsList())
                                {
                                    System.out.println(h.getSpellIndex(s) + ": " + s.getName());
                                }
                                userInput = input.next(".").charAt(0);
                                index = Integer.parseInt(userInput.toString());
                                if(h.getSpell(index) != null)
                                {
                                    spell = h.getSpell(index);
                                }
                                else continue;
                                while(a == null)
                                {
                                    Party p;
                                    if(spell.isBlack()) p = enemyParty;
                                    else p = mainParty;
                                    System.out.println("On which character?");
                                    for(Object b: p.getActiveParty())
                                    {
                                        BattleCharacter bc = (BattleCharacter) b;
                                        System.out.println(p.getIndex(bc) + ": " + bc.getName());
                                    }
                                    userInput = input.next(".").charAt(0);
                                    index = Integer.parseInt(userInput.toString());
                                    if(p.get(index) != null && (!p.get(index).isFainted() || spell.getDamageModifier() == Float.POSITIVE_INFINITY) ) //Don't allow for invalid characters. Allow ONLY resurrect spells on fainted characters
                                    {
                                        a = BattleAction.spell(h,spell,p.get(index));
                                    }
                                }
                            }
                            break;
                        case 'f':
                            a = BattleAction.flee(h);
                            break;
                        //TODO: item handler (case 'i':)
                        default:
                            continue;
                    }
                }
                addAction(a);
            }
        }
    }

    public static void main(String[] args) {
        //create enemy party
        //create main party
        System.out.println("IT'S HAPPENING");
        Enemy dumb = new Enemy("Enemy Comp Sci", BattleClass.COMP_SCI, 5, null, false);
        Enemy dumb2 = new Enemy("Enemy Econ", BattleClass.ECON, 5, null, false);
        Enemy dumb3 = new Enemy("Enemy Humanities", BattleClass.HUMANITIES, 5, null, false);
        Enemy dumb4 = new Enemy("Enemy Nat Sci", BattleClass.NAT_SCI, 5, null, false);
        ArrayList<Enemy> enemlist = new ArrayList<Enemy>();
        enemlist.add(dumb);
        enemlist.add(dumb2);
        enemlist.add(dumb3);
        enemlist.add(dumb4);
        EnemyParty teamyolo = new EnemyParty(enemlist);
        Spell yoloswag = new Spell(1, "Damage Spell", 2, 3, true);
        Spell smokeWeedEveryDay = new Spell(2, "Healing Spell", 5, 3, false);
        Hero smart = new Hero("Hero Econ", BattleClass.ECON, 5);
        Hero smart2 = new Hero("Hero Nat Sci", BattleClass.NAT_SCI, 5);
        Hero smart3 = new Hero("Hero Humanities", BattleClass.HUMANITIES, 5);
        Hero smart4 = new Hero("Hero Comp Sci", BattleClass.COMP_SCI, 5);
        smart.addSpells(yoloswag);
        smart2.addSpells(smokeWeedEveryDay);
        smart3.addSpells(yoloswag);
        smart4.addSpells(yoloswag);
        MainParty teamswag = new MainParty();
        teamswag.addCharacter(smart);
        teamswag.addCharacter(smart2);
        teamswag.addCharacter(smart3);
        teamswag.addCharacter(smart4);

        BattleTest battle = new BattleTest(teamyolo, "teamyolo");
        battle.ready(teamswag, null, null);

        while (!battle.isOver()) {
            float delta = 1f / 60;
            battle.update(delta);
            try {
                // sleep for 1/60 of a second
                Thread.sleep((int)(delta*1000));
            } catch(InterruptedException ignored) {
            }
        }
        System.out.println("IT'S OVER");
    }
}
