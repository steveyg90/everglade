
//  ██████╗  █████╗  ██████╗██╗  ██╗ ██████╗ ██████╗  ██████╗ ██╗   ██╗███╗   ██╗██████╗ ███████╗██╗    ██╗ █████╗ ██████╗ ██████╗ ███████╗██████╗
//  ██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝ ██╔══██╗██╔═══██╗██║   ██║████╗  ██║██╔══██╗██╔════╝██║    ██║██╔══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
//  ██████╔╝███████║██║     █████╔╝ ██║  ███╗██████╔╝██║   ██║██║   ██║██╔██╗ ██║██║  ██║███████╗██║ █╗ ██║███████║██████╔╝██████╔╝█████╗  ██████╔╝
//  ██╔══██╗██╔══██║██║     ██╔═██╗ ██║   ██║██╔══██╗██║   ██║██║   ██║██║╚██╗██║██║  ██║╚════██║██║███╗██║██╔══██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
//  ██████╔╝██║  ██║╚██████╗██║  ██╗╚██████╔╝██║  ██║╚██████╔╝╚██████╔╝██║ ╚████║██████╔╝███████║╚███╔███╔╝██║  ██║██║     ██║     ███████╗██║  ██║
//  ╚═════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚═════╝ ╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
//

package com.sdgja.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.map.IMap;
import com.sdgja.utils.DisposableMemoryManager;

public class BackgroundSwapper {
    private SpriteBatch batch;
    private Texture texture;
    private int x,y;
    private int width, height;
    private float rgb;

    public BackgroundSwapper(int width, int height, int x, int y,String file) {
        this.width=width;
        this.height=height;
        this.x = x;
        this.y = y;
        batch = new SpriteBatch();
        texture = new Texture(file);
        this.rgb = 1.0f;
        DisposableMemoryManager.addDisposable(texture);
    }

    public void updateImage(String file) {
        texture = new Texture(file);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getRGB() { return this.rgb;}

    public void drawBackground(float cameraY) {
        batch.setShader(null);
        batch.begin();
        int topLimit = IMap.BOTTOMFADELIMIT;
        int bottomLimit = IMap.BOTTOMFADELIMIT+256;
        float newRgb = rgb;
        if(cameraY < bottomLimit) {
            newRgb = ((cameraY - topLimit) / (bottomLimit - topLimit));  //divisor;
        }
        rgb=IntoHellGame.myLerp(newRgb,rgb,.00001f);
        batch.setColor(rgb, rgb, rgb, 1);
        batch.draw(texture,x,y,width,height);
        batch.end();
    }

}
