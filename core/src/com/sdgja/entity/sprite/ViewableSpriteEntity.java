package com.sdgja.entity.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.brashmonkey.spriter.Player;
import com.sdgja.map.IMap;
import com.sdgja.utils.DisposableMemoryManager;
import com.sdgja.utils.GlobalVar;

import java.util.ArrayList;
import java.util.List;
import static com.sdgja.intohell.IntoHellGame.camera;

public final class ViewableSpriteEntity implements Disposable {

    public static SpriteBatch batch=null;  // only want one of these
    public Player spriter;

    private Vector2 startPosition;

    private static List<String> files = null;

    public Player getSprite() { return this.spriter; }

    public Vector2 getStartPosition() { return startPosition;}

    public void initialise(String scml, String animFrame, Vector2 startPosition) {
        if(batch==null) {
            batch = new SpriteBatch();
            DisposableMemoryManager.addDisposable(this);  // only want to add once
        }
        if(files == null) {
            files = new ArrayList<>();
        }

        this.startPosition = startPosition;

    }

    public Vector2 update(Vector2 pos) {
        float X = ((pos.x) - (camera.position.x)/ IMap.ZOOM);
        float Y= (GlobalVar.coreY-((pos.y-6) - (camera.position.y)/ IMap.ZOOM))-30;
        spriter.setPosition(X,Y); //(((position.x* IMap.ZOOM) - (camera.position.x*IMap.ZOOM)), (720-((position.y* IMap.ZOOM) - (camera.position.y*IMap.ZOOM))));
        spriter.update();
        return this.startPosition;
    }

    public void draw() {
    }

    @Override
    public void dispose() {
        if(files!=null) {
            files.clear();
        }
  /*      batch = null;
        files = null;*/
    }
}
