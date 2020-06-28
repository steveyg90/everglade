//  ██████╗  █████╗ ██╗   ██╗ ██████╗ █████╗ ███████╗████████╗
//  ██╔══██╗██╔══██╗╚██╗ ██╔╝██╔════╝██╔══██╗██╔════╝╚══██╔══╝
//  ██████╔╝███████║ ╚████╔╝ ██║     ███████║███████╗   ██║
//  ██╔══██╗██╔══██║  ╚██╔╝  ██║     ██╔══██║╚════██║   ██║
//  ██║  ██║██║  ██║   ██║   ╚██████╗██║  ██║███████║   ██║
//  ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚══════╝   ╚═╝
// 'JayCast' RayCast lighting system ;)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.sdgja.raycast;

import com.badlogic.gdx.Gdx;
import com.sdgja.actor.Actor;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.map.IMap;
import com.sdgja.map.MapGeneration;
import com.sdgja.utils.RangedRandom;

import java.util.Arrays;

import static com.sdgja.map.IMap.ZOOM;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class RayCast {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final float  angleStep       = 0.05f;    // Precision of player raycast
    private static final float  minBrightness   = 0.03f;   // Minimum brightness of non raycast tiles
    private static final float  plotDiv         = 5f;        // Divide by this when plotting raycast to lightmap (additive)
    private static final float  hardDiv         = 1.9f;     // Divide by this when ray hits solid tile
    private static final float  intensityMult   = 6f;       // Multiply by this for initial cast
    private static final float  decayDivisor    = 1.0125f;   // Divide by this for each step
    private static final int    playerMaxPlots  = 6;        // Maximum number of plots when hitting solids
    private static final int    playerMaxReach  = 35;       // Maximum reach of a ray

// These settings look reasonable to me. - JA
//    private static final float  angleStep       = 0.05f;    // Precision of player raycast
//    private static final float  minBrightness   = 0.05f;    // Minimum brightness of non raycast tiles
//    private static final float  plotDiv         = 6;        // Divide by this when plotting raycast to lightmap (additive)
//    private static final float  hardDiv         = 1.6f;     // Divide by this when ray hits solid tile
//    private static final float  intensityMult   = 10f;      // Multiply by this for initial cast
//    private static final float  decayDivisor    = 1.025f;   // Divide by this for each step
//    private static final int    playerMaxPlots  = 4;        // Maximum number of plots when hitting solids
//    private static final int    playerMaxReach  = 35;       // Maximum reach of a ray

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static float[][]lightMap = null;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static RayCast _instance = null;
    public static RayCast getInstance() {
        if(_instance==null) {
            _instance = new RayCast();
            int resX = (int) ((Gdx.graphics.getWidth() ) * ZOOM);
            resX /= IMap.TILEWIDTH;
            int resY = (int) ((Gdx.graphics.getHeight() ) * ZOOM);
            resY /= IMap.TILEHEIGHT;
            _instance.lightMap = new float[resX*3][resY*3];

        }
        return _instance;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private RayCast() {
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void clearLightmap() {
        for (int x = 0; x < lightMap.length; x++) {
            for (int y = 0; y < lightMap[0].length; y++) {
                lightMap[x][y] = minBrightness;
            }
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void rayCastPlayer(Actor player) {
//        clearLightmap();
// dX/dY = destination coords
        int dX = (int) (lightMap.length/3)        + (int) (player.getPlayerPosition().x*IMap.ZOOM/8)   - (int) (IntoHellGame.camera.position.x/8);
        int dY = (int) (lightMap[0].length/3)     + (int) (player.getPlayerPosition().y*IMap.ZOOM/8)   - (int) (IntoHellGame.camera.position.y/8);
// sX/sY = Source coords
        float sX= (player.getPlayerPosition().x*IMap.ZOOM) / 8;
        float sY= (player.getPlayerPosition().y*IMap.ZOOM) / 8;
        for (float step=0f;step<1.0f+angleStep;step+=angleStep){
            castRay (sX,sY,dX,dY,  1,      step,   1.0f-(step/2),playerMaxPlots,playerMaxReach );
            castRay (sX,sY,dX,dY,  -1,     step,   1.0f-(step/2),playerMaxPlots,playerMaxReach);
            castRay (sX,sY,dX,dY,      step,   1,  1.0f-(step/2),playerMaxPlots,playerMaxReach);
            castRay (sX,sY,dX,dY,      step,   -1, 1.0f-(step/2),playerMaxPlots,playerMaxReach);

            if (step<1.0f && step!=0f) {
                castRay (sX,sY,dX,dY,  1,      -step,   1.0f-(step/2),playerMaxPlots,playerMaxReach);
                castRay (sX,sY,dX,dY,  -1,     -step,   1.0f-(step/2),playerMaxPlots,playerMaxReach);
                castRay (sX,sY,dX,dY,      -step,   1,  1.0f-(step/2),playerMaxPlots,playerMaxReach);
                castRay (sX,sY,dX,dY,      -step,   -1, 1.0f-(step/2),playerMaxPlots,playerMaxReach);
            }
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final float torchAngleStep  = 0.2f;        // Precision
    private final int   torchMaxPlots   = 3;            // Penetration
    private final int   torchMaxReach   = 7;            // Guaranteed length
    private final int   torchRandom     = 3;           // Extra length

    public void rayCastTorch(float tileX,float tileY) {
//        clearLightmap();
// dX/dY = destination coords
        float leftLimit = (IntoHellGame.camera.position.x/8)-((lightMap.length/3)/2);
        if (leftLimit<0) { leftLimit=0; }

        float rightLimit = (IntoHellGame.camera.position.x/8)+((lightMap.length/3)+10);
        if (rightLimit>IMap.WIDTH) { rightLimit=IMap.WIDTH; }

        float topLimit = (IntoHellGame.camera.position.y/8)-((lightMap[0].length/3)/2);
        if (topLimit<0) { topLimit=0; }

        float bottomLimit = (IntoHellGame.camera.position.y/8)+((lightMap[0].length/3)+10);
        if (bottomLimit>IMap.HEIGHT) { bottomLimit=IMap.HEIGHT; }

        if (tileX<leftLimit || tileX> rightLimit || tileY< topLimit || tileY>bottomLimit){
//            System.out.println("Nope!");
            return;
        }
        float dX = (lightMap.length/3)+tileX-IntoHellGame.camera.position.x/8; //(lightMap.length/3)+10; //        + (int) (player.getPlayerPosition().x*IMap.ZOOM/8)   - (int) (camera.position.x/8); // + ((((FrameCount.getFrameCount())>>1)&15)>>3);
        float dY = (lightMap[0].length/3)+tileY-IntoHellGame.camera.position.y/8; //(lightMap[0].length/3)+10; //     + (int) (player.getPlayerPosition().y*IMap.ZOOM/8)   - (int) (camera.position.y/8); // + ((((FrameCount.getFrameCount())>>2)&7)>>3);
// sX/sY = Source coords
        float sX= tileX; //(player.getPlayerPosition().x*IMap.ZOOM) / 8;
        float sY= tileY; //(player.getPlayerPosition().y*IMap.ZOOM) / 8;
        for (float step=0f;step<1.0f+torchAngleStep;step+=torchAngleStep){
            castRay (sX,sY,dX,dY,  1,      step,   1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
            castRay (sX,sY,dX,dY,  -1,     step,   1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
            castRay (sX,sY,dX,dY,      step,   1,  1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
            castRay (sX,sY,dX,dY,      step,   -1, 1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));

            if (step<1.0f && step!=0f) {
                castRay (sX,sY,dX,dY,  1,      -step,   1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
                castRay (sX,sY,dX,dY,  -1,     -step,   1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
                castRay (sX,sY,dX,dY,      -step,   1,  1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
                castRay (sX,sY,dX,dY,      -step,   -1, 1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
            }
        }
    }


//    int dX = (int) (lightMap.length/3)+tileX; //        + (int) (player.getPlayerPosition().x*IMap.ZOOM/8)   - (int) (camera.position.x/8); // + ((((FrameCount.getFrameCount())>>1)&15)>>3);
//    int dY = (int) (lightMap[0].length/3)+tileY; //     + (int) (player.getPlayerPosition().y*IMap.ZOOM/8)   - (int) (camera.position.y/8); // + ((((FrameCount.getFrameCount())>>2)&7)>>3);
//    // sX/sY = Source coords
//    float sX= (IntoHellGame.camera.position.x/8)+tileX; //(player.getPlayerPosition().x*IMap.ZOOM) / 8;
//    float sY= (IntoHellGame.camera.position.y/8)+tileY; //(player.getPlayerPosition().y*IMap.ZOOM) / 8;
//        for (float step=0f;step<1.0f+torchAngleStep;step+=torchAngleStep){
//        castRay (sX,sY,dX,dY,  1,      step,   1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
//        castRay (sX,sY,dX,dY,  -1,     step,   1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
//        castRay (sX,sY,dX,dY,      step,   1,  1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
//        castRay (sX,sY,dX,dY,      step,   -1, 1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
//
//        if (step<1.0f && step!=0f) {
//            castRay (sX,sY,dX,dY,  1,      -step,   1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
//            castRay (sX,sY,dX,dY,  -1,     -step,   1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
//            castRay (sX,sY,dX,dY,      -step,   1,  1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
//            castRay (sX,sY,dX,dY,      -step,   -1, 1.0f-(step/2),torchMaxPlots, RangedRandom.generate(torchMaxReach,torchMaxReach+torchRandom));
//        }
//    }
//}



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void castRay (float sX,float sY, float dX,float dY, float stepX, float stepY, float intensity, int maxPlots, int lengtho){
        short[][] sMap = MapGeneration.getForegroundMap();
        intensity *=intensityMult;
        for (int count=0;count<(lengtho);count++){
            if (sX>=0 && sY>=0 && sX<(IMap.WIDTH-1) && sY<(IMap.HEIGHT-2)){
                if (sMap[(int) sX][(int) sY] != 0) {
                    intensity /= hardDiv;
                    maxPlots--;
                    if (maxPlots==0){
                        break;
                    }
                }
                if (intensity < minBrightness) {
                    break;
                }
                lightMap[(int) dX][(int) dY] += intensity / plotDiv;
            }
            intensity /=decayDivisor;
            sX+=stepX;
            sY+=stepY;
            dX+=stepX;
            dY+=stepY;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public float[][] getLightMap() {
        return lightMap;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void dispose() {
        Arrays.asList(lightMap).clear();
    }
}
