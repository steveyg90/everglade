package com.sdgja.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera extends OrthographicCamera implements IFullyPannable, IZoomable{


    public Camera(float width, float height, float zoom) {
        super(width,height);
        setToOrtho(true, width, height);
        this.zoom = zoom;
    }

    @Override
    public void zoomIn() {

    }

    @Override
    public void zoomOut() {

    }

    @Override
    public void left(float px) {
    //    position.x-=px;
     //   this.update();
    }

    @Override
    public void right(float px) {
     //   position.x+=px;
     //   this.update();
    }

    @Override
    public void up(float px) {
     //   position.y-=px;
      //  this.update();
    }

    @Override
    public void down(float px) {
     //   position.y+=px;
      //  this.update();
    }
}
