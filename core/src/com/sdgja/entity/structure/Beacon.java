package com.sdgja.entity.structure;

import com.sdgja.map.MapGeneration;
import com.sdgja.tmx.TmxRenderer;

public class Beacon extends Entity {

    enum act {
        normal,
        grow
    }

    // Constants
    final int MAXBOUNCE = 6;        // How many tiles to bounce the drill when it hits a solid

    // Variables
    private act action;             // Current action of Beacon
    private int power;
    private int timer1;             // Generic timer
    private int timer2;             // Generic timer
    private int currentGrowth;      // Generic timer
    private int growthTimer;      // Generic timer

    IFunc play = () -> {

        structureMap = getStructureMap("beacon");  // structure map - need to use same name as when we created thumper
        switch (action) {
            case normal:
                animateBeacon();
                break;
            case grow:
                growthTimer++;
                if (growthTimer==growthSpeed/2){
                    growthTimer=0;
                    currentGrowth++;
                    if (currentGrowth==TmxRenderer.getStructureHeight("beacon") ){
                        action=act.normal;
                    }
                    TmxRenderer.growStructureIntoMap("beacon", MapGeneration.getBuildingMap(), x, y,currentGrowth);

                }
                break;
            default:

        }
    };
    private static int[] beaconAnim = {23,23,23,23,23,25,27,29,27,25};

    private void animateBeacon() {
        short[][] map = MapGeneration.getBuildingMap();
        timer1++;
        if (timer1==12){
            timer1=0;
            timer2++;
            if (timer2==beaconAnim.length){
                timer2=0;
            }
            map[x][y]= (short) beaconAnim[timer2];
            map [x+1][y]  = (short) ((map[x][y])+1);
            map [x+0][y+1]= (short) ((map[x][y])+128);
            map [x+1][y+1]= (short) ((map[x][y])+129);
        }
    }

    public Beacon(int x, int y, int startPower, short[][] map) {
        super("beacon",x, y,map);
        TmxRenderer.allocateStructureIntoMap("beacon", MapGeneration.getBuildingMap(), x, y);
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

