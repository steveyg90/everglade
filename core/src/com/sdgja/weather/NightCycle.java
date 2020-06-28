package com.sdgja.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.sdgja.actor.Player;
import com.sdgja.filesystem.IFileName;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.map.IMap;
import com.sdgja.utils.DisposableMemoryManager;
import com.sdgja.utils.GlobalVar;

import java.io.IOException;

import static com.sdgja.intohell.IntoHellGame.*;

public class NightCycle implements Disposable {

    private final float radius = 1920f/3.2f;  //// Sun/moon radius

    private static int day = 0;
    private static int timeOfDay = 0;
    private static float alpha = 0;
    public  boolean cycleDone = false;

//    private SpriteBatch batch;
    private Texture texture;
    private Sprite spriteImage;
    private Sprite[] moonImages;
    private Vector2 position;
    private float angle = 0;
    private boolean goDark = true;

    private Texture[] moonTextures;

//    private static final double RADIUS = (Gdx.graphics.getWidth()/2)+80;

    private Texture skyScape;
    private Sprite skyScapeSprite=null;
    private static Sprite daySky = null;

    public static int getDay() { return day; }
    public static int getTime() { return timeOfDay; }
    public static float getAlpha() { return alpha; }

    public static void setSkyTexture(Texture texture) {
        daySky.setTexture(texture);
    }

    public NightCycle() {
//        this.batch = new SpriteBatch();
        cycleDone = false;
        day = 0;
        DisposableMemoryManager.addDisposable(this);
        daySky = new Sprite(IntoHellGame.getSkyTexture());  // get current sky texture
    }

    public void addImage(String filename, Vector2 position, float startAngle, boolean goDark) throws IOException {
        this.texture = new Texture(IFileName.ROOT + "/weathergfx/" + filename);
        this.spriteImage = new Sprite(this.texture);
        //this.position[imageIndex] = new Vector2(Gdx.graphics.getWidth() - spriteImage[imageIndex].getWidth()- 560, Gdx.graphics.getHeight()/2 - 450);
        this.position = new Vector2(position.x,position.y);
//        this.offsetPosition = new Vector2(this.position.x, this.position.y);
        this.angle = startAngle;
        this.goDark = goDark;
        if(!this.goDark) {
            this.skyScape = new Texture(IFileName.ROOT + "/weathergfx/starscape.png");
            skyScapeSprite = new Sprite(this.skyScape);
            moonTextures = new Texture[8];
            moonImages = new Sprite[8];
            for(int i=0;i<moonTextures.length;i++) {
                moonTextures[i] = new Texture(IFileName.ROOT + "/weathergfx/" + "moon" + i + ".png");
                moonImages[i] = new Sprite(moonTextures[i]);
            }
        }
    }


