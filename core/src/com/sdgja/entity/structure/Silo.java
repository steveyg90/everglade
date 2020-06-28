package com.sdgja.entity.structure;

import com.sdgja.map.MapGeneration;
import com.sdgja.tmx.TmxRenderer;

public class Silo extends Entity {

    enum act {
        normal,
        grow
    }

    // Constants
    final int MAXBOUNCE = 6;        // How many tiles to bounce the drill when it hits a solid

    // Variables
    private act action;             // Current action of Silo
    private int power;
    private int timer1;             // Generic timer
    private int timer2;             // Generic timer
    private int currentGrowth;      // Generic timer
    private int growthTimer;      // Generic timer

    IFunc play = () -> {

        structureMap = getStructureMap("silo");  // structure map - need to use same name as when we created thumper
        switch (action) {
            case normal:
                animateSilo();
                break;
            case grow:
                growthTimer++;
                if (growthTimer==growthSpeed/2){
                    growthTimer=0;
                    currentGrowth++;
                    if (currentGrowth==TmxRenderer.getStructureHeight("silo") ){
                        action=act.normal;
                    }
                    TmxRenderer.growStructureIntoMap("silo", MapGeneration.getBuildingMap(), x, y,currentGrowth);

                }
                break;
            default:

        }
    };
    private static int[] siloAnim  = {23,25,27,29,27,25};
    private static int[] siloAnim2 = {29,27,25,23,25,27};
    private static int[] siloAnim3 = {25,23,25,27,27,27};

    private static int[] siloBottom1 = {31,33,35,37,35,33};

    private void animateSilo() {
        short[][] map = MapGeneration.getBuildingMap();
        timer1++;
        if (timer1==12){
            timer1=0;
            timer2++;
            if (timer2==siloAnim.length){
                timer2=0;
            }
            map [x+4][y]  = (short) siloAnim[timer2];
            map [x+5][y]  = (short) (siloAnim[timer2]+1);

            map [x+0][y+1]  = (short) siloAnim2[timer2];
            map [x+1][y+1]  = (short) (siloAnim2[timer2]+1);
            map [x+0][y+1+3]  = (short) siloBottom1[timer2];
            map [x+1][y+1+3]  = (short) (siloBottom1[timer2]+1);

            map [x+8][y+1]  = (short) siloAnim2[timer2];
            map [x+9][y+1]  = (short) (siloAnim2[timer2]+1);
            map [x+8][y+1+3]  = (short) siloBottom1[timer2];
            map [x+9][y+1+3]  = (short) (siloBottom1[timer2]+1);

            map [x+1][y+5]  = (short) siloAnim[timer2];
            map [x+2][y+5]  = (short) (siloAnim[timer2]+1);
            map [x+1][y+5+3]  = (short) siloBottom1[timer2];
            map [x+2][y+5+3]  = (short) (siloBottom1[timer2]+1);

            map [x+7][y+5]  = (short) siloAnim[timer2];
            map [x+8][y+5]  = (short) (siloAnim[timer2]+1);
            map [x+7][y+5+3]  = (short) siloBottom1[timer2];
            map [x+8][y+5+3]  = (short) (siloBottom1[timer2]+1);

            map [x+0][y+9]  = (short) siloAnim3[timer2];
            map [x+1][y+9]  = (short) (siloAnim3[timer2]+1);
            map [x+0][y+9+3]  = (short) siloBottom1[timer2];
            map [x+1][y+9+3]  = (short) (siloBottom1[timer2]+1);

            map [x+8][y+9]  = (short) siloAnim3[timer2];
            map [x+9][y+9]  = (short) (siloAnim3[timer2]+1);
            map [x+8][y+9+3]  = (short) siloBottom1[timer2];
            map [x+9][y+9+3]  = (short) (siloBottom1[timer2]+1);
            //            map [x+0][y+1]=(map[x][y])+128;
//            map [x+1][y+1]=(map[x][y])+129;
        }
    }

    public Silo(int x, int y, int startPower, short[][] map) {
        super("silo",x, y,map);
        TmxRenderer.allocateStructureIntoMap("silo", MapGeneration.getBuildingMap(), x, y);
        action=act.grow;
        currentGrowth =0;
        timer1=0;
        timer2=0;
        power=startPower;
        addActionAndSelf();
    }

    public void addActionAndSelf() {
        addAction(play);
        EntityManager.addEntity(this);
    }
}

