package com.sdgja.hud.health;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.sdgja.filesystem.IFileName;

import static com.sdgja.intohell.IntoHellGame.gamePort;
import static com.sdgja.intohell.IntoHellGame.getCameraDiffernceX;

public class HealthPower {
//    static String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
//            + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
//            + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
//            + "uniform mat4 u_projTrans;\n" //
//            + "varying vec4 v_color;\n" //
//            + "varying vec2 v_texCoords;\n" //
//            + "\n" //
//            + "void main()\n" //
//            + "{\n" //
//            + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
//            + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
//            + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
//            + "}\n";
//    static String fragmentShader = "#ifdef GL_ES\n" //
//            + "#define LOWP lowp\n" //
//            + "precision mediump float;\n" //
//            + "#else\n" //
//            + "#define LOWP \n" //
//            + "#endif\n" //
//            + "varying LOWP vec4 v_color;\n" //
//            + "varying vec2 v_texCoords;\n" //
//            + "uniform sampler2D u_texture;\n" //
//            + "void main()\n"//
//            + "{\n" //
//            + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords).a;\n" //
//            + "}";

    private static SpriteBatch HUDbatch;
    private static Texture textureHealth;
    private static Texture textureHotBar;
    private static Texture texturePower;
    private static Texture textureCompass;
    private static Texture textureNeedle;
    private static Texture textureJournal;
    private static Texture textureConversation;
    private static Sprite spriteHealth;
    private static Sprite spritePower;
    private static Sprite spriteHotBar;
    private static Sprite spriteCompass;
    private static Sprite spriteNeedle;
    private static Sprite spriteJournal;
    private static Sprite spriteConversation;
//    private static ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);

    static final float paddingX = 32f;
    static final float paddingY = 16f;

    public static void initHUD(){
        HUDbatch = new SpriteBatch();
        textureHealth = new Texture(IFileName.FNHealth);
        texturePower  = new Texture(IFileName.FNPower);
        textureHotBar = new Texture(IFileName.FNHotbar);
        textureCompass = new Texture(IFileName.FNCompass);
        textureNeedle = new Texture(IFileName.FNNeedle);
        textureJournal = new Texture(IFileName.FNJournal);
        textureConversation = new Texture(IFileName.FNConversation);
        spriteHealth = new Sprite(textureHealth);
        spritePower = new Sprite(texturePower);
        spriteHotBar = new Sprite(textureHotBar);
        spriteCompass = new Sprite(textureCompass);
        spriteNeedle = new Sprite(textureNeedle);
        spriteJournal = new Sprite(textureJournal);
        spriteConversation = new Sprite(textureConversation);
    }

    public static void drawHUD(){
        HUDbatch.begin();
        spriteHealth.setPosition(paddingX,(gamePort.getBottomGutterHeight()+gamePort.getScreenHeight()) - textureHealth.getHeight()-paddingY);
        spritePower.setPosition((gamePort.getLeftGutterWidth()+gamePort.getScreenWidth()) - 242-paddingX,(gamePort.getBottomGutterHeight()+gamePort.getScreenHeight()) - texturePower.getHeight()-paddingY);
        spriteHotBar.setPosition(gamePort.getLeftGutterWidth()+(gamePort.getScreenWidth()/2) - (464/2),(gamePort.getBottomGutterHeight()+gamePort.getScreenHeight()) - textureHotBar.getHeight()-paddingY);
        spriteCompass.setPosition(paddingX,(gamePort.getBottomGutterHeight()+paddingY));
        spriteNeedle.setPosition(paddingX,(gamePort.getBottomGutterHeight()+paddingY));
        spriteJournal.setPosition((gamePort.getLeftGutterWidth()+gamePort.getScreenWidth())-paddingX- textureJournal.getWidth(),(gamePort.getBottomGutterHeight()+paddingY));
        spriteConversation.setPosition(gamePort.getLeftGutterWidth()+(gamePort.getScreenWidth()/2) - (textureConversation.getWidth()/2),(gamePort.getBottomGutterHeight()+paddingY));

        spriteHealth.draw(HUDbatch);

        spritePower.draw(HUDbatch);

        spriteHotBar.draw(HUDbatch);

        spriteJournal.draw(HUDbatch);

        spriteConversation.draw(HUDbatch);

        spriteCompass.draw(HUDbatch);
        spriteNeedle.draw(HUDbatch);
        spriteNeedle.rotate(getCameraDiffernceX());
        HUDbatch.end();
    }

}

