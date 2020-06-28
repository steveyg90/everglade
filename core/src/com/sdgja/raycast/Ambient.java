//   █████╗ ███╗   ███╗██████╗ ██╗███████╗███╗   ██╗████████╗
//  ██╔══██╗████╗ ████║██╔══██╗██║██╔════╝████╗  ██║╚══██╔══╝
//  ███████║██╔████╔██║██████╔╝██║█████╗  ██╔██╗ ██║   ██║
//  ██╔══██║██║╚██╔╝██║██╔══██╗██║██╔══╝  ██║╚██╗██║   ██║
//  ██║  ██║██║ ╚═╝ ██║██████╔╝██║███████╗██║ ╚████║   ██║
//  ╚═╝  ╚═╝╚═╝     ╚═╝╚═════╝ ╚═╝╚══════╝╚═╝  ╚═══╝   ╚═╝
// Ambient lighting effects

package com.sdgja.raycast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
//import com.badlogic.gdx.math.Matrix4;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.utils.GlobalVar;
import com.sdgja.utils.RangedRandom;
//import com.sdgja.actor.Player;

import static com.sdgja.intohell.IntoHellGame.*;

public class Ambient {

    public static void clearAmbientBuffer() {
        FrameBuffer lightBuffer = getLightBuffer();
        lightBuffer.begin();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.98f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lightBuffer.end();
    }

    public static void drawPlayerAmbient(float x, float y){
        FrameBuffer lightBuffer = getLightBuffer();
        SpriteBatch lightbatch = getLightbatch();
        Texture lightTexture = getLightTexture();
        lightBuffer.begin();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
        Gdx.gl.glEnable(GL20.GL_BLEND);

//        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,0.9f);
        lightbatch.begin();
        lightbatch.setColor(1f,1f,1f,.6f);
        float lightX = (x) - (camera.position.x * 2);
        float lightY = (y) - (camera.position.y * 2);
        float lightW = 128 * 6;
        float lightH = 128 * 5;
        lightbatch.draw(lightTexture,lightX -(lightW /2),(GlobalVar.coreY-(lightY))-(lightH /2),lightW,lightH,0,0,128,128,false,true);
        lightbatch.end();
        lightBuffer.end();

    }

    public static void drawAmbientLight(float x, float y){

        FrameBuffer lightBuffer = getLightBuffer();
        SpriteBatch lightbatch = getLightbatch();
        Texture lightTexture = getLightTexture();
        lightBuffer.begin();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        lightbatch.begin();
        lightbatch.setColor(1f, .3f, .7f, 1f);
        float flightX = (x) - (IntoHellGame.camera.position.x* 2);
        float flightY = (y) - (IntoHellGame.camera.position.y* 2);
        float flightW = ((128-48)+(RangedRandom.generate(0,48))) * 7;
        float flightH = ((128-32)+(RangedRandom.generate(0,32))) * 7;
        lightbatch.draw(lightTexture, flightX - (flightW / 2), (GlobalVar.coreY - (flightY)) - (flightH / 2), flightW, flightH, 0, 0, 128, 128, false, true);
        lightbatch.end();
        lightBuffer.end();

    }

}
