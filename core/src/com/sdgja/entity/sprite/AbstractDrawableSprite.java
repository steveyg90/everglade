package com.sdgja.entity.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.gdx.Drawer;
import com.brashmonkey.spriter.gdx.Loader;
import com.sdgja.actor.Actor;
import com.sdgja.entity.structure.IFunc;
import com.sdgja.intohell.IntoHellGame;

public abstract class AbstractDrawableSprite extends SpriteEntity implements IRenderable {

    IFunc play = () -> {
        update();  // call base class method
        draw();   // call base class method
    };

    protected ViewableSpriteEntity viewableSpriteEntity=null;  // we only want one of these

    protected void loadSpriteData(String scml, String animFrame, Drawer drawer, Loader loader, Data data, SCMLReader reader, IFunctional fp) {
        FileHandle scmlHandle = null;

        if(reader==null) {
            scmlHandle = Gdx.files.internal(scml);
            reader = new SCMLReader(scmlHandle.read());
            data = reader.getData();
            loader = new Loader(data);
            loader.load(scmlHandle.file());
            data = reader.getData();
            drawer = new Drawer(loader, viewableSpriteEntity.batch, null);
            fp.setSpriterObjects(reader,data,loader,drawer);
        }

        viewableSpriteEntity.spriter = new Player(data.getEntity(0));
        viewableSpriteEntity.spriter.setAnimation(animFrame);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Checks if we have collided with the player - returns true if we have
    public boolean collisionWithPlayer(int offset) {
        Actor player = IntoHellGame.getPlayer();
        Vector2 playerPos = new Vector2(player.getPlayerPosition().x,player.getPlayerPosition().y);
        if( Math.abs(player.getPlayerPosition().x - this.worldPosition.x) > offset || Math.abs(player.getPlayerPosition().y - this.worldPosition.y) > offset)
            return false;
        // Create the bounding boxes for player and this object
        Rectangle bbox = new Rectangle(playerPos.x,playerPos.y,player.getPlayer().getBoudingRectangle(null).size.width,player.getPlayer().getBoudingRectangle(null).size.height);
        Rectangle hbox = new Rectangle(worldPosition.x,worldPosition.y,dimension.x,dimension.y);

        return bbox.overlaps(hbox);
    }

    public AbstractDrawableSprite(Vector2 worldPosition, Vector2 dimension, boolean active, boolean playMethod, boolean flipped) {
        super(worldPosition, dimension, active, flipped);
        if(viewableSpriteEntity==null)
            viewableSpriteEntity = new ViewableSpriteEntity();
        if(playMethod) {
            addAction(play);
        }
    }

    public void initialise(String scmlFile, String animFrame, Vector2 worldPosition) {
        viewableSpriteEntity.initialise(scmlFile,animFrame,worldPosition);
    }

    @Override
    public void draw() {
        if(viewableSpriteEntity.getSprite()!=null) {
            viewableSpriteEntity.draw();
        }
    }

    public void update() {
        worldPosition = viewableSpriteEntity.update(worldPosition);
    }

    public void faceLeft() {
        if(viewableSpriteEntity.getSprite().flippedX()==1)
            viewableSpriteEntity.getSprite().flip(true,false);
    }
    public void faceRight() {
        if(viewableSpriteEntity.getSprite().flippedX()==-1)
            viewableSpriteEntity.getSprite().flip(true,false);
    }
}

@FunctionalInterface
interface IFunctional {
    void setSpriterObjects(SCMLReader sr, Data data, Loader loader, Drawer drawer);
}
