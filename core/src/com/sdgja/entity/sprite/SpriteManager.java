package com.sdgja.entity.sprite;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.brashmonkey.spriter.Rectangle;
import com.sdgja.actor.Actor;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.utils.DisposableMemoryManager;
import com.sdgja.utils.GlobalVar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteManager implements Disposable {


    public List<AbstractDrawableSprite> spriteEntities;

    public List<AbstractDrawableSprite> getSpriteEntities() {
        return spriteEntities;
    }

    private SpriteManager() {
        spriteEntities = new ArrayList<>();
        DisposableMemoryManager.addDisposable(this);
    }

    public void removeAll() {
        for(AbstractDrawableSprite sprite : spriteEntities) {
            sprite.viewableSpriteEntity.dispose();
        }
        spriteEntities.clear();
    }

    public void removeSpriteEntity(AbstractDrawableSprite entity) {
        spriteEntities.remove(entity);
    }

    public void addSpriteEntity(AbstractDrawableSprite sprite) {
        spriteEntities.add(sprite);
    }

    public void playAll() {
        Actor player = IntoHellGame.getPlayer();
        Vector2 playerPos = new Vector2(player.getPlayerPosition().x,player.getPlayerPosition().y);
        int debugCounter =0;
        float playX = player.getPlayerPosition().x;
        float playY = player.getPlayerPosition().y;
        for (int i = 0; i < spriteEntities.size(); i++) {
            if( Math.abs(playX - spriteEntities.get(i).worldPosition.x) > GlobalVar.coreX*2 || Math.abs(playY - spriteEntities.get(i).worldPosition.y) > GlobalVar.coreY*2){
                debugCounter++;
            } else {
                spriteEntities.get(i).start();
            }
        }

        IntoHellGame.totalSprites = spriteEntities.size();
        IntoHellGame.culled = debugCounter;
//        IntoHellGame.getWinManager().addInfo("Console","Total " + spriteEntities.size() + " Culled " + debugCounter);


// see if any sprites need to be removed
//        debugCounter =0;
        for (int i = 0; i < spriteEntities.size(); i++) {
            if (!spriteEntities.get(i).isActive()) {
                spriteEntities.remove(spriteEntities.get(i));
//                debugCounter++;
            }
        }
//        System.out.println("Removed " + debugCounter);

    }

//    public Vector2 testEntity(){
//        Vector2 res = new Vector2();
//        res.x = spriteEntities.get(5).worldPosition.x;
//        res.y = spriteEntities.get(5).worldPosition.y;
//        return res;
//    }


    public void sort() {
        Collections.sort(spriteEntities);
    }

    @Override
    public void dispose() {
        removeAll();
        SpriteManager.instance = null;  // VIP so garbage collector cleans up
    }

    private static SpriteManager instance = null;
    public static SpriteManager getInstance() {
        if(instance==null) {
            instance = new SpriteManager();
        }
        return instance;
    }

}
