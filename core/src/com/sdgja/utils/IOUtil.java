
//  ██╗ ██████╗ ██╗   ██╗████████╗██╗██╗
//  ██║██╔═══██╗██║   ██║╚══██╔══╝██║██║
//  ██║██║   ██║██║   ██║   ██║   ██║██║
//  ██║██║   ██║██║   ██║   ██║   ██║██║
//  ██║╚██████╔╝╚██████╔╝   ██║   ██║███████╗
//  ╚═╝ ╚═════╝  ╚═════╝    ╚═╝   ╚═╝╚══════╝
//

package com.sdgja.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sdgja.map.IMap;

import java.util.Vector;

import static com.sdgja.intohell.IntoHellGame.camera;

public class IOUtil {

    public static Vector2 toScreenSpace(Vector2 position) {
        Vector2 v = new Vector2(0, 0);
        v.x = position.x / IMap.TILEWIDTH;
        v.y = position.y / IMap.TILEHEIGHT;
        return v;
    }

    public static Vector2 getMousePos() {
        Vector2 mousePos = new Vector2();
        mousePos.set(Gdx.input.getX(), Gdx.input.getY());
        return mousePos;
    }



    public static TVector<Integer> getMousePosInMapSpace(int camX, int camY, float zoom) {
        float xDiv = (float) Gdx.graphics.getWidth()/GlobalVar.coreX;      // Scale to viewport dimensions
        float yDiv = (float) Gdx.graphics.getHeight()/GlobalVar.coreY;
        float x = (Gdx.input.getX()/xDiv) * zoom;
        float y = (Gdx.input.getY()/yDiv) * zoom;
        int mouseX = (int)x;
        int mouseY = (int)y;
        mouseX/=IMap.TILEWIDTH;
        mouseX+=camX;
        mouseY/=IMap.TILEHEIGHT;
        mouseY+=camY;
        return new TVector<>(mouseX, mouseY);
    }

    public static TVector<Integer> getMousePosInScreenSpace(){
        Vector2 mousepos = getMousePos();
        mousepos = toScreenSpace(mousepos);
        TVector<Integer> mp = new TVector<>((int)Math.ceil(mousepos.x),(int)Math.ceil(mousepos.y));
        return mp;
    }
}
