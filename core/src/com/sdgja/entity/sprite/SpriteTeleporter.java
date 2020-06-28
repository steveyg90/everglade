package com.sdgja.entity.sprite;

import com.badlogic.gdx.math.Vector2;
import com.sdgja.entity.structure.IFunc;

public class SpriteTeleporter extends AbstractDrawableSprite{

    IFunc play = () -> { // TODO
    };

    public SpriteTeleporter(Vector2 worldPosition, Vector2 dimension, boolean active, boolean basePlayMethod,boolean flipped) {
        super(worldPosition, dimension, active, basePlayMethod,flipped);
        addAction(play);
    }
}
