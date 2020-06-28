package com.sdgja.entity.structure;

import com.sdgja.map.MapGeneration;
import com.sdgja.tmx.TmxRenderer;
import com.sdgja.utils.FrameCount;

import static com.sdgja.map.MapGeneration.getBuildingMap;


public class Thumper extends Entity {

    enum act {
        dig,
        retract,
        bounce,
        waitDig,
        waitRetract,
        grow
    }

// Constants
private final int MAXBOUNCE = 6;        // How many tiles to bounce the drill when it hits a solid

// Variables
    private act action;             // Current action of Thumper
    private int depth;              // Current maximum depth of drill
    private int offsetX;            // Offset into Thumper graphic for drill position within tile map of entity
    private int offsetY;
    private int bounceHeight;       // How much to bounce when drill hits an object
    private int maxBounceHeight;    // How much to bounce when drill hits an object
    private boolean bounceUp;       // True = bouncing upwards
    private int waitDelay;          // How long to wait before next dig
    private int power;              // Current power level,1 unit per cycle is used
    private int currentGrowth;      // Generic timer
    private int growthTimer;      // Generic timer

    IFunc play = () -> {

        structureMap = getStructureMap("thumper");  // structure map - need to use same name as when we created thumper

        short[][] map = getBuildingMap();
        switch (action){
            case dig:
                animateHead(1);
                eraseDrill();
                if (!checkNextDrill()){
                    depth++;
                } else {
                    bounceHeight=depth;
                    if (bounceHeight> MAXBOUNCE){
                        bounceHeight= MAXBOUNCE;
                    }
                    maxBounceHeight=bounceHeight;
                    bounceUp=true;

                    if (depth<2){
                        destroyMap();
                        waitDelay = 120;
                        action = act.waitRetract;

                    } else{
                        action=act.bounce;

                    }
                }
                drawDrill();
                break;

            case bounce:
                eraseDrill();
                byte b = (byte) (FrameCount.getFrameCount() & 3);
                if (b == 0) {
                    if (bounceUp == true) {
                        animateHead(3);
                        depth--;
                        bounceHeight--;
                        if (bounceHeight == 1) {
                            bounceUp = false;
                        }
                    } else {
                        depth++;
                        bounceHeight++;
                        if (bounceHeight == maxBounceHeight) {
                            maxBounceHeight--;
                            bounceHeight = maxBounceHeight;
                            bounceUp = true;
                            if (maxBounceHeight <= 1) {
                                destroyMap();
                                waitDelay = 60;
                                action = act.waitRetract;
                            }
                        }
                    }
                }
                drawDrill();
                break;

            case retract:
                animateHead(15);
                eraseDrill();
                b = (byte) (FrameCount.getFrameCount()&7);
                if (b == 0) {
                    b = (byte) (FrameCount.getFrameCount() & 1);
                    if (b == 0) {
                        if (depth == 1) {
                            waitDelay = 120;
                            action = act.waitDig;
                        } else {
                            depth--;
                    }
                }
                }
                drawDrill();
                break;
            case waitRetract:
                eraseDrill();
                waitDelay--;
                if (waitDelay==0) {
                    action = act.retract;
                }
                drawDrill();
                break;

            case waitDig:
                eraseDrill();
                    if (waitDelay==0 &&power!=0) {
                        power--;
                        action = act.dig;
                    } else {
                        waitDelay--;
                    }
                drawDrill();
                break;
            case grow:
                growthTimer++;
                if (growthTimer==growthSpeed*.5){
                    growthTimer=0;
                    currentGrowth++;
                    if (currentGrowth==TmxRenderer.getStructureHeight("thumper") ){
                        action=act.dig;
                        // This will remove an entity - in this case, this thumper
//                        EntityManager.removeEntity(this);

                    }
                    TmxRenderer.growStructureIntoMap("thumper", getBuildingMap(), x, y,currentGrowth);

                }
                break;

            default:
            }
    };

    private void drawDrill(){
        short[][] map = getBuildingMap();
        map [offsetX+x]  [offsetY+y+depth-1]=9;
        map [offsetX+x+1][offsetY+y+depth-1]=10;
        map [offsetX+x]  [offsetY+y+depth]=9+128;
        map [offsetX+x+1][offsetY+y+depth]=10+128;
    }

