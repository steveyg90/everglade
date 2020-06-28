
//   ██████╗ █████╗ ██╗   ██╗███████╗ ██████╗ ███████╗███╗   ██╗███████╗██████╗  █████╗ ████████╗██╗ ██████╗ ███╗   ██╗
//  ██╔════╝██╔══██╗██║   ██║██╔════╝██╔════╝ ██╔════╝████╗  ██║██╔════╝██╔══██╗██╔══██╗╚══██╔══╝██║██╔═══██╗████╗  ██║
//  ██║     ███████║██║   ██║█████╗  ██║  ███╗█████╗  ██╔██╗ ██║█████╗  ██████╔╝███████║   ██║   ██║██║   ██║██╔██╗ ██║
//  ██║     ██╔══██║╚██╗ ██╔╝██╔══╝  ██║   ██║██╔══╝  ██║╚██╗██║██╔══╝  ██╔══██╗██╔══██║   ██║   ██║██║   ██║██║╚██╗██║
//  ╚██████╗██║  ██║ ╚████╔╝ ███████╗╚██████╔╝███████╗██║ ╚████║███████╗██║  ██║██║  ██║   ██║   ██║╚██████╔╝██║ ╚████║
//   ╚═════╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝ ╚═════╝ ╚══════╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
//

package com.sdgja.map;

import com.sdgja.tmx.TmxRenderer;
import com.sdgja.utils.RangedRandom;

import java.util.Random;

import static com.sdgja.map.MapGeneration.*;
import static com.sdgja.tmx.TmxRenderer.getStructureHeight;
import static com.sdgja.tmx.TmxRenderer.getStructureWidth;


final public class CaveGeneration implements IMap {

    private static Random rand;

    // private static int tunnelHeights [] = {6,7,8,9,10,11,12,13,14,15,16,17,16,15,14,13,12,11,10,9,8,7};


    static {
        rand = new Random();
    }


    public static short[][] initialiseMap(short[][] map, float chance) {
        for (int x = 0; x < IMap.WIDTH; x++) {
            for (int y = 0; y < IMap.HEIGHT; y++) {
                map[x][y] = (short) (rand.nextFloat() < chance ? 1 : 0);
            }
        }

        return map;
    }

    public static int countAliveNeighbours(short[][] map, int x, int y){
        int count = 0;
        for(int i=-1; i<2; i++){
            for(int j=-1; j<2; j++){
                int neighbour_x = x+i;
                int neighbour_y = y+j;
                //If we're looking at the middle point
                if(i == 0 && j == 0){
                    //Do nothing, we don't want to add ourselves in!
                }
                //In case the index we're looking at it off the edge of the map
                else if(neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length){
                    count++;
                }
                //Otherwise, a normal check of the neighbour
                else if(map[neighbour_x][neighbour_y]==0x1 /*'#'*/) {
                    count++;
                }
            }
        }
        return count;
    }

    public static short[][] doSimulationStep(short[][] oldMap, int deathLimit, int birthLimit){

        short[][] newMap = new short[IMap.WIDTH][IMap.HEIGHT];

        //Loop over each row and column of the map
        for(int x=0; x<oldMap.length; x++){
            for(int y=0; y<oldMap[0].length; y++){
                int nbs = countAliveNeighbours(oldMap, x, y);
                //The new value is based on our simulation rules
                //First, if a cell is alive but has too few neighbours, kill it.
                if(oldMap[x][y]==0x1){
                    if(nbs < deathLimit){
                        newMap[x][y] = 0x1;//'#';
                    }
                    else{
                        newMap[x][y] = 0;
                    }
                } //Otherwise, if the cell is dead now, check if it has the right number of neighbours to be 'born'
                else{
                    if(nbs > birthLimit){
                        newMap[x][y] = 0;
                    }
                    else{
                        newMap[x][y] = 0x1;//'#';
                    }
                }
            }
        }
        return newMap;
    }

/////////////////////////////////////////////////////////////////////////
// Start of Horizon Code
/////////////////////////////////////////////////////////////////////////

    // Possible variations for horizon draw
    enum horizonStates {
        curveUp,
        flat,
        flat2,
        curveDown
    }

    // Current player status
    private static CaveGeneration.horizonStates horizonState = CaveGeneration.horizonStates.flat;

/////////////////////////////////////////////////////////////////////////

