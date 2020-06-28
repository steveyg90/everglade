package com.sdgja.entity.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.brashmonkey.spriter.*;
import com.sdgja.entity.structure.IFunc;
import com.sdgja.filesystem.IFileName;
import com.sdgja.map.IMap;
import com.sdgja.map.MapGeneration;
import com.sdgja.utils.FrameCount;
import com.sdgja.utils.RangedRandom;
import com.brashmonkey.spriter.gdx.Drawer;
import com.brashmonkey.spriter.gdx.Loader;

import javax.swing.text.View;

import static com.sdgja.intohell.IntoHellGame.getPlayer;
import static com.sdgja.utils.XMLReader.getTileId;

public class Pengo extends AbstractDrawableSprite implements Disposable {
    public static Drawer drawer;
    public static Loader loader;
    public static Data data;
    public static SCMLReader reader;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// All player movements are keyed from this speed.
final static float monsterBaseSpeed = 5.5f/2.5f;

    @Override
    public void dispose() {
//        loader.dispose();
    }

    // Possible variations for monster status
    enum e_Actions {
        e_Idle,
        e_JumpLeft,
        e_JumpRight,
        e_Fall,
        e_FallLeft,
        e_FallRight,
        e_WalkLeft,
        e_WalkRight
    }
    // Variable for fall multiplier
    private static float monsterFallSpeed = 1;
    private final float monsterFallStartSpeed = 0.15f;
    private boolean facingRight=true;
    private final float stepUpValue = 15; // how much to move the player up when he hits a single block
    int[] monsterJumpTable = {15, 12, 11, 10, 9, 8, 7, 7, 6, 6, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};

    private int monsterJumpIndex;

