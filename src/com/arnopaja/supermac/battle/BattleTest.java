package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.*;

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
                // run code for if the main party is defeated
                System.out.println("You have lost");
            } else if (enemyParty.isDefeated()) {
                // run code for if the enemy party is defeated'
                System.out.println("You have won!");
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    System.out.println("Setting turn actions");
                    setTurnActions();
                } else {
                    System.out.println(action.run(delta).getText());
                }
            }
        }
    }

    @Override
    protected void setTurnActions() {
        // implement me!
        Random r = new Random();
        //Set enemy actions
        BattleAction a;
        for (Enemy e:enemyParty.getActiveParty())
        {
            System.out.println(e);
            a = BattleAction.attack(e,mainParty.getRandom());
            addAction(a);
            System.out.println(a);
        }
        //Set friendly actions
        for (Hero h:mainParty.getActiveParty())
        {
            System.out.println(h);
            a = BattleAction.attack(h,enemyParty.getRandom());
            addAction(a);
            System.out.println(a);
        }
    }

    public static void main(String[] args) {
        //create enemy party
        //create main party
        //Look into java timing.

        Enemy dumb = new Enemy("titface", BattleClass.COMP_SCI, 5, null, false);
        Enemy dumb2 = new Enemy("SwagFace", BattleClass.ECON, 6, null, false);
        Enemy dumb3 = new Enemy("highFace", BattleClass.HUMANITIES, 1, null, false);
        Enemy dumb4 = new Enemy("DrunkFace", BattleClass.NAT_SCI, 1, null, false);
        ArrayList<Enemy> enemlist = new ArrayList<Enemy>();
        enemlist.add(dumb);
        enemlist.add(dumb2);
        enemlist.add(dumb3);
        enemlist.add(dumb4);
        EnemyParty teamyolo = new EnemyParty(enemlist);

        Hero smart = new Hero("WonderWoman", BattleClass.ECON, 5);
        Hero smart2 = new Hero("Superman", BattleClass.NAT_SCI, 6);
        Hero smart3 = new Hero("Jesus", BattleClass.NAT_SCI, 5);
        Hero smart4 = new Hero("Swagtastic", BattleClass.COMP_SCI, 4);
        ArrayList<Hero> herolist = new ArrayList<Hero>();
        MainParty teamswag = new MainParty();
        teamswag.addCharacter(smart);
        teamswag.addCharacter(smart2);
        teamswag.addCharacter(smart3);
        teamswag.addCharacter(smart4);

        BattleTest battle = new BattleTest(teamyolo, "teamyolo");
        battle.ready(teamswag, null);

        while (true) {
            float delta = 1f / 60;
            battle.update(delta);
            try {
                // sleep for 1/60 of a second
                Thread.sleep((int)(delta*1000));
            } catch(InterruptedException e) {
            }
            // some timing function based on delta
        }
    }
}
