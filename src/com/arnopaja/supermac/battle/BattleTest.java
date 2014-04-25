package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.*;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.inventory.Item;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Ari Weiland
 */
public class BattleTest extends Battle {

    public BattleTest(EnemyParty enemyParty, MainParty mainParty, String backgroundName) {
        super(enemyParty, mainParty, backgroundName);
    }

    @Override
    public void update(float delta) {
        if (isReady()) {
            if (mainParty.isDefeated()) {
                // run code for if the main party is defeated
                System.out.println("NIGGA YOU DEAD");
            } else if (enemyParty.isDefeated()) {
                // run code for if the enemy party is defeated'
                System.out.println("NIGGA THEY DEAD");
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    System.out.println("SETTIN YO ACTIONS N SHEEEEIT");
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
        for (Enemy e:enemyParty)
        {
            a = BattleAction.attack(e,mainParty.getRandom());
            addAction(a);
            System.out.println(a);
        }
        //Set friendly actions
        for (Hero h:mainParty.getActiveParty())
        {
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

        BattleTest battle = new BattleTest(teamyolo, teamswag, "teamyolo");
        //DialogueHandler handler = new DialogueHandler();
        //battle.ready(teamswag, null);

        while (true) {
            float delta = 1f / 60;
            System.out.println("FUCKYODICKNIGGA");
            battle.update(delta);
            try {
                Thread.sleep((int)(delta*1000));
            } catch(InterruptedException e) {
            }
            // some timing function based on delta
        }
    }
}
