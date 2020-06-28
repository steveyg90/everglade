//  ███╗   ███╗ █████╗ ██████╗  ██████╗ ███████╗███╗   ██╗███████╗██████╗  █████╗ ████████╗██╗ ██████╗ ███╗   ██╗
//  ████╗ ████║██╔══██╗██╔══██╗██╔════╝ ██╔════╝████╗  ██║██╔════╝██╔══██╗██╔══██╗╚══██╔══╝██║██╔═══██╗████╗  ██║
//  ██╔████╔██║███████║██████╔╝██║  ███╗█████╗  ██╔██╗ ██║█████╗  ██████╔╝███████║   ██║   ██║██║   ██║██╔██╗ ██║
//  ██║╚██╔╝██║██╔══██║██╔═══╝ ██║   ██║██╔══╝  ██║╚██╗██║██╔══╝  ██╔══██╗██╔══██║   ██║   ██║██║   ██║██║╚██╗██║
//  ██║ ╚═╝ ██║██║  ██║██║     ╚██████╔╝███████╗██║ ╚████║███████╗██║  ██║██║  ██║   ██║   ██║╚██████╔╝██║ ╚████║
//  ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝      ╚═════╝ ╚══════╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
//

package com.sdgja.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sdgja.filesystem.IFileName;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.raycast.RayCast;
import com.sdgja.utils.FrameCount;
import com.sdgja.utils.TVector;

import static com.sdgja.Ui.MouseInterface.isBuildingValid;
import static com.sdgja.intohell.IntoHellGame.camera;
import static com.sdgja.weather.NightCycle.getAlpha;

public final class MapGeneration implements IMap {

    private static short[][] map;
    private static short[][] backgroundMap;
    private static short[][] buildingMap;
    private static short[][] buildingPlaceMap;
    private static short[][] foliageMap;
    private static short[][] resourceMap;

    private static Texture tileSheet;
    private static TextureRegion[][] tileRegions;

    private static MapGeneration _instance;

    private static int tileXOffset, tileYOffset;
    private static TVector<Integer> screenRes = new TVector<>(0,0);

    private static SpriteBatch spritebatch = null;

    public static MapGeneration getInstance() {
        if(_instance==null) {

            _instance = new MapGeneration();
            map = new short[WIDTH][HEIGHT];
            backgroundMap = new short[WIDTH][HEIGHT];
            buildingMap = new short[WIDTH][HEIGHT];
            foliageMap = new short[WIDTH][HEIGHT];
            resourceMap = new short[WIDTH][HEIGHT];
            buildingPlaceMap = new short[WIDTH][HEIGHT];
            screenRes.setX(Gdx.graphics.getWidth());
            screenRes.setY(Gdx.graphics.getHeight());
            float resX = screenRes.getX()/2 * IMap.ZOOM;
            tileXOffset = (int)resX / IMap.TILEWIDTH;
            float resY = screenRes.getY()/2 * IMap.ZOOM;
            tileYOffset = (int)resY / IMap.TILEHEIGHT + 1;
            init();
        }
        return _instance;
    }

    private MapGeneration() {
        spritebatch = new SpriteBatch();
    }

    public static short[][]getForegroundMap() { return map; }
    public static short[][]getBackgroundMap() { return backgroundMap; }
    public static short[][]getBuildingMap() { return buildingMap; }
    public static short[][]getBuildingPlaceMap() { return buildingPlaceMap; }
    public static short[][]getFoliageMap() { return foliageMap; }
    public static short[][]getResourceMap() { return resourceMap; }