    public static void createHorizon(short[][] map, short[][] backMap) {

//        TmxRenderer.drawStructureIntoMapSkip0("treeCanopy", getBackgroundMap(),(IMap.WIDTH/2)+30 , (IMap.PLATFORM_HEIGHT)+(IMap.PLATFORM_HEIGHT-getStructureHeight("treeCanopy")));
//        plantBush((IMap.WIDTH/2)-25);


        int topLimit = IMap.PLATFORM_HEIGHT;
        int bottomLimit = IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT;
        float y1 = (bottomLimit / 2) + (topLimit / 2);
        float x1 = 0;
        float xStep = 1f;
        float yStep = 0.1f;
        float yStepAdd = 0.05f;
        int count = 30;
        while (x1 < IMap.WIDTH - 1) {
// Plot floor tile column

            int randomAdd = RangedRandom.generate(0, 10);
            if (randomAdd > 7) {
                randomAdd = RangedRandom.generate(25, 30);
            }
            for (int i = 0; i < 6 + (randomAdd / 2); i++) {
                map[(int) x1][(int) y1 + i] = 1;
            }

// Clear tiles above floor column
            int temp = (int) y1;
            while (temp > 0) {
                temp--;
                map[(int) x1][temp] = 0;
            }

// Set backmap tiles from ground column to end of platform+terrain
            temp = (int) y1;
            randomAdd = RangedRandom.generate(0, 10);
            if (randomAdd > 7) {
                randomAdd = RangedRandom.generate(25, 30);
            }
            while (temp < (IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT + 25 + randomAdd)) {
                backMap[(int) x1][temp] = 5;
                temp++;
            }
            count--;
            if (xStep > 1f) {
                xStep = 1f;
            }
            switch (horizonState) {
                case flat:
                    if (count > 0) {
                        x1 += xStep;
                        y1 += yStep;
                        if (yStep < 0) {
                            yStep -= yStepAdd;
                        } else {
                            yStep += yStepAdd;
                        }
                        if (y1 < topLimit) {
                            y1++;
                            yStep = 0.2f;
                        }
                        if (y1 > bottomLimit) {
                            y1--;
                            yStep = -0.2f;
                        }
                    } else {
                        if (y1 - topLimit < 30) {
                            count = RangedRandom.generate((IMap.TERRAIN_HEIGHT / 10), (IMap.TERRAIN_HEIGHT / 5));
                            xStep = 0.40f + (float) Math.random();
                            yStep = (float) 0.1f + (RangedRandom.generate(5, 50) / 1000);
                            yStepAdd = (float) 0.052f + (RangedRandom.generate(1, 15) / 1000);
                            horizonState = horizonStates.curveDown;
                        } else {
                            count = RangedRandom.generate((IMap.TERRAIN_HEIGHT / 10), (IMap.TERRAIN_HEIGHT / 5));
                            xStep = 0.40f + (float) Math.random();
                            yStep = (float) 0.05f + (RangedRandom.generate(5, 50) / 100);
                            yStepAdd = (float) 0.048f + (RangedRandom.generate(1, 15) / 100);
                            horizonState = horizonStates.curveUp;
                        }
                    }
                    break;
                case flat2:
                    if (count > 0) {
                        x1 += xStep;
                        y1 += yStep;
                        if (yStep < 0) {
                            yStep -= yStepAdd;
                        } else {
                            yStep += yStepAdd;
                        }
                        if (y1 < topLimit) {
                            y1++;
                            yStep = 0.2f;
                        }
                        if (y1 > bottomLimit) {
                            y1--;
                            yStep = -0.2f;
                        }
                    } else {
                        if (bottomLimit - y1 > 40) {
                            count = RangedRandom.generate((IMap.TERRAIN_HEIGHT / 10), (IMap.TERRAIN_HEIGHT / 5));
                            xStep = 0.420f + (float) Math.random();
                            yStep = (float) 0.1f + (RangedRandom.generate(5, 50) / 1000);
                            yStepAdd = (float) 0.052f + (RangedRandom.generate(1, 15) / 1000);
                            horizonState = horizonStates.curveDown;
                        } else {
                            count = RangedRandom.generate((IMap.TERRAIN_HEIGHT / 10), (IMap.TERRAIN_HEIGHT / 5));
                            xStep = 0.50f + (float) Math.random();
                            yStep = (float) 0.05f + (RangedRandom.generate(5, 50) / 100);
                            yStepAdd = (float) 0.048f + (RangedRandom.generate(1, 15) / 100);
                            horizonState = horizonStates.curveUp;
                        }
                    }
                    break;
                case curveUp:
                    if (count > 0) {
                        x1 += xStep;
                        y1 -= yStep;
                        yStep += yStepAdd;
                    } else {

                        if (RangedRandom.generate(1, 10) > 8) {
                            count = RangedRandom.generate((IMap.TERRAIN_HEIGHT / 10), (IMap.TERRAIN_HEIGHT / 5));
                            xStep = 0.50f + (float) Math.random();
                            yStep = (float) (0.1f + (RangedRandom.generate(5, 50) / 1000)) * 2;
                            yStepAdd = (float) 0.052f + (RangedRandom.generate(1, 15) / 1000);
                            horizonState = horizonStates.curveDown;
                        } else {

                            count = RangedRandom.generate((IMap.TERRAIN_HEIGHT / 8), (IMap.TERRAIN_HEIGHT / 4));
                            xStep = 1f;
                            yStepAdd = 0.007f;
                            if (count > 22) {
                                yStepAdd *= 2.25;
                                count *= 2.5;
                            }
                            if (y1 < (((IMap.PLATFORM_HEIGHT * 2) + IMap.TERRAIN_HEIGHT) / 2)) {
                                yStep = 0.0051f;
                            } else {
                                yStep = -0.0051f;
                            }
                            horizonState = horizonStates.flat2;
                        }
                    }
                    break;
                case curveDown:
                    if (count > 0) {
                        x1 += xStep;
                        y1 += yStep;
                        yStep += yStepAdd;
                    } else {
                        count = RangedRandom.generate((IMap.TERRAIN_HEIGHT / 6), (IMap.TERRAIN_HEIGHT / 3));
                        if (count > 15) {
                            count *= 2;
                        }
                        xStep = 1f;
                        yStepAdd = 0.001f;
                        if (y1 < (((IMap.PLATFORM_HEIGHT * 2) + IMap.TERRAIN_HEIGHT) / 2)) {
                            yStep = 0.01f;
                        } else {
                            yStep = -0.01f;
                        }
                        horizonState = horizonStates.flat;
                    }
                    break;
                default:
            }


        }


//Copy map to background map
        for (int xx = 1; xx < IMap.WIDTH - 2; xx++) {
            for (int yy = 1; yy < IMap.HEIGHT - 2; yy++) {
                if (map[xx][yy] == 1) {
                    backMap[xx + 0][yy + 0] = 5;
                    backMap[xx + 1][yy + 1] = 5;
                    backMap[xx - 1][yy + 1] = 5;

                    if (yy > IMap.TERRAIN_HEIGHT + IMap.PLATFORM_HEIGHT) {
                        backMap[xx - 1][yy - 1] = 5;
                        backMap[xx + 1][yy - 1] = 5;
                    } else {
                        backMap[xx + 1][yy + 0] = 5;
                        backMap[xx - 1][yy + 0] = 5;
                    }
                }
            }
        }


///////////////////////////////////////////////////////

// Draw some holes in the surface
        float i = 0;
        for (int y = 0; y < IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT + 120; y++) {
            int adjuster = (int) i;
            for (int x = 0; x < 2 + (adjuster / 5); x++) {
                map[adjuster + x + (IMap.WIDTH / 4)][y] = 0;
                map[adjuster + x + (IMap.WIDTH / 2)][y] = 0;
                map[adjuster + x + ((IMap.WIDTH / 4) * 3)][y] = 0;
            }
            i += .17f;
        }

//  Make caves wider
        for (int x = 1; x < IMap.WIDTH - 2; x++) {
            for (int y = IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT; y < IMap.HEIGHT - 2; y++) {
                if (map[x][y] == 0 && map[x + 1][y] == 1) {
                    map[x + 1][y] = -1;
                }
            }
        }
        for (int x = 0; x < IMap.WIDTH - 1; x++) {
            for (int y = IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT; y < IMap.HEIGHT - 1; y++) {
                if (map[x][y] == -1) {
                    map[x][y] = 0;
                }
            }
        }

// Make caves taller
        for (int x = 0; x < IMap.WIDTH - 1; x++) {
            for (int y = IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT; y < IMap.HEIGHT - 1; y++) {
                if (map[x][y + 1] == 0 && map[x][y] == 1) {
//                    if (y< IMap.PLATFORM_HEIGHT+ IMap.TERRAIN_HEIGHT+60) {
                    map[x][y] = 0;
//                        }
                    if (y< IMap.PLATFORM_HEIGHT+ IMap.TERRAIN_HEIGHT+30) {
                        map[x][y - 1] = 0;
                    }
                }
            }
        }

        // Make caves taller
        for (int x = 0; x < IMap.WIDTH - 1; x++) {
            for (int y = IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT; y < IMap.HEIGHT - 1; y++) {
                if (map[x][y + 1] == 0 && map[x][y] == 1) {
//                    if (y< IMap.PLATFORM_HEIGHT+ IMap.TERRAIN_HEIGHT+60) {
                    map[x][y] = 0;
//                        }
//                    if (y< IMap.PLATFORM_HEIGHT+ IMap.TERRAIN_HEIGHT+30) {
//                        map[x][y - 1] = 0;
//                    }
                }
            }
        }

// Dinosaur bones
        short [][]resourceMap=getResourceMap();

        for (int dinoCount = 0; dinoCount < 16; dinoCount++) {
            int dinoX = RangedRandom.generate(20, IMap.WIDTH - 20);
//            dinoX =50;
//            int dinoY = IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT + 50 + (dinoCount * 6) + 10;
            int dinoY = IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT + (dinoCount * 6);
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    resourceMap[dinoX + x][dinoY + y] = (short) ((3 * 128) + 1 + x + (128 * y));

                    map[dinoX + x][dinoY + y] = 1; // **
                    map[dinoX + x][dinoY - 1] = 1;
                    map[dinoX + x][dinoY + 4] = 1;
                    map[dinoX - 1][dinoY + y] = 1;
                    map[dinoX + 4][dinoY + y] = 1;
                }
            }
        }

