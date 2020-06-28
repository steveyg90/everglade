
//  ███╗   ███╗ ██████╗ ██╗   ██╗███████╗███████╗██╗███╗   ██╗████████╗███████╗██████╗ ███████╗ █████╗  ██████╗███████╗
//  ████╗ ████║██╔═══██╗██║   ██║██╔════╝██╔════╝██║████╗  ██║╚══██╔══╝██╔════╝██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝
//  ██╔████╔██║██║   ██║██║   ██║███████╗█████╗  ██║██╔██╗ ██║   ██║   █████╗  ██████╔╝█████╗  ███████║██║     █████╗
//  ██║╚██╔╝██║██║   ██║██║   ██║╚════██║██╔══╝  ██║██║╚██╗██║   ██║   ██╔══╝  ██╔══██╗██╔══╝  ██╔══██║██║     ██╔══╝
//  ██║ ╚═╝ ██║╚██████╔╝╚██████╔╝███████║███████╗██║██║ ╚████║   ██║   ███████╗██║  ██║██║     ██║  ██║╚██████╗███████╗
//  ╚═╝     ╚═╝ ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝╚═╝  ╚═══╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝╚══════╝
//

package com.sdgja.Ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.sdgja.entity.structure.*;
import com.sdgja.map.FindFlatGround;
import com.sdgja.map.IMap;
import com.sdgja.map.MapGeneration;
import com.sdgja.raycast.Ambient;
import com.sdgja.raycast.RayCast;
import com.sdgja.tmx.TmxRenderer;
import com.sdgja.utils.IOUtil;
import com.sdgja.utils.MouseFocus;
import com.sdgja.utils.TVector;

import static com.sdgja.utils.DebugShell.clearSpawnString;
import static com.sdgja.utils.DebugShell.getSpawnString;

public final class MouseInterface {

    private static int placeX = 0;          // Record X,Y,W,D of place building
    private static int placeY = 0;          // Used for erasing previous drawing
    private static int placeW = 1;
    private static int placeD = 1;

    private static boolean validPlace;      // Is building positioned in a valid site for construction?

    // Used to determine what effects to use on graphics for valid vs invalid placement
    public static boolean isBuildingValid() {return validPlace;}

    // Check if player has a building ready for placement and guide player to suitable site.
    public static void checkBuildingPlacement(final int camX, final int camY, final float zoom) {

// Clear previous place graphics
        short[][] map = MapGeneration.getBuildingPlaceMap();
        for (int cY = placeY; cY < placeY + placeD; cY++) {
            for (int cX = placeX; cX < placeX + placeW; cX++)
                map[cX][cY] = 0;
        }

// Requesting a valid object?
        String key = getSpawnString();
        if(!TmxRenderer.getStructureKeys().contains(key)) {
            return;
        }
// Right mouse button to cancel positioning
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            placeBuildingHousekeeping();
            return;
        }

// Capture mouse position
        TVector<Integer> mousePos = IOUtil.getMousePosInMapSpace(camX, camY, zoom);
// Acquire, clamp to map limits and store current X,Y of mouse position and W,D of object
        placeW = TmxRenderer.getStructureWidth(key);
        placeD = TmxRenderer.getStructureHeight(key);
        placeX = mousePos.getX() - (placeW / 2);
        placeY = mousePos.getY() - (placeD / 2);
        if (placeX<0) {placeX=0; }
        if ((placeX+placeW)>= IMap.WIDTH) {placeX=IMap.WIDTH-placeW; }
        if (placeY<0) {placeY=0; }
        if ((placeY+placeD)>= IMap.HEIGHT) {placeY=IMap.HEIGHT-placeD; }

        if (key=="torch"){
            handleTorch(mousePos,key);
            return;
        }

        if (key=="foundation"){
            handleFoundation(mousePos,key);
            return;
        }



// Look for a flat run of grass to place current object
        Vector2 pos = FindFlatGround.findFlatRun(placeW, placeX, placeY);
        int destX = (int) pos.x;
        int destY = (int) pos.y;

        Vector2 check = FindFlatGround.findBuildingSpace(placeX,placeY,placeW, placeD);

