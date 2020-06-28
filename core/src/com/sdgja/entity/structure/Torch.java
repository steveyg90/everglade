//  ████████╗ ██████╗ ██████╗  ██████╗██╗  ██╗
//  ╚══██╔══╝██╔═══██╗██╔══██╗██╔════╝██║  ██║
//     ██║   ██║   ██║██████╔╝██║     ███████║
//     ██║   ██║   ██║██╔══██╗██║     ██╔══██║
//     ██║   ╚██████╔╝██║  ██║╚██████╗██║  ██║
//     ╚═╝    ╚═════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
//  Torch that can be placed in the world by the player

package com.sdgja.entity.structure;

import com.sdgja.map.IMap;
import com.sdgja.map.MapGeneration;
import com.sdgja.raycast.Ambient;
import com.sdgja.raycast.RayCast;
import com.sdgja.tmx.TmxRenderer;
import com.sdgja.utils.FrameCount;

public class Torch extends Entity {

    enum act {
        normal
    }

    // Constants
//    final int MAXBOUNCE = 6;        // How many tiles to bounce the drill when it hits a solid

    // Variables
    private act action;             // Current action of Beacon
    private int power;
//    private int timer1;             // Generic timer
//    private int timer2;             // Generic timer
//    private int currentGrowth;      // Generic timer
//    private int growthTimer;      // Generic timer

    IFunc play = () -> {

        structureMap = getStructureMap("torch");  // structure map - need to use same name as when we created thumper
        switch (action) {
            case normal:
                animateTorch();
                RayCast.getInstance().rayCastTorch(x,y);
                Ambient.drawAmbientLight(x*(8/IMap.ZOOM),y*(8/IMap.ZOOM));

                break;
            default:
                break;
        }
    };
    private static int[] torchAnim = {529,530,531,530,529,530,531,530};

    private void animateTorch() {
        short[][] map = MapGeneration.getBuildingMap();
        int index=(((FrameCount.getFrameCount())>>3)&7);
        map[x][y]= (short) torchAnim[index];
    }

    public Torch(int x, int y, int startPower, short[][] map) {
        super("torch",x, y,map);
        TmxRenderer.drawStructureIntoMap("torch", MapGeneration.getBuildingMap(), x, y);
        action=act.normal;
//        currentGrowth =0;
//        timer1=0;
//        timer2=0;
        power=startPower;
        addActionAndSelf();
    }

    public void addActionAndSelf() {
        addAction(play);
        EntityManager.addEntity(this);
    }
}