// Round corners of caves
        decorateCave(map, 1, 0);
//        decorateCave(backMap, 5, 6 * 128);

// Precious metal seams
// 640/768 = Silver chars
// 384/512 = Gold chars

        createSeam(map, 640, 768, 10, 30, 50);
        createSeam(map, 384, 512, 60, 30, 70);

        createSeam(map, 640, 768, 150, 30, 70);
        createSeam(map, 384, 512, 200, 30, 70);

// Pass through map to  update mud texture from 1x1 char to 2x2 char
        int timer = RangedRandom.generate(10, 20);
        for (int y = 0; y < IMap.HEIGHT; y++) {
            for (int x = 0; x < IMap.WIDTH; x++) {
// Foreground Map
                if (map[x][y] == 1) {
                    timer--;
                    if (timer == 0) {
//// This adds fools gold randomly
                        timer = (RangedRandom.generate(10, 20))|1;
                        if (x % 2 == 0 && y % 2 == 0) {
                            map[x][y] = 3;
                            if (map[x + 1][y] == 1) {
                                map[x + 1][y] = 4;
                            }
                        } else {
                            if (x % 2 == 0 && y % 2 == 1) {
                                map[x][y] = 128 + 3;
                            } else {
                                if (x % 2 == 1 && y % 2 == 1) {
                                    map[x][y] = 128 + 4;
                                } else {
                                    map[x][y] = (short) (1 + (x % 2) + ((y % 2) * 128));
                                }
                            }
                        }
                    } else {
// Normal tile
                        map[x][y] = (short) (1 + (x % 2) + ((y % 2) * 128));
                    }
                }
            }
        }


        drawBackgroundDesigns(getBackgroundMap(), "bigTreeBack", IMap.WIDTH / 2, 1);
        drawBackgroundDesigns(getForegroundMap(), "bigTree", IMap.WIDTH / 2, 1);
        forestBiome(((IMap.WIDTH/8)*0),((IMap.WIDTH/8)*1) );
        jungleBiome(((IMap.WIDTH/8)*1),((IMap.WIDTH/8)*2) );
        arcticBiome(((IMap.WIDTH/8)*2),((IMap.WIDTH/8)*3) );
        desertBiome(((IMap.WIDTH/8)*3),((IMap.WIDTH/8)*4) );

        arcticBiome(((IMap.WIDTH/8)*4),((IMap.WIDTH/8)*5) );
        jungleBiome(((IMap.WIDTH/8)*5),((IMap.WIDTH/8)*6) );
        forestBiome(((IMap.WIDTH/8)*6),((IMap.WIDTH/8)*7) );
        desertBiome(((IMap.WIDTH/8)*7),((IMap.WIDTH/8)*8) );

