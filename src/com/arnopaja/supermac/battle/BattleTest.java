package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.*;
import com.arnopaja.supermac.inventory.Spell;

import java.util.ArrayList;
import java.util.Random;
/**
 * @author Ari Weiland
 */
public class BattleTest extends Battle {

    public BattleTest(EnemyParty enemyParty, String backgroundName) {
        super(enemyParty, backgroundName);
    }

    @Override
    public void update(float delta) {
        if (isReady()) {
            if (mainParty.isDefeated()) {
                // subrun code for if the main party is defeated
                System.out.println("You have lost");
                setOver();
            } else if (enemyParty.isDefeated()) {
                // subrun code for if the enemy party is defeated'
                System.out.println("You have won!");
                setOver();
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    System.out.println("\nSetting turn actions");
                    setTurnActions();
                } else {
                    System.out.println(action.run(delta));
                    if(mainParty.partyHasFled())
                    {
                        mainParty.clearHasFled();
                        this.setOver();
                    }
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
        for (Enemy e:enemyParty.getActiveParty())
        {
            System.out.println(e.dump());
            a = BattleAction.attack(e,mainParty.getRandom());
            addAction(a);
            System.out.println(a);
        }
        //Set friendly actions
        for (Hero h:mainParty.getActiveParty())
        {
            System.out.println(h.dump());
            if(h.getMana() > h.getSpell(0).getManaCost()){
                a = BattleAction.spell(h, h.getSpell(0), enemyParty.getRandom());
            } else {
                a = BattleAction.attack(h,enemyParty.getRandom());
            }
            addAction(a);
            System.out.println(a);
        }
    }

    public static void main(String[] args) {
        //create enemy party
        //create main party
        //Look into java timing.
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
        Spell yoloswag = new Spell(1, "yoloswag69", 1, 3, true);
        Hero smart = new Hero("Hero Econ", BattleClass.ECON, 5);
        Hero smart2 = new Hero("Hero Nat Sci", BattleClass.NAT_SCI, 5);
        Hero smart3 = new Hero("Hero Humanities", BattleClass.HUMANITIES, 5);
        Hero smart4 = new Hero("Hero Comp Sci", BattleClass.COMP_SCI, 5);
        smart.addSpells(yoloswag);
        smart2.addSpells(yoloswag);
        smart3.addSpells(yoloswag);
        smart4.addSpells(yoloswag);
        MainParty teamswag = new MainParty();
        teamswag.addCharacter(smart);
        teamswag.addCharacter(smart2);
        teamswag.addCharacter(smart3);
        teamswag.addCharacter(smart4);

        BattleTest battle = new BattleTest(teamyolo, "teamyolo");
        battle.ready(teamswag, null);

        while (!battle.isOver()) {
            float delta = 1f / 60;
            battle.update(delta);
            try {
                // sleep for 1/60 of a second
                Thread.sleep((int)(delta*1000));
            } catch(InterruptedException e) {
            }
        }
        System.out.println("IT'S OVER");
    }
}
