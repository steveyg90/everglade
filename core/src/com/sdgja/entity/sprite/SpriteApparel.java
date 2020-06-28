package com.sdgja.entity.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.Timeline;
import com.brashmonkey.spriter.gdx.Drawer;
import com.brashmonkey.spriter.gdx.Loader;
import com.sdgja.actor.Actor;
import com.sdgja.entity.structure.IFunc;
import com.sdgja.filesystem.IFileName;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.map.IMap;
import com.sdgja.map.MapGeneration;

import static com.sdgja.utils.XMLReader.getTileId;

public class SpriteApparel extends AbstractDrawableSprite implements Disposable {

    public static Drawer drawer;
    public static Loader loader;
    public static Data data;
    public static SCMLReader reader;

    IFunc play = () -> {

        if (!appCheckFloor()){
            worldPosition.y+=5;
        }

        if(collisionWithPlayer(100)) {
//            System.out.println("Collided");
            SpriteManager.getInstance().removeSpriteEntity(this);
        }
        update();  // call base class method
        draw();   // call our override method
    };

    private void loadSpriteData(String scml, String animFrame) {
        FileHandle scmlHandle = null;

        if(reader==null) {
            scmlHandle = Gdx.files.internal(scml);
            reader = new SCMLReader(scmlHandle.read());
            data = reader.getData();
            loader = new Loader(data);
            loader.load(scmlHandle.file());
            data = reader.getData();
            drawer = new Drawer(loader, viewableSpriteEntity.batch, null);
        }

        viewableSpriteEntity.spriter = new Player(data.getEntity(0));
        viewableSpriteEntity.spriter.setAnimation(animFrame);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean appCheckFloor() { return (!checkTiles(worldPosition.x, worldPosition.y + 13)); }

    private boolean checkTiles(float fx,float fy) {
        int x=(int) ((fx-7)* IMap.ZOOM) / 8;
        int y=(int) ((fy+(13*2))*IMap.ZOOM) / 8;
        short [][] map = MapGeneration.getForegroundMap();
        if (getTileId (map [x][y]) == 0 && getTileId (map [x+1][y]) == 0 && getTileId (map [x][y-1]) == 0 && getTileId (map [x+1][y-1]) == 0 && getTileId (map [x][y-2]) == 0 && getTileId (map [x+1][y-2]) == 0) {
            return (true);
        }
        return (false);
    }

    // basePlayMethod if true will use AbstractDrawableSprite play method, set to false to have own version and add the action yourself
    // See - SpriteMonster for example of this
    public SpriteApparel(Vector2 worldPosition, Vector2 dimension, boolean active, boolean basePlayMethod,boolean flipped) {
        super(worldPosition, dimension, active, basePlayMethod, flipped);
        initialise(IFileName.FNIndianaHat,"idle", worldPosition);
     //   loadSpriteData(IFileName.FNIndianaHat, "idle");
        loadSpriteData(IFileName.FNIndianaHat, "idle", drawer, loader, data, reader,
                (d,l,dt,r)->{drawer=r;loader=dt;data=l;reader=d;});
        addAction(play);
    }

    @Override
   // public void draw() {
   //         viewableSpriteEntity.draw();

   // }
    public void draw() {
//        viewableSpriteEntity.batch.begin();
        drawer.draw(viewableSpriteEntity.spriter);
//        viewableSpriteEntity.batch.end();

    }


    @Override
    public void dispose() {

        //loader.dispose();
    }
}