// Round corners of caves
//        decorateCave(map, 1, 0);
        decorateCave(backMap, 5, 6 * 128);


// Pass through map to  update mud texture from 1x1 char to 2x2 char
        timer = RangedRandom.generate(10, 20);
        for (int y = 0; y < IMap.HEIGHT; y++) {
            for (int x = 0; x < IMap.WIDTH; x++) {
/// Background Map
                if (backMap[x][y] == 5) {
                    timer--;
                    if (timer == 0) {
// This adds fools gold randomly
                        timer = (RangedRandom.generate(6, 15))|1;
                        if (x % 2 == 0 && y % 2 == 0) {
                            backMap[x][y] = 7;
                            if (backMap[x + 1][y] == 5) {
                                backMap[x + 1][y] = 8;
                            }
                        } else {
                            if (x % 2 == 0 && y % 2 == 1) {
                                backMap[x][y] = 128 + 7;
                            } else {
                                if (x % 2 == 1 && y % 2 == 1) {
                                    backMap[x][y] = 128 + 8;
                                } else {
                                    backMap[x][y] = (short) (5 + (x & 1) + ((y & 1) * 128));
                                }
                            }
                        }
                    } else {
                        backMap[x][y] = (short) (5 + (x & 1) + ((y & 1) * 128));
                    }
                }
            }
        }

        // Draw upper and lower limits of horizon into map
        for (int x = 0; x < IMap.WIDTH; x++) {
            map[x][topLimit] = 128;
            map[x][bottomLimit] = 128;
        }

    }


    private static void forestBiome(int startX,int endX){
        int midX=(startX+endX)/2;

        plantFoliage(startX, "treeCanopy");             // Start biome with canopy
        startX += getStructureWidth("treeCanopy");

        if (RangedRandom.generate(1,5)>2) { plantFoliage(startX, "shrub1"); }
        startX += getStructureWidth("shrub1");

        if (RangedRandom.generate(1,5)>1) { plantFoliage(startX, "shrub4"); }
        startX += getStructureWidth("shrub2")*4;

        while (1==1) {
            if (startX + getStructureWidth("bush1")+(getStructureWidth("shrub1")*2) < endX) {
                plantFoliage(startX, "bush1");
                startX += getStructureWidth("bush1");
                if (RangedRandom.generate(1,5)>3) { plantFoliage(startX, "shrub2"); }
                startX += getStructureWidth("shrub2");
                if (RangedRandom.generate(1,5)>2) { plantFoliage(startX, "shrub3"); }
                startX += getStructureWidth("shrub3");
            } else { break; }
            if (startX + (getStructureWidth("tree1")*2)+(getStructureWidth("shrub1")*2) < endX) {
                plantFoliage(startX, "tree1");
                startX += getStructureWidth("tree1");
                if (RangedRandom.generate(1,5)>2) { plantFoliage(startX, "tree1"); }
                startX += getStructureWidth("tree1");
                if (RangedRandom.generate(1,5)>2) { plantFoliage(startX, "shrub4"); }
                startX += getStructureWidth("shrub1");
                if (RangedRandom.generate(1,5)>2) { plantFoliage(startX, "shrub2"); }
                startX += getStructureWidth("shrub2");
            } else { break; }
            if (startX + getStructureWidth("tree2")+(getStructureWidth("shrub1")*2) < endX) {
                plantFoliage(startX, "tree2");
                startX += getStructureWidth("tree2");
                if (RangedRandom.generate(1,5)>1) { plantFoliage(startX, "shrub4"); }
                startX += getStructureWidth("shrub3");
                if (RangedRandom.generate(1,5)>1) { plantFoliage(startX, "shrub2"); }
                startX += getStructureWidth("shrub2");
            } else { break; }
        }
        drawBackgroundDesigns(getBackgroundMap(), "holyRocks", midX-(getStructureWidth("holyRocks")), 2);

    }

    private static void jungleBiome(int startX,int endX){
        int midX=(startX+endX)/2;
        short [][]map=getForegroundMap();
        for (int y = IMap.PLATFORM_HEIGHT; y < IMap.PLATFORM_HEIGHT+IMap.TERRAIN_HEIGHT+100; y++) {
            for (int x = startX; x < endX; x++) {
                for (int idx = 0; idx < baseArray.length; idx++) {
                    if (baseArray[idx] == map[x][y]) {
                        map[x][y] = (short) jungleArray[idx];
                        break;
                    }
                }
            }
        }
        while (1==1) {
            if (startX + getStructureWidth("aztec2")*2 < endX) {
                plantFoliage(startX, "aztec2");
                startX += getStructureWidth("aztec2")*2;
            } else { break; }
            if (startX + getStructureWidth("rock1")*2 < endX) {
                plantFoliage(startX, "rock1");
                startX += getStructureWidth("rock1")*2;
            } else { break; }
            if (startX + getStructureWidth("aztec5")*2 < endX) {
                plantFoliage(startX, "aztec5");
                startX += getStructureWidth("aztec5")*2;
            } else { break; }
            if (startX + getStructureWidth("aztec3")*2 < endX) {
                plantFoliage(startX, "aztec3");
                startX += getStructureWidth("aztec3")*2;
            } else { break; }
            if (startX + getStructureWidth("aztec1")*2 < endX) {
                plantFoliage(startX, "aztec1");
                startX += getStructureWidth("aztec1")*2;
            } else { break; }
            if (startX + getStructureWidth("aztec4")*2 < endX) {
                plantFoliage(startX, "aztec4");
                startX += getStructureWidth("aztec4")*2;
            } else { break; }
         }
        drawBackgroundDesigns(getBackgroundMap(), "megaVine", midX-(getStructureWidth("megaVine")), 2);
    }


    private static void arcticBiome(int startX,int endX){
        short [][]map=getForegroundMap();
        for (int y = IMap.PLATFORM_HEIGHT; y < IMap.PLATFORM_HEIGHT+IMap.TERRAIN_HEIGHT+100; y++) {
            for (int x = startX; x < endX; x++) {
                for (int idx = 0; idx < baseArray.length; idx++) {
                    if (baseArray[idx] == map[x][y]) {
                        map[x][y] = (short) arcticArray[idx];
                        break;
                    }
                }
            }
        }
        while (1==1) {
            if (startX + getStructureWidth("pineTree")*2 < endX) {
                plantFoliage(startX, "pineTree");
                startX += getStructureWidth("pineTree")*2;
            } else { break; }
        }
    }

    private static void desertBiome(int startX,int endX){
        int midX=(startX+endX)/2;
        short [][]map=getForegroundMap();
        for (int y = IMap.PLATFORM_HEIGHT; y < IMap.PLATFORM_HEIGHT+IMap.TERRAIN_HEIGHT+100; y++) {
            for (int x = startX; x < endX; x++) {
                for (int idx = 0; idx < baseArray.length; idx++) {
                    if (baseArray[idx] == map[x][y]) {
                        map[x][y] = (short) desertArray[idx];
                        break;
                    }
                }
            }
        }
        while (1==1) {
            if (startX + getStructureWidth("cactus")*2 < endX) {
                plantFoliage(startX, "cactus");
                startX += getStructureWidth("cactus")*2;
            } else { break; }
            if (startX + getStructureWidth("column")*2 < endX) {
                plantFoliage(startX, "column");
                startX += getStructureWidth("column")*2;
            } else { break; }
            if (startX + getStructureWidth("palmTree1")*2 < endX) {
                plantFoliage(startX, "palmTree1");
                startX += getStructureWidth("palmTree1")*2;
            } else { break; }
            if (startX + getStructureWidth("palmTree2")*2 < endX) {
                plantFoliage(startX, "palmTree2");
                startX += getStructureWidth("palmTree2")*2;
            } else { break; }
            if (startX + getStructureWidth("column2")*2 < endX) {
                plantFoliage(startX, "column2");
                startX += getStructureWidth("column2")*2;
            } else { break; }
            if (startX + getStructureWidth("bigWall2")*1.5 < endX) {
                plantFoliage(startX, "bigWall2");
                startX += getStructureWidth("bigWall2")*1.5;
            } else { break; }
            if (startX + getStructureWidth("deadTree1")*2 < endX) {
                plantFoliage(startX, "deadTree1");
                startX += getStructureWidth("deadTree1")*2;
            } else { break; }
            if (startX + getStructureWidth("cactus2") < endX) {
                plantFoliage(startX, "cactus2");
                startX += getStructureWidth("cactus2");
            } else { break; }
            if (startX + getStructureWidth("cactus2") < endX) {
                plantFoliage(startX, "cactus2");
                startX += getStructureWidth("cactus2");
            } else { break; }
            if (startX + getStructureWidth("cactus") < endX) {
                plantFoliage(startX, "cactus");
                startX += getStructureWidth("cactus");
            } else { break; }
            if (startX + getStructureWidth("rockPile")*2 < endX) {
                plantFoliage(startX, "rockPile");
                startX += getStructureWidth("rockPile")*2;
            } else { break; }
            if (startX + getStructureWidth("deadTree2")*2 < endX) {
                plantFoliage(startX, "deadTree2");
                startX += getStructureWidth("deadTree2")*2;
            } else { break; }
            if (startX + getStructureWidth("bigWall")*1.5 < endX) {
                plantFoliage(startX, "bigWall");
                startX += getStructureWidth("bigWall")*1.5;
            } else { break; }
        }
        drawBackgroundDesigns(getBackgroundMap(), "beanBack", midX-(getStructureWidth("beanBack")*2), 4);

    }




    private static int baseTile =524;

    private static int baseArray [] = {
            1,2,3,4,
            128+1,128+2,128+3,128+4,
            baseTile,baseTile+1,baseTile+2,baseTile+3,baseTile+4,
            baseTile+128,baseTile+128+1,baseTile+128+2,baseTile+128+3,baseTile+128+4,
            baseTile+256,baseTile+256+1,baseTile+256+2,baseTile+256+3,baseTile+256+4,
            baseTile+385,baseTile+385+1,
            baseTile+513,baseTile+513+1,baseTile+513+2,
            baseTile+642

    };

    private static int baseTile2 = 1297;

    private static int desertArray [] = {
            1041,1042,1043,1044,
            128+1041,128+1042,128+1043,128+1044,
            baseTile2,baseTile2+1,baseTile2+2,baseTile2+3,baseTile2+4,
            baseTile2+128,baseTile2+128+1,baseTile2+128+2,baseTile2+128+3,baseTile2+128+4,
            baseTile2+256,baseTile2+256+1,baseTile2+256+2,baseTile2+256+3,baseTile2+256+4,
            baseTile2+385,baseTile2+385+1,
            baseTile2+513,baseTile2+513+1,baseTile2+513+2,
            baseTile2+642
    };

    private static int baseTile3 = 1302;

    private static int jungleArray [] = {
            1046,1047,1048,1049,
            128+1046,128+1047,128+1048,128+1049,
            baseTile3,baseTile3+1,baseTile3+2,baseTile3+3,baseTile3+4,
            baseTile3+128,baseTile3+128+1,baseTile3+128+2,baseTile3+128+3,baseTile3+128+4,
            baseTile3+256,baseTile3+256+1,baseTile3+256+2,baseTile3+256+3,baseTile3+256+4,
            baseTile3+385,baseTile3+385+1,
            baseTile3+513,baseTile3+513+1,baseTile3+513+2,
            baseTile3+642
    };

    private static int baseTile4 = 1307;

    private static int arcticArray [] = {
            1051,1052,1053,1054,
            128+1051,128+1052,128+1053,128+1054,
            baseTile4,baseTile4+1,baseTile4+2,baseTile4+3,baseTile4+4,
            baseTile4+128,baseTile4+128+1,baseTile4+128+2,baseTile4+128+3,baseTile4+128+4,
            baseTile4+256,baseTile4+256+1,baseTile4+256+2,baseTile4+256+3,baseTile4+256+4,
            baseTile4+385,baseTile4+385+1,
            baseTile4+513,baseTile4+513+1,baseTile4+513+2,
            baseTile4+642
    };

        //  Decorate cave walls with rounded corners
        private static void decorateCave (short[][] map, int mudChar, int offset) {
            int caveDec = 0;
            for (int x = 1; x < IMap.WIDTH - 2; x++) {
                for (int y = 1; y < IMap.HEIGHT - 2; y++) {
                    if (map[x][y] == 0) {
                        if (map[x][y - 1] == mudChar) {
                            caveDec = caveDec | 1;
                        }
                        if (map[x][y + 1] == mudChar) {
                            caveDec = caveDec | 2;
                        }
                        if (map[x - 1][y] == mudChar) {
                            caveDec = caveDec | 4;
                        }
                        if (map[x + 1][y] == mudChar) {
                            caveDec = caveDec | 8;
                        }
                        if (caveDec != 0) {
                            switch (caveDec) {
                                case 1: {               // Top
                                    map[x][y] = (short) (525 + offset + (x & 1) + ((x & 2) / 2));
                                }
                                break;
                                case 2: {               // Bottom
                                    map[x][y] = (short) (781 + offset + (x & 1) + ((x & 2) / 2));
                                }
                                break;
                                case 4: {               // Left
                                    map[x][y] = (short) (652 + offset + (y % 2));
                                }
                                break;
                                case 8: {               // Right
                                    map[x][y] = (short) (655 + offset + (y % 2));
                                }
                                break;
                                case 5: {               // Upper left corner
                                    map[x][y] = (short) (524 + offset);
                                }
                                break;
                                case 6: {               // Lower left corner
                                    map[x][y] = (short) (780 + offset);
                                }
                                break;
                                case 9: {               // Upper right corner
                                    map[x][y] = (short) (528 + offset);
                                }
                                break;
                                case 10: {              // Lower right corner
                                    map[x][y] = (short) (784 + offset);
                                }
                                break;
                                case 15: {              // All 4 sides
                                    map[x][y] = (short) (654 + offset);
                                }
                                break;
                                case 3: {              // Top and bottom
                                    map[x][y] = (short) (909 + offset);
                                }
                                break;
                                case 12: {              // Top and bottom
                                    map[x][y] = (short) (1038 + offset);
                                }
                                break;
                                case 13: {              // Bottom missing
                                    map[x][y] = (short) (910 + offset);
                                }
                                break;
                                case 14: {              // Top missing
                                    map[x][y] = (short) (1166 + offset);
                                }
                                break;
                                case 11: {              // Left missing
                                    map[x][y] = (short) (1039 + offset);
                                }
                                break;
                                case 7: {              // Right missing
                                    map[x][y] = (short) (1037 + offset);
                                }
                                break;
                            }
                            caveDec = 0;
                        }
                    }
                }
            }
        }

        private static void createSeam (short[][] map, int char1, int char2, int goldYStart, int goldRange, int goldCount) {
            goldYStart += IMap.PLATFORM_HEIGHT + IMap.TERRAIN_HEIGHT;
            short [][]resourceMap=getResourceMap();
            while (goldCount != 0) {
                int xStart = RangedRandom.generate(20, IMap.WIDTH - 30);
                int yStart = goldYStart + RangedRandom.generate(0, goldRange);
                for (int y = yStart; y < yStart + 9; y++) {
                    for (int x = xStart; x < xStart + 9; x++) {
                        if (map[x][y] == 1) {
                            int chance = RangedRandom.generate(0, 10);
                            if (chance < 4) {
                                resourceMap[x][y] = (short) char1;
                            } else if (chance < 6) {
                                resourceMap[x][y] = (short) char2;
                            }
                        }
                    }
                    xStart++;
                }
                goldCount--;
            }
        }

        private static void drawBackgroundDesigns(short[][] map, String name, int px, int pcount) {
            int py = 0;
            for (int pi = 0; pi < pcount; pi++) {
                while (py < (IMap.TERRAIN_HEIGHT + IMap.PLATFORM_HEIGHT)) {
                    TmxRenderer.drawStructureIntoMapSkip0(name, map, px, py);
                    py += getStructureHeight(name);
                }
                px += getStructureWidth(name); // RangedRandom.generate(2, 8) + getStructureWidth(name);
                py = ((getStructureHeight(name)) / 2) * (pi + 1);
            }
        }

        private static void plantFoliage(int x, String key){
//        int map [][]= getForegroundMap();
//        int y = IMap.PLATFORM_HEIGHT;
//        while (map[x+(getStructureWidth("rock1")/2)][y]==0){
//            y++;
//        }
//        y-=(getStructureHeight("rock1"));
//        TmxRenderer.drawStructureBehindMapSkip0("rock1", getFoliageMap(),x, y+5);
//    }


            short[][] map = getForegroundMap();
            int y = IMap.PLATFORM_HEIGHT;
            while (map[x+(getStructureWidth(key)/2)][y]==0){
                y++;
            }
            y-=(int) ((getStructureHeight(key)/3)*2);
            TmxRenderer.drawStructureBehindMapSkip0(key, getFoliageMap(),x, y);
        }


    }