    private void animateHead(int mask){
        short[][] map = getBuildingMap();
        byte b = (byte) (FrameCount.getFrameCount() & mask);
        if (b == 0) {
            if (map[x + 4][y] == 15) {
                map[x + 4][y] = 399;
                map[x + 4 + 1][y] = 399 + 1;
                map[x + 3][y+1] = 15;
                map[x + 5 + 1][y+1] = 15 + 1;
                map[x + 3][y+2] = 143;
                map[x + 3][y+3] = 143;
                map[x + 6][y+2] = 144;
                map[x + 6][y+3] = 144;
            } else {
                map[x + 4][y] = 15;
                map[x + 4 + 1][y] = 15 + 1;
                map[x + 3][y+1] = 399;
                map[x + 5 + 1][y+1] = 399 + 1;
                map[x + 3][y+2] = 273;
                map[x + 3][y+3] = 273;
                map[x + 6][y+2] = 274;
                map[x + 6][y+3] = 274;
            }
        }
    }

    private static void dressTile(int x, int y, short[][] map) {
        int caveDec = 0;

        if (map[x][y - 1] != 0 && map[x][y-1]<524) {
            caveDec = caveDec | 1;
        }
        if (map[x][y + 1] != 0 && map[x][y+1]<524) {
            caveDec = caveDec | 2;
        }
        if (map[x - 1][y] != 0 && map[x-1][y]<524) {
            caveDec = caveDec | 4;
        }
        if (map[x + 1][y] != 0 && map[x+1][y]<524) {
            caveDec = caveDec | 8;
        }

        int offset = 0;
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
        }
    }

    private void eraseDrill(){
        short[][] map = getBuildingMap();
        map [offsetX+x][offsetY+y+depth]=0;
        map [offsetX+x+1][offsetY+y+depth]=0;
        map = MapGeneration.getForegroundMap();
        map [offsetX+x][offsetY+y+depth]=0;
        map [offsetX+x+1][offsetY+y+depth]=0;
        dressTile(offsetX+x,offsetY+y+depth,MapGeneration.getForegroundMap());
        dressTile(offsetX+x+1,offsetY+y+depth,MapGeneration.getForegroundMap());
    }

    private void destroyMap(){
        short[][] map = MapGeneration.getForegroundMap();
        map [offsetX+x][y + offsetY + depth + 1]=0;
        map [offsetX+x+1][offsetY+y+depth+1]=0;
        map [offsetX+x][y + offsetY + depth]=0;
        map [offsetX+x+1][offsetY+y+depth]=0;
        dressTile(offsetX+x,offsetY+y+depth,MapGeneration.getForegroundMap());
        dressTile(offsetX+x+1,offsetY+y+depth,MapGeneration.getForegroundMap());
        dressTile(offsetX+x,offsetY+y+depth+1,MapGeneration.getForegroundMap());
        dressTile(offsetX+x+1,offsetY+y+depth+1,MapGeneration.getForegroundMap());
    }
    private static int[] digTiles = {774,1,2,3,4,128+1,128+2,128+3,128+4};


// Returns true if blocked.
    private boolean checkNextDrill(){
// Check for torch or other building in our path
        short[][] bMap = getBuildingMap();
        if ((bMap [offsetX+x][offsetY+y+depth+2]!=0 || bMap [offsetX+x+1][offsetY+y+depth+2]!=0) && depth>4){
            return true;
        }

// Check for dig tiles
        short[][] map = MapGeneration.getForegroundMap();
        for (int i=0;i<digTiles.length;i++)
        {
            if ((map [offsetX+x][offsetY+y+depth+1])==digTiles[i] || (map [offsetX+x+1][offsetY+y+depth+1])==digTiles[i])
            {
                return true;
            }
        }
        return false;
    }


    public Thumper(int x, int y, int startPower, short[][] map) {
        super("thumper",x, y,map);
        TmxRenderer.allocateStructureIntoMap("thumper", getBuildingMap(), x, y);
        action = act.grow;
        currentGrowth =0;
        growthTimer=0;
        depth=1;
        offsetX=4;
        offsetY=2;
        power=startPower;
        addActionAndSelf();
    }

    public void addActionAndSelf() {
        addAction(play);
        EntityManager.addEntity(this);
    }
}

