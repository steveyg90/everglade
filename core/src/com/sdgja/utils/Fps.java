
//  ███████╗██████╗ ███████╗
//  ██╔════╝██╔══██╗██╔════╝
//  █████╗  ██████╔╝███████╗
//  ██╔══╝  ██╔═══╝ ╚════██║
//  ██║     ██║     ███████║
//  ╚═╝     ╚═╝     ╚══════╝
//

package com.sdgja.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import static com.sdgja.intohell.IntoHellGame.camera;

final public class Fps implements Disposable {

    private static BitmapFont fpsFont;
    private static SpriteBatch batch;

    static {
        fpsFont = new BitmapFont();
        batch = new SpriteBatch();
       // DisposableMemoryManager.addDisposable(fpsFont);  // make sure to add font resource to our memory manager
    }
    public static void drawFps() {
        Color c = fpsFont.getColor();
        int fps = Gdx.graphics.getFramesPerSecond();
        fpsFont.setColor(0, 1, 0, 1);
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        batch.begin();
        fpsFont.draw(batch, "FPS: " + fps, camera.viewportWidth-70, camera.viewportHeight-60);
        batch.end();
        fpsFont.setColor(c); // white
    }

    @Override
    public void dispose() {
      //  fpsFont.dispose();
      //  fpsFont = null;

    }

}
