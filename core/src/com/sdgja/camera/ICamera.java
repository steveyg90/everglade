package com.sdgja.camera;

public interface ICamera extends IZoomable{
}

interface IZoomable {
    void zoomIn();
    void zoomOut();
}
interface IHorizontalPannable {
    void left(float px);
    void right(float px);
}
interface IVerticalPannable  {
    void up(float px);
    void down(float px);
}
interface IFullyPannable extends IHorizontalPannable, IVerticalPannable{

}
interface IFullyPannableAndZoomable extends IFullyPannable, IZoomable {

}