    // Current player status
    private Pengo.e_Actions monsterStatus = Pengo.e_Actions.e_Idle;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    IFunc play = () -> {
        switch (monsterStatus) {
            case e_Idle:
                viewableSpriteEntity.getSprite().setAnimation("idle");
                checkForFall();
                if (monsterStatus == Pengo.e_Actions.e_Idle) {
// Chance of breaking out of idle animation
                    if (RangedRandom.generate(1,150)<5) {
// Chance of changing direction
                        if (RangedRandom.generate(1,100)<25) { facingRight = !facingRight; }
                        if (!facingRight) {
                            if ((FrameCount.getFrameCount()&3)==1) {
                                monsterStatus = e_Actions.e_JumpLeft;
                                monsterJumpIndex = 0;
                            } else {
                                monsterStatus = Pengo.e_Actions.e_WalkLeft;
                            }
                        } else {
                            if ((FrameCount.getFrameCount()&3)==1) {
                                monsterStatus = e_Actions.e_JumpRight;
                                monsterJumpIndex = 0;
                            } else {
                                monsterStatus = Pengo.e_Actions.e_WalkRight;
                            }
                        }
                    }
                }
                break;
            case e_Fall:
                viewableSpriteEntity.getSprite().setAnimation("jump");
                if (monsterFallSpeed < monsterBaseSpeed/2) {
                    monsterFallSpeed += 0.50;
                }
                if (monsterCheckFloor()) {
                    monsterStatus = Pengo.e_Actions.e_Idle;
                } else {
                    worldPosition.y += monsterBaseSpeed + monsterFallSpeed;
                }
                break;
            case e_FallLeft:
                facingRight=false;
                viewableSpriteEntity.getSprite().setAnimation("jump");
                if (viewableSpriteEntity.getSprite().flippedX()==-1) {      // -1 or 1 !
                    viewableSpriteEntity.getSprite().flipX();
                }
                if (monsterFallSpeed < monsterBaseSpeed/2) {
                    monsterFallSpeed += 0.50;
                }
                if (monsterCheckFloor()) {
                    monsterStatus = Pengo.e_Actions.e_Idle;
                } else {
                    moveMonsterLeft();
                    worldPosition.y += monsterBaseSpeed + monsterFallSpeed;
                }
                break;
            case e_FallRight:
                facingRight=true;
                viewableSpriteEntity.getSprite().setAnimation("jump");
                if (monsterFallSpeed < monsterBaseSpeed/2) {
                    monsterFallSpeed += 0.50;
                }
                if (monsterCheckFloor()) {
                    monsterStatus = Pengo.e_Actions.e_Idle;
                } else {
                    moveMonsterRight();
                    worldPosition.y += monsterBaseSpeed + monsterFallSpeed;
                }
                break;
            case e_WalkLeft:
                facingRight=false;
                viewableSpriteEntity.getSprite().setAnimation("walk");
                    if (!moveMonsterLeft()){
                        if ((FrameCount.getFrameCount()&3)!=0) {
                            monsterStatus = e_Actions.e_WalkRight;
                            if (RangedRandom.generate(1,100)<15) {
                                monsterStatus = Pengo.e_Actions.e_WalkLeft;
                            }
                            } else {
                            monsterStatus = e_Actions.e_JumpRight;
                            monsterJumpIndex = 0;
                        }
                    }
                checkForFall();
                if (monsterStatus == Pengo.e_Actions.e_Fall){ 		// Changed from walk into fall
                    if (!checkTiles(worldPosition.x-monsterBaseSpeed, worldPosition.y + 26)) {
                        worldPosition.y +=13;
                        monsterStatus = Pengo.e_Actions.e_WalkLeft;
                    }
                }
                break;
            case e_WalkRight:
                facingRight=true;
                viewableSpriteEntity.getSprite().setAnimation("walk");
                if (!moveMonsterRight()){
                    if ((FrameCount.getFrameCount()&3)!=0) {
                        monsterStatus = e_Actions.e_WalkLeft;
                        if (RangedRandom.generate(1,100)<15) {
                            monsterStatus = Pengo.e_Actions.e_WalkRight;
                        }
                    } else {
                        monsterStatus = e_Actions.e_JumpLeft;
                        monsterJumpIndex = 0;
                    }
                }
                checkForFall();
                if (monsterStatus == Pengo.e_Actions.e_Fall){ 		// Changed from walk into fall
                    if (!checkTiles(worldPosition.x+monsterBaseSpeed, worldPosition.y + 26)) {
                        worldPosition.y +=13;
                        monsterStatus = Pengo.e_Actions.e_WalkRight;
                        monsterJumpIndex = 0;
                    }
                }
                break;
            case e_JumpLeft:
                facingRight=false;
                viewableSpriteEntity.getSprite().setAnimation("jump");
                if (monsterJumpIndex == monsterJumpTable.length || (monsterBumpedHead())) {
                    startFallLeft();
                } else {
                    moveMonsterLeft();
                    worldPosition.y -= (monsterJumpTable[monsterJumpIndex]);
                    monsterJumpIndex++;
                }
                break;
            case e_JumpRight:
                facingRight=true;
                viewableSpriteEntity.getSprite().setAnimation("jump");
                if (monsterJumpIndex == monsterJumpTable.length || (monsterBumpedHead())) {
                    startFallRight();
                } else {
                    moveMonsterRight();
                worldPosition.y -= (monsterJumpTable[monsterJumpIndex]);
                    monsterJumpIndex++;
                }
                break;
            default:
            break;
        }
//        if(collisionWithPlayer(100)) {
//            this.setActive(false);
//        }
        if(collisionWithPlayer(100)) {
            if (worldPosition.x > getPlayer().getPlayerPosition().x){
                monsterStatus = Pengo.e_Actions.e_JumpRight;
                monsterJumpIndex = 0;

            } else {
                monsterStatus = Pengo.e_Actions.e_JumpLeft;
                monsterJumpIndex = 0;

            }
        }

        flipMonster();
        clampMonsterToMap();
        update();  // call base class method
        draw();   // call base class method
    };

