package com.sdgja.camera.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sdgja.filesystem.IFileName;
import com.sdgja.utils.DisposableMemoryManager;


public class HudCamera {//extends OrthographicCamera {

    private BitmapFont font;
    private SpriteBatch hudBatch;
    private Texture hudTexture;

    public HudCamera(float width, float height) {
        //super(width, height);

        hudBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        hudTexture = new Texture(IFileName.ROOT + "/hud/hud.png");

        DisposableMemoryManager.addDisposable(font);
        DisposableMemoryManager.addDisposable(hudTexture);
        DisposableMemoryManager.addDisposable(hudBatch);
    }

    public void drawHud() {

      //  update();

        hudBatch.begin();
            hudBatch.setColor(0.4f, 0.4f, 0.4f, .7f);
            hudBatch.draw(hudTexture,0,Gdx.graphics.getHeight()/2 + 70,Gdx.graphics.getWidth()/2,300);
//            hudBatch.draw(hudTexture,640,Gdx.graphics.getHeight()/2 + 70,Gdx.graphics.getWidth()/2,300);
        hudBatch.end();
    }

    public void drawText(int x, int y, String str ) {
        hudBatch.begin();
          font.draw(hudBatch, str, x, Gdx.graphics.getHeight()/2 + (400 - y));
        hudBatch.end();
    }

}