        int diffX = Math.abs(destX - placeX);
        int diffY = Math.abs(destY - mousePos.getY());
        if ((diffX < ((placeW/2)+1) && diffY < placeD) && (destX != 0 || destY != 0) && (check.x != 0 || check.y != 0) ) {
            if (pressedLeftButton()) {
// Plot our object into the Building Map.
                switch (key) {
                    case "beacon":
                        new Beacon(destX, destY - (placeD - 3), 1, MapGeneration.getBuildingMap());
                        placeBuildingHousekeeping();
                        break;
                    case "thumper":
                        new Thumper(destX, destY - (placeD - 3), 25, MapGeneration.getBuildingMap());
                        placeBuildingHousekeeping();
                        break;
                    case "silo":
                        new Silo(destX, destY - (placeD - 3), 1, MapGeneration.getBuildingMap());
                        placeBuildingHousekeeping();
                        break;
                    case "sentry":
                        new Sentry(destX, destY - (placeD - 3), 1, MapGeneration.getBuildingMap());
                        placeBuildingHousekeeping();
                        break;
                    case "beanstalk":
                        new Beanstalk(destX, destY - (placeD - 3), 1, MapGeneration.getBuildingMap());
                        placeBuildingHousekeeping();
                        break;
                    default:
                        break;
                }
            } else {
// Building is in a valid site, but mouse button not pressed
                validPlace = true;
                placeX = destX;
                placeY = destY - (placeD - 3);
                TmxRenderer.drawStructureIntoMapSkip(key, MapGeneration.getBuildingPlaceMap(), placeX, placeY,3);
            }
        } else {
// Building is not on a valid site
            validPlace = false;
            TmxRenderer.drawStructureIntoMapSkip(key, MapGeneration.getBuildingPlaceMap(), placeX, placeY,3);
        }
    }

    // This is for objects that can be placed in caves, like torches etc..
    public static void handleTorch(TVector mousePos, String key) {
// Look for a suitable space for a torch
        Vector2 pos = FindFlatGround.findTorchSpace(placeX,placeY,placeW, placeD);
        int destX = (int) pos.x;
        int destY = (int) pos.y;
        if ((destX != 0 || destY != 0)) {
            Ambient.drawAmbientLight(placeX*(8/IMap.ZOOM),placeY*(8/IMap.ZOOM));
            if (pressedLeftButton()) {
// Plot our object into the Building Map.
                switch (key) {
                    case "torch":
                        new Torch(destX, destY, 100, MapGeneration.getBuildingMap());
                        placeBuildingHousekeeping();
                        break;
                    default:
                        break;
                }
            } else {
// Building is in a valid site, but mouse button not pressed
                validPlace = true;
                placeX = destX;
                placeY = destY;
                TmxRenderer.drawStructureIntoMap(key, MapGeneration.getBuildingPlaceMap(), placeX, placeY);
                RayCast.getInstance().rayCastTorch(placeX,placeY);
            }

        } else {
// Building is not on a valid site
            validPlace = false;
            TmxRenderer.drawStructureIntoMap(key, MapGeneration.getBuildingPlaceMap(), placeX, placeY);
        }
    }

    // This is for static objects, simple plot to the map, no logic.
    public static void handleFoundation(TVector mousePos, String key) {
// Look for a suitable space
        Vector2 pos = FindFlatGround.findFoundationSpace(placeX,placeY,placeW, placeD);
        int destX = (int) pos.x;
        int destY = (int) pos.y;
        if ((destX != 0 || destY != 0)) {
            placeX = destX;
            placeY = destY;
            if (pressedLeftButton()) {
// Plot our object into the Building Map.
                switch (key) {
                    case "foundation":
                        TmxRenderer.drawStructureIntoMap(key, MapGeneration.getForegroundMap(), placeX, placeY);
                        placeBuildingHousekeeping();
                        break;
                    default:
                        break;
                }
            } else {
// Building is in a valid site, but mouse button not pressed
                validPlace = true;
               TmxRenderer.drawStructureIntoMap(key, MapGeneration.getBuildingPlaceMap(), placeX, placeY);
            }

        } else {
// Building is not on a valid site
            validPlace = false;
            TmxRenderer.drawStructureIntoMap(key, MapGeneration.getBuildingPlaceMap(), placeX, placeY);
        }
    }




    // Housekeeping method to clear the spawn string and reset the 'place' variables.
    private static void placeBuildingHousekeeping () {
        if (getSpawnString()!="foundation") {
            clearSpawnString();
            placeX = 0;
            placeY = 0;
            placeW = 1;
            placeD = 1;
        }
    }

// Detect LMB press and ensure focus is in game window, not gui.
    private static boolean pressedLeftButton(){
        if ((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) && MouseFocus.getFocus() == MouseFocus.WindowFocus.Main){
            return true;
        } else{
            return false;
        }
    }

}