    public void performCycle(boolean sun) {

        if (IMap.DEBUG_MODE) {
            if (Gdx.input.isKeyPressed(Input.Keys.HOME)) {
                this.angle += 0.01f;   // 0.001f
            }
        }

        if (sun) {
// DAY TIME!
            this.angle += 0.001f;
            if (this.angle > 6) {
                this.angle -= 6;
            }
            if (this.angle < 4) {
                if (IntoHellGame.cloudBackground[IntoHellGame.biome].getCloudStatus()) {
                    alpha = 1f;
                    if (this.angle < (2.0f)) {                      // Fade Sky ON
                        alpha = this.angle/ 2f;
                    }
                    if (this.angle > (2.0f)) {                     // Fade Sky OFF
                        alpha = (this.angle-2.0f);
                        if (alpha>1f) { alpha=1f; }
                        alpha=1-alpha;
                    }
// Draw the Sky
//                    weatherBatch.setColor(alpha, alpha, alpha, 1);
                    daySky.setColor(alpha, alpha, alpha, 1);
                    weatherBatch.draw(daySky,-(1920/2),-(1080/2));
// Draw the sun
                    position.x = (1920f/2f - 1920f/15f + (float) Math.cos(this.angle) * (radius * 2))-(1920/2);
                    position.y = (1080f/10f + (float) Math.sin(this.angle) * radius)-(1080/3f);
                    spriteImage.setPosition(position.x, position.y);
                    spriteImage.rotate(.125f);        // Rotate Sun
                    spriteImage.setColor(1f, 1f, 1, 1);
                    weatherBatch.setColor(1f, 1f, 1, 1);
                    spriteImage.draw(weatherBatch);
                    weatherBatch.setColor(alpha, alpha, alpha, 1);
                }
            }
            if(angle > 2f && angle <3f) {
                nightCol = 1.0f-(angle-2);
            }
            if (angle < 1) {
                nightCol = angle;
            }
            timeOfDay=((int)(this.angle*2));
        } else {
// NIGHT TIME!
            this.angle += 0.001f;
            if (this.angle > 6) {
                day++;
                this.angle -= 6;
            }
            if (this.angle < 3) {
                if (IntoHellGame.cloudBackground[IntoHellGame.biome].getCloudStatus()) {
                    alpha = 0.1f;
                    if (this.angle < (0.7f)) {                      // Fade Stars ON
                        alpha = this.angle * 2f;
                        if (alpha>1f) { alpha=1f; }
                        alpha *=0.1f;
                    }
                    if (this.angle > (2.3f)) {                      // Fade Stars OFF
                        alpha = (3.0f - this.angle) * 2f;
                        if (alpha>1f) { alpha=1f; }
                        alpha *=0.1f;
                    }
// Draw the Stars
                    skyScapeSprite.setOrigin(skyScapeSprite.getWidth() / 2, skyScapeSprite.getHeight() / 2);
//                    weatherBatch.setColor(1,1,1,alpha); //alpha, alpha, alpha, 1);
                    skyScapeSprite.setColor(1,1,1,alpha*10f); //alpha, alpha, alpha, 1);
//                    skyScapeSprite.setPosition(-(skyScapeSprite.getWidth() / 2)-(GlobalVar.coreX/2), -(skyScapeSprite.getHeight() / 2)-(GlobalVar.coreY/2));
                    skyScapeSprite.setPosition(-(skyScapeSprite.getWidth()/2), -(skyScapeSprite.getHeight()/2));
                    skyScapeSprite.rotate(.02f);        // Rotate stars
//                    skyScapeSprite.setScale(2f);
                    skyScapeSprite.draw(weatherBatch);                 // Draw stars
// Draw the Moon
                    position.x = (1920f/2f - 1920f/15f + (float) Math.cos(this.angle) * (radius * 2))-(1920/2);
                    position.y = (1080f/10f + (float) Math.sin(this.angle) * radius)-(1080/3);
                    moonImages[day & 7].setPosition(position.x, position.y);
                    weatherBatch.setColor(1, 1, 1, 1);
                    moonImages[day & 7].setColor(1, 1, 1, 1);
                    moonImages[day & 7].draw(weatherBatch);
                    weatherBatch.setColor(alpha, alpha, alpha, 1);
                }
            }
            timeOfDay=12+((int)(this.angle*2));
        }
        timeOfDay-=15;                          // Adjust for 6am sunrise
        if (timeOfDay<0) {timeOfDay+=12;}
        timeOfDay*=2;
    }


    @Override
    public void dispose() {
        IntoHellGame.nightCol2 = 1;
        nightCol = 1;
        if(goDark)
            angle = 1;
        else
//            angle = Math.PI + .5f;

            cycleDone = false;
        day = 0;

        this.texture.dispose();
        if(this.skyScape!=null) {
            this.skyScape.dispose();
        }
        if(moonTextures!=null) {
            for (int i = 0; i < moonTextures.length; i++) {
                if (moonTextures[i] != null) {
                    moonTextures[i].dispose();
                }
            }
        }

    }
}

