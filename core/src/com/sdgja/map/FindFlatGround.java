
//  ███████╗██╗███╗   ██╗██████╗ ███████╗██╗      █████╗ ████████╗ ██████╗ ██████╗  ██████╗ ██╗   ██╗███╗   ██╗██████╗
//  ██╔════╝██║████╗  ██║██╔══██╗██╔════╝██║     ██╔══██╗╚══██╔══╝██╔════╝ ██╔══██╗██╔═══██╗██║   ██║████╗  ██║██╔══██╗
//  █████╗  ██║██╔██╗ ██║██║  ██║█████╗  ██║     ███████║   ██║   ██║  ███╗██████╔╝██║   ██║██║   ██║██╔██╗ ██║██║  ██║
//  ██╔══╝  ██║██║╚██╗██║██║  ██║██╔══╝  ██║     ██╔══██║   ██║   ██║   ██║██╔══██╗██║   ██║██║   ██║██║╚██╗██║██║  ██║
//  ██║     ██║██║ ╚████║██████╔╝██║     ███████╗██║  ██║   ██║   ╚██████╔╝██║  ██║╚██████╔╝╚██████╔╝██║ ╚████║██████╔╝
//  ╚═╝     ╚═╝╚═╝  ╚═══╝╚═════╝ ╚═╝     ╚══════╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚═════╝
//

package com.sdgja.map;

import com.badlogic.gdx.math.Vector2;

import static com.sdgja.utils.XMLReader.getTileId;

public final class FindFlatGround {

// To call method do:
// Vector2 pos = FindFlatGround.findFlatRun(100);

    public static Vector2 findFlatRun(int width, int startX, int startY) {
        short[][] map = MapGeneration.getInstance().getForegroundMap();
        short[][] bMap = MapGeneration.getInstance().getBuildingMap();
        for (int x = startX; x < startX + 20; x++) {
            for (int y = startY; y < startY + 20; y++) {
                if ((map[x][y] == 773 || map[x][y] == 774 || map[x][y] == 775) && bMap[x][y] == 0) {
                    int runWidth = 0;
                    for (int z = 0; z < width && x + z < IMap.WIDTH; z++) {
                        if ((map[x + z][y] == 773 || map[x + z][y] == 774 || map[x + z][y] == 775) && bMap[x + z][y] == 0) {
                            runWidth++;
                        } else {
                            runWidth = 0;
                        }
                        if (runWidth == width) {
                            return new Vector2(x, y);    //x,y);
                        }
                    }
                }
            }
        }
        return new Vector2(0, 0);
    }

// Find a space in the building maps
// If 0,0 returned, then no space found

    public static Vector2 findBuildingSpace(int startX, int startY, int width, int height) {
        short[][] bMap = MapGeneration.getInstance().getBuildingMap();
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                if (bMap[x][y] != 0) {
                    return new Vector2(0, 0);
                }
            }
        }
        return new Vector2(startX, startY);    //x,y);
    }


// Find a space in the foreground and building maps
// If 0,0 returned, then no space found

    public static Vector2 findTorchSpace(int startX, int startY, int width, int height) {
        short[][] map = MapGeneration.getInstance().getForegroundMap();
        short[][] bMap = MapGeneration.getInstance().getBuildingMap();
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                if (map[x][y] != 0 || bMap[x][y] != 0) {
                    return new Vector2(0, 0);
                }
            }
        }
        return new Vector2(startX, startY);    //x,y);
    }


// Find a space in the foreground and building maps suitable for a foundation tile.
// If 0,0 returned, then no space found

// NOTE THIS CODE IS HARD CODED FOR a 1X3 TMX!!

    public static Vector2 findFoundationSpace(int startX, int startY, int width, int height) {
        short[][] map = MapGeneration.getInstance().getForegroundMap();
        short[][] bMap = MapGeneration.getInstance().getBuildingMap();
        startX-=2;
        startY-=2;

        for (int x = 0; x < 4; x++) {
            for (int y=0;y<4;y++) {
                if (bMap[startX+x + 0][startY+y + 0] == 0 && bMap[startX+x + 1][startY+y + 0] == 0 && bMap[startX+x + 2][startY+y + 0] == 0 && bMap[startX+x + 0][startY+y + 1] == 0 && bMap[startX+x + 1][startY+y + 1] == 0 && bMap[startX+x + 2][startY+y + 2] == 0 && getTileId(map[startX+x + 0][startY+y + 0]) == 0 && getTileId(map[startX+x + 1][startY+y + 0]) == 0 && getTileId(map[startX+x + 2][startY+y + 0]) == 0 && getTileId(map[startX+x + 0][startY+y + 1]) != 0 && getTileId(map[startX+x + 1][startY+y + 1]) != 0 && getTileId(map[startX+x + 2][startY+y + 1]) != 0) {
                    return new Vector2(startX+x, startY+y);
                }
            }
        }
        return new Vector2(0, 0);
    }
}