    private void flipMonster() {
        if ((viewableSpriteEntity.getSprite().flippedX() == -1 && !facingRight) || (viewableSpriteEntity.getSprite().flippedX() != -1 && facingRight)) {      // -1 or 1 !
            viewableSpriteEntity.getSprite().flipX();
        }
    }

    private void checkForFall() {
        if (!monsterCheckFloor()) {
            startFall();
        }
    }
    private boolean monsterBumpedHead() { return (!checkTiles(worldPosition.x, worldPosition.y - 13)); }

    private void startFallLeft() {
        startFall();
        monsterStatus = Pengo.e_Actions.e_FallLeft;
    }

    private void startFallRight() {
        startFall();
        monsterStatus = Pengo.e_Actions.e_FallRight;
    }

    private void startFall() {
        monsterFallSpeed = monsterFallStartSpeed;
        monsterStatus = Pengo.e_Actions.e_Fall;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean monsterCheckFloor() { return (!checkTiles(worldPosition.x, worldPosition.y + 13)); }

    private boolean checkTiles(float fx,float fy) {
        int x=(int) ((fx-7)* IMap.ZOOM) / 8;
        int y=(int) ((fy+(13*2))*IMap.ZOOM) / 8;
        short [][] map = MapGeneration.getForegroundMap();
        if (getTileId (map [x][y]) == 0 && getTileId (map [x+1][y]) == 0 && getTileId (map [x][y-1]) == 0 && getTileId (map [x+1][y-1]) == 0 && getTileId (map [x][y-2]) == 0 && getTileId (map [x+1][y-2]) == 0) {
            return (true);
        }
        return (false);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean moveMonsterLeft() {
        if (checkTiles(worldPosition.x-monsterBaseSpeed,worldPosition.y)) {
            worldPosition.x -= monsterBaseSpeed;
            return true;
        } else {
            if (checkTiles(worldPosition.x-monsterBaseSpeed,worldPosition.y-(stepUpValue*1.5f))) {
                worldPosition.x -= monsterBaseSpeed;
                worldPosition.y -= stepUpValue;
                return true;
            } else {
                return false;
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean moveMonsterRight() {
        if (checkTiles(worldPosition.x+monsterBaseSpeed,worldPosition.y)) {
            worldPosition.x += monsterBaseSpeed;
            return true;
        } else {
            if (checkTiles(worldPosition.x+monsterBaseSpeed,worldPosition.y-(stepUpValue*1.5f))) {
                worldPosition.x += monsterBaseSpeed;
                worldPosition.y -= stepUpValue;
                return true;
            } else {
                return false;
            }
        }
    }
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void clampMonsterToMap() {
        if (worldPosition.x < 32f) 							{ worldPosition.x = 32f; }
        if ((worldPosition.x*IMap.ZOOM) > (IMap.WIDTH-3)*8) 	{ worldPosition.x = ((IMap.WIDTH-3)*8)/IMap.ZOOM; }
        if (worldPosition.y < 64f) 							{ worldPosition.y = 64f; }
        if ((worldPosition.y*IMap.ZOOM) > (IMap.HEIGHT-4)*8) { worldPosition.y = ((IMap.HEIGHT-4)*8)/IMap.ZOOM; }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Pengo(Vector2 worldPosition, Vector2 dimension, boolean active, boolean basePlayMethod, boolean flipped) {
        super(worldPosition, dimension, active, basePlayMethod,flipped);
        initialise(IFileName.FNPengo,"idle", worldPosition);
        loadSpriteData(IFileName.FNPengo, "idle", drawer, loader, data, reader,
                (d,l,dt,r)->{drawer=r;loader=dt;data=l;reader=d;});
        addAction(play);
    }

    @Override
    public void draw() {
//        ViewableSpriteEntity.batch.begin();
//        viewableSpriteEntity.batch.begin();
        drawer.draw(viewableSpriteEntity.spriter);
        if(this.flipped) {   // faceRight should turn player back other way
            faceLeft();
        }
//        ViewableSpriteEntity.batch.end();
//        viewableSpriteEntity.batch.end();


    }
}