    private static void init() {
        //CaveGeneration.clearMap(map, WIDTH, HEIGHT);
        CaveGeneration.initialiseMap(map, 0.46f);

        for(int i=0;i<SMOOTH+1;i++)
            map = CaveGeneration.doSimulationStep(map, 1, 4);

//        CaveGeneration.createTerrain(map);

        CaveGeneration.createHorizon(map,backgroundMap);

        try {
            tileSheet = new Texture(IFileName.FNTileGraphics);
            tileRegions = TextureRegion.split(tileSheet, TILEWIDTH, TILEHEIGHT);

            flipAllTiles();

        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
//        for(int y=0; y<IMap.HEIGHT;y++) {
//            buildingPlaceMap[100][y] = 1;
//        }
    }

    // As libGDX uses coordinates of 0,0 at bottom left of screen and we are using camera to be 0,0 at top left
    // we need to also flip the tile regions and anything else that use TextureRegion would need to be flipped
    private static void flipAllTiles() {
        int y=tileSheet.getTextureData().getHeight() / TILEHEIGHT;
        int x=tileSheet.getTextureData().getWidth() / TILEWIDTH;
        for(int sx=0; sx<x;sx++) {
            for(int sy=0;sy<y;sy++) {
                tileRegions[sx][sy].flip(false,true);
            }
        }
    }

    public static void renderBuildingMap(SpriteBatch batch, int sx, int sy) {
        int ey=sy+(tileYOffset<<1)+1;
        int ex=sx+(tileXOffset<<1)+1;
        for(int x=sx-1; x<+ex+1;x++) {
            for(int y=sy; y<ey; y++) {
                if(buildingMap[x][y]!=0) {
                    drawTile(batch, buildingMap[x][y], x, y);
                }
            }
        }
    }

    public static void renderFoliageMap(SpriteBatch batch, int sx, int sy) {
        spritebatch.setProjectionMatrix(camera.combined);
        int ey=sy+(tileYOffset<<1)+1;
        int ex=sx+(tileXOffset<<1)+1;
        spritebatch.setShader(null);
        float alpha=getAlpha()*0.5f+(0.25f);
        spritebatch.setColor(alpha,alpha,alpha,1);
        spritebatch.begin();
        for(int x=sx-1; x<+ex+1;x++) {
            for(int y=sy; y<ey; y++) {
                if(foliageMap[x][y]!=0) {
                    drawTile(spritebatch, foliageMap[x][y], x, y);
                }
            }
        }
        spritebatch.end();
        spritebatch.setShader(null);
    }

    public static void renderBuildingPlaceMap(SpriteBatch batch, int sx, int sy) {
        int ey=sy+(tileYOffset<<1)+1;
        int ex=sx+(tileXOffset<<1)+1;
        for(int x=sx-1; x<+ex+1;x++) {
            for(int y=sy; y<ey; y++) {
                if(buildingPlaceMap[x][y]!=0) {
                    if (isBuildingValid()) {
                        drawTileBuildingPlace(batch, buildingPlaceMap[x][y], x, y);
                    } else {
                        drawTile(batch, buildingPlaceMap[x][y], x, y);
                    }
                }
            }
        }
    }

    public static void renderBackMap(SpriteBatch batch, int sx, int sy) {
        int ey=sy+(tileYOffset<<1)+1;
        int ex=sx+(tileXOffset<<1)+1;
        for(int x=sx-1; x<+ex+1;x++) {
            for(int y=sy; y<ey; y++) {
                if(backgroundMap[x][y]!=0) {
                    drawTile(batch, backgroundMap[x][y], x, y);
                }
            }
        }
    }

    public static void renderResourceMap(SpriteBatch batch, int sx, int sy) {
        int ey=sy+(tileYOffset<<1)+1;
        int ex=sx+(tileXOffset<<1)+1;
        for(int x=sx-1; x<+ex+1;x++) {
            for(int y=sy; y<ey; y++) {
                if(resourceMap[x][y]!=0) {
                    drawTile(batch, resourceMap[x][y], x, y);
                }
            }
        }
    }

//    public static void renderResourceMap(SpriteBatch batch, int sx, int sy, boolean raycast, float rgb) {
//        float[][] lightMap = RayCast.getInstance().getLightMap();
//        int ey=sy+(tileYOffset<<1)+1;;
//        int ex=sx+(tileXOffset<<1)+1;
//        int lightXPos = lightMap.length/3;
//        map=getResourceMap();
//        for(int x=sx; x<+ex;x++) {
//            int lightYPos = lightMap[0].length/3;
//            for(int y=sy; y<ey; y++) {
//                if(map[x][y]!=0) {
//                    if(raycast && rgb!=0) {
//                        batch.setColor(1, 1, 0.95f, 1); //rgb2); //1.0f);
//                        drawTile(batch, map[x][y], x, y);
//                        float col = 1 *((lightMap[lightXPos - 1][lightYPos] + (lightMap[lightXPos][lightYPos]*2) + lightMap[lightXPos + 1][lightYPos] + lightMap[lightXPos][lightYPos - 1] + lightMap[lightXPos][lightYPos + 1] + lightMap[lightXPos-1][lightYPos - 1] + lightMap[lightXPos+1][lightYPos -1] + lightMap[lightXPos-1][lightYPos + 1] + lightMap[lightXPos+1][lightYPos + 1]) / 10);
//                        batch.setColor(col, col, col, rgb/1.075f); //rgb2); //1.0f);
//                    }
//                    drawTile(batch, map[x][y], x, y);
//                }
//                lightYPos++;
//            }
//            lightXPos++;
//        }
//    }


    public static void renderMap(SpriteBatch batch, int sx, int sy, boolean raycast, float rgb) {
        float[][] lightMap = RayCast.getInstance().getLightMap();
        int ey=sy+(tileYOffset<<1)+1;;
        int ex=sx+(tileXOffset<<1)+1;
        int lightXPos = lightMap.length/3;
        for(int x=sx-1; x<+ex+1;x++) {
            int lightYPos = lightMap[0].length/3;
            for(int y=sy; y<ey; y++) {
                if(map[x][y]!=0) {
                    if(raycast && rgb!=0) {
                        batch.setColor(1, 1, 0.95f, 1); //rgb2); //1.0f);
                        drawTile(batch, map[x][y], x, y);
                        float col = 1 *((lightMap[lightXPos - 1][lightYPos] + (lightMap[lightXPos][lightYPos]*2) + lightMap[lightXPos + 1][lightYPos] + lightMap[lightXPos][lightYPos - 1] + lightMap[lightXPos][lightYPos + 1] + lightMap[lightXPos-1][lightYPos - 1] + lightMap[lightXPos+1][lightYPos -1] + lightMap[lightXPos-1][lightYPos + 1] + lightMap[lightXPos+1][lightYPos + 1]) / 10);
                        batch.setColor(col, col, col, rgb/1.075f); //rgb2); //1.0f);
                    }
                    drawTile(batch, map[x][y], x, y);
               }
                lightYPos++;
            }
            lightXPos++;
        }
    }



//    public static void renderMapOffset(SpriteBatch batch, int sx, int sy, boolean raycast, float rgb) {
//        float[][] lightMap = RayCast.getInstance().getLightMap();
//        int ey=sy+(tileYOffset<<1)+1;;
//        int ex=sx+(tileXOffset<<1)+1;
//        int lightXPos = lightMap.length/3;
//        for(int x=sx; x<+ex;x++) {
//            int lightYPos = lightMap[0].length/3;
//            for(int y=sy; y<ey; y++) {
//                if(map[x][y]!=0) {
//                    if(raycast && rgb!=0) {
//                        batch.setColor(.5f, .5f, .5f, 1f); //rgb2); //1.0f);
//                        drawTileOffset(batch, map[x][y], x, y);
//                        float col = 1 *((lightMap[lightXPos - 1][lightYPos] + (lightMap[lightXPos][lightYPos]*2) + lightMap[lightXPos + 1][lightYPos] + lightMap[lightXPos][lightYPos - 1] + lightMap[lightXPos][lightYPos + 1] + lightMap[lightXPos-1][lightYPos - 1] + lightMap[lightXPos+1][lightYPos -1] + lightMap[lightXPos-1][lightYPos + 1] + lightMap[lightXPos+1][lightYPos + 1]) / 10);
//                        batch.setColor(col*.25f, col*.25f, col*.25f, rgb/1.575f); //rgb2); //1.0f);
//                    }
//                    drawTileOffset(batch, map[x][y], x, y);
//                }
//                lightYPos++;
//            }
//            lightXPos++;
//        }
//    }


    private static void  drawTileBuildingPlace(SpriteBatch batch, int tile, int x, int y) {
        int yTilePos = tile / 128;
        int xTilePos = tile % 128;
        drawTBP(batch, yTilePos, xTilePos,tileRegions,x,y);
    }

    private static float [] pulseArray = {1.05f,1.1f,1.25f,1.4f,1.25f,1.1f,1.05f,1.0f};

    private static void  drawTBP(SpriteBatch batch, int tx, int ty, TextureRegion[][] t, int x, int y) {
        x-=tileXOffset;
        y-=tileYOffset;
        int index = (((FrameCount.getFrameCount())>>2)&7);
        float scale = pulseArray[index]*1.1f;
        batch.draw(t[tx][ty], x*TILEWIDTH,y*TILEHEIGHT,0,0,TILEWIDTH,TILEHEIGHT,scale,scale,0);
    }
    // New method added SDG - draw tile from tile number
    private static void  drawTile(SpriteBatch batch, int tile, int x, int y) {
        int yTilePos = tile / 128;
        int xTilePos = tile % 128;
        draw(batch, yTilePos, xTilePos,tileRegions,x,y);
    }

    private static void  draw(SpriteBatch batch, int tx, int ty, TextureRegion[][] t, int x, int y) {
        x-=tileXOffset;
        y-=tileYOffset;
        batch.draw(t[tx][ty], x*TILEWIDTH, y*TILEHEIGHT, TILEWIDTH, TILEHEIGHT );
    }

//    // New method added SDG - draw tile from tile number
//    private static void  drawTileOffset(SpriteBatch batch, int tile, int x, int y) {
//        int yTilePos = tile / 128;
//        int xTilePos = tile % 128;
//        drawOffset(batch, yTilePos, xTilePos,tileRegions,x,y);
//    }
//    private static void  drawOffset(SpriteBatch batch, int tx, int ty, TextureRegion[][] t, int x, int y) {
//        x-=tileXOffset;
//        y-=tileYOffset;
//        batch.draw(t[tx][ty], (x*TILEWIDTH), (y*TILEHEIGHT)+3, TILEWIDTH, TILEHEIGHT );
//        batch.draw(t[tx][ty], (x*TILEWIDTH)-3, (y*TILEHEIGHT)+2, TILEWIDTH, TILEHEIGHT );
//        batch.draw(t[tx][ty], (x*TILEWIDTH)+3, (y*TILEHEIGHT)+2, TILEWIDTH, TILEHEIGHT );
//
//        batch.draw(t[tx][ty], (x*TILEWIDTH), (y*TILEHEIGHT)-2, TILEWIDTH, TILEHEIGHT );
//        batch.draw(t[tx][ty], (x*TILEWIDTH)-2, (y*TILEHEIGHT)-1, TILEWIDTH, TILEHEIGHT );
//        batch.draw(t[tx][ty], (x*TILEWIDTH)+2, (y*TILEHEIGHT)-1, TILEWIDTH, TILEHEIGHT );
//    }



    public static void dispose() {  // our own dispose
        _instance = null;
        //tileSheet.dispose();

    }
}