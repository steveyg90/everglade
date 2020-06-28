package com.sdgja.entity.sprite;

import com.badlogic.gdx.math.Vector2;
import com.sdgja.entity.structure.IFunc;

public class SpriteEntity  implements Comparable<SpriteEntity>  {
    protected Vector2 worldPosition;
    protected Vector2 dimension;
    protected boolean flipped;
    private boolean active;
    private IFunc action;

    @Override
    public int compareTo(SpriteEntity spriteEntity) {
        return (int) (worldPosition.y - spriteEntity.worldPosition.y);
    }

    public void setActive(boolean active) { this.active = active;}

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        SpriteEntity s = (SpriteEntity)o;
        return worldPosition.y == s.worldPosition.y;
    }

    public SpriteEntity(Vector2 worldPosition, Vector2 dimension, boolean active, boolean flipped) {
        this.worldPosition=worldPosition;
        this.dimension=dimension;
        this.active=active;
        this.flipped=flipped;
    }

    public boolean isActive() {
        return this.active;
    }

    public void addAction(IFunc action) {
        this.action = action;
    }

    public void start() {
        action.play();
    }

}
