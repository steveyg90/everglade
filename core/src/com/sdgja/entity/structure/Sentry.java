package com.sdgja.entity.structure;

import com.sdgja.map.MapGeneration;
import com.sdgja.tmx.TmxRenderer;
import com.sdgja.utils.FrameCount;

public class Sentry extends Entity {

    enum act {
        normal,
        grow
    }

    // Constants
//    final int MAXBOUNCE = 6;        // How many tiles to bounce the drill when it hits a solid

    // Variables
    private act action;             // Current action of Sentry
    private int power;              // Current power
    private int timer1;             // Generic timer

    private int currentGrowth;      // Generic timer
    private int growthTimer;      // Generic timer

    IFunc play = () -> {

        structureMap = getStructureMap("sentry");  // structure map - need to use same name as when we created thumper
        switch (action) {
            case normal:
                drawLightning();
                break;
            case grow:
                growthTimer++;
                if (growthTimer==growthSpeed*3.3){
                    growthTimer=0;
                    currentGrowth++;
                    if (currentGrowth==TmxRenderer.getStructureHeight("sentry") ){
                        action=act.normal;
                    }
                    TmxRenderer.growStructureIntoMap("sentry", MapGeneration.getBuildingMap(), x, y,currentGrowth);

                }
                break;
            default:

        }
    };

    private void drawLightning() {
        short[][] map = MapGeneration.getBuildingMap();
// Red lightning in centre
        map[x + 2][y+4 + timer1] = 0;
        map[x + 3][y+4 + timer1] = 0;
        int redLight = 277;
        byte b = (byte) (FrameCount.getFrameCount() & 3);
        if (b == 0) {
            redLight += 128;
            timer1++;
            if (timer1 > 6) {
                timer1 = 0;
            }
        }
        map[x + 2][y+4 + timer1] = (short) redLight;
        map[x + 3][y+4 + timer1] = (short) (redLight + 1);

// Blue lightning at sides
        if (FrameCount.getFrameCount() > 64) {
//            map[x+1][y + 1] = 0;
//            map[x+1][y + 2] = 0;
            map[x][y + 6] = 0;
            map[x][y + 7] = 0;
            map[x + 5][y + 6] = 0;
            map[x + 5][y + 7] = 0;
        } else {
            b = (byte) (FrameCount.getFrameCount() & 4);
            if (b == 0) {
//                map[x+1][y + 1] = 275;
//                map[x+1][y + 2] = 275 + 128;
                map[x][y + 6] = 275;
                map[x][y + 7] = 275 + 128;
                map[x + 5][y + 6] = 276;
                map[x + 5][y + 7] = 276 + 128;
            } else {
//                map[x+1][y + 1] = 276;
//                map[x+1][y + 2] = 276 + 128;
                map[x][y + 6] = 276;
                map[x][y + 7] = 276 + 128;
                map[x + 5][y + 6] = 275;
                map[x + 5][y + 7] = 275 + 128;
            }

        }
    }


    public Sentry(int x, int y, int startPower, short[][] map) {
        super("sentry", x, y, map);
        TmxRenderer.allocateStructureIntoMap("sentry", MapGeneration.getBuildingMap(), x, y);
        {
            action = act.grow;
            currentGrowth =0;
            growthTimer=0;
            timer1 = 0;
            power = startPower;
            addActionAndSelf();
        }
    }


        public void addActionAndSelf () {
            addAction(play);
            EntityManager.addEntity(this);
        }
    }


