
//  ██╗    ██╗██╗███╗   ██╗██████╗  ██████╗ ██╗    ██╗███╗   ███╗ █████╗ ███╗   ██╗ █████╗  ██████╗ ███████╗██████╗
//  ██║    ██║██║████╗  ██║██╔══██╗██╔═══██╗██║    ██║████╗ ████║██╔══██╗████╗  ██║██╔══██╗██╔════╝ ██╔════╝██╔══██╗
//  ██║ █╗ ██║██║██╔██╗ ██║██║  ██║██║   ██║██║ █╗ ██║██╔████╔██║███████║██╔██╗ ██║███████║██║  ███╗█████╗  ██████╔╝
//  ██║███╗██║██║██║╚██╗██║██║  ██║██║   ██║██║███╗██║██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║██║   ██║██╔══╝  ██╔══██╗
//  ╚███╔███╔╝██║██║ ╚████║██████╔╝╚██████╔╝╚███╔███╔╝██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║╚██████╔╝███████╗██║  ██║
//   ╚══╝╚══╝ ╚═╝╚═╝  ╚═══╝╚═════╝  ╚═════╝  ╚══╝╚══╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝
//
package com.sdgja.SAJInventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.map.IMap;
import com.sdgja.utils.GlobalVar;
import com.sdgja.utils.MouseFocus;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.sdgja.intohell.IntoHellGame.camera;
import static com.sdgja.intohell.IntoHellGame.gamePort;


public class WindowManager implements InputProcessor {

    private float screenX, screenY = 0;
    private int zOrder = 0;
    private Map<String, InventoryManager> windows;

    public WindowManager() {
        windows = new LinkedHashMap<>();
        //  Gdx.input.setInputProcessor(this);
    }

    public void addWindow(String name, InventoryManager inventoryWindow) {
        if(!windows.containsKey(name)) {
            inventoryWindow.setZOrder(windows.size());
            windows.put(name, inventoryWindow);
        }
    }

    public InventoryManager getWindow(String name) {
        return windows.get(name);
    }



    ////////////////////////////////////////////////////////
    // Add text to information window
    public void addInfo(String windowname, String text) {
        InventoryManager i = windows.get(windowname);
        if(i!=null && i.getWindowType()== InventoryManager.WinType.Info || i.getWindowType()==InventoryManager.WinType.Input
                || i.getWindowType()==InventoryManager.WinType.Debug) {// i.isInfoWindow()) { // only can add to an information window
            i.addinfo(windowname, text);
        }
    }

    public void addGrid(String windowname, String gridname, int w, int h, float x, float y) {
        InventoryManager i = windows.get(windowname);
        if(i!=null) {
            i.addGrid(gridname,w,h,x,y);
        }
    }

    public  void showWindows() {
        boolean batchStarted = false;
        for (Map.Entry mapElement : windows.entrySet()) {
            InventoryManager window = (InventoryManager) mapElement.getValue();
            if(window.isShowing()) {
                window.drawInventory(mapElement.getKey().toString(),
                        window.getWindowType()== InventoryManager.WinType.Info || window.getWindowType()== InventoryManager.WinType.Input );
                if(window.getWindowType()== InventoryManager.WinType.Inventory){// != InventoryManager.WinType.Info && window.getWindowType()!= InventoryManager.WinType.Input) {
                    window.drawWindowItems();
                } else {  // must be information window, debug or input
                    window.drawText();  // through the beauty of polymorphism, the correct method will be called based on the child class here
                }
                window.processWindowDrag();   // causes batch.end
            }
        }
        for (Map.Entry mapElement : windows.entrySet()) {
            InventoryManager window = (InventoryManager) mapElement.getValue();
            window.resetInfoContents();
        }
    }

    public  void turnWindowVisible(String name, boolean showing) {
        InventoryManager i = windows.get(name);
        if(i!=null) {
            i.setShowing(showing);
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Check if mouse cursor in the bounds of a window
    private InventoryManager inWindow(float screenX, float screenY) {
        MouseFocus.setFocus(MouseFocus.WindowFocus.Main);
        InventoryManager window = null;
        ArrayList<String> keys = new ArrayList<>(windows.keySet());
        for (int i = keys.size() - 1; i >= 0; i--) {
            window = windows.get(keys.get(i));
            if (window.isShowing()) {
                Vector2 windowPos = window.getPosition();
                Vector2 windowSize = window.getWidthAndHeight();
                if (screenX >= windowPos.x + 14 && screenX < windowPos.x + (windowSize.x + 14)) { // in x-boundary
                    if (screenY >= windowPos.y && screenY < windowPos.y + (windowSize.y)) {
                        // Make this the topmost window now
                        windows.remove(keys.get(i));
                        windows.put(keys.get(i),window);
                        if(window.getWindowType()== InventoryManager.WinType.Input)
                            MouseFocus.setFocus(MouseFocus.WindowFocus.InputWindow);
                        else
                        if(window.getWindowType()==InventoryManager.WinType.Debug)
                            MouseFocus.setFocus(MouseFocus.WindowFocus.DebugWindow);
                        else
                        if(window.getWindowType()==InventoryManager.WinType.Info)
                            MouseFocus.setFocus(MouseFocus.WindowFocus.Console);
                        else
                            MouseFocus.setFocus(MouseFocus.WindowFocus.Gui);
                        break;
                    }
                }
            }
        }


        return window;
    }


    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        float xDiv = (float) gamePort.getScreenWidth()/ GlobalVar.coreX;      // Scale to viewport dimensions
        float yDiv = (float) gamePort.getScreenHeight()/GlobalVar.coreY;

        float newx = ((Gdx.input.getX()-gamePort.getScreenX())/xDiv);
        float newy = ((Gdx.input.getY()-gamePort.getScreenY())/yDiv);
//        System.out.println("[X] " + newx + " [Y] " + newy);

        InventoryManager w = inWindow(newx,newy);// screenX,screenY);
        boolean bLMBPressed = false;
        ArrayList<String> keys = new ArrayList<>(windows.keySet());
        if(w!=null) {
            Vector2 windowPos = w.getPosition();
            //  System.out.println("Window pos: " + windowPos.x + "," + windowPos.y);

            Vector2 windowSize = w.getDimensions();
            float iposy = windowPos.y + 32;
            if(newx < windowPos.x+16  + windowSize.x  && newx > windowPos.x + windowSize.x  ) {
                if (newy+32  >= iposy && newy < iposy-20) {
                    // close?
                    String n = w.getName();
                    turnWindowVisible(w.getName(),true);
                    // Code not great here as ties up with windows names in IntoHellGame and variables
                    switch(w.getName()) {
                        case "Input": IntoHellGame.bInputWindow=false;break;
                        case "Inventory" : IntoHellGame.bInventoryShowing=false;break;
                        case "Console": IntoHellGame.bConsoleShowing=false;break;
                        case "Debug": IntoHellGame.bQuestsShowing=false;break;
                        // change names below to match window names later when we have more
                        case "a":break;
                        case "b":break;
                        case "c":break;
                        case "d":break;
                        case "e":break;
                        case "f":break;
                    }

                    return false;
                }
            }


            if (newx >= windowPos.x && newx < windowPos.x + (windowSize.x * 32)) {
                if (newy + 32 >= iposy && newy < iposy) {  // cursor is in the title bar so we can now drag the window
                    this.screenX = (newx - windowPos.x) - 16;
                    this.screenY = newy - iposy;
                    bLMBPressed = true;
                    w.setDragging(bLMBPressed);
                    w.setScreenXOffset(this.screenX);
                    w.setScreenYOffset(this.screenY);
                    return true;
                } else { // are we on a scroll bar
                    //if(w.isInfoWindow()) {
                    ParentWindow pw = w.getFirstWindow(w.getName());
                    if (pw instanceof InfoWindow) {
                        if (newx >= windowPos.x + (windowSize.x-8)  && newx < windowPos.x + windowSize.x+8 ) {
                            if (newy + 48 >= iposy && newy < iposy + 24) {  // scroll up

                                ((InfoWindow) pw).decScrollYPos();
                            } else {
                                Vector2 wh = w.getWidthAndHeight();
                                int yy = (int) (newy - wh.y);
                                if (yy >= windowPos.y - 32 && yy < windowPos.y - 16) {  // scroll down

                                    ((InfoWindow) pw).incScrollYPos();
                                }
                            }
                        }
                    }
                }
                //}
            }
        }
        return false;// bLMBPressed;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (Map.Entry mapElement : windows.entrySet()) {
            InventoryManager window = (InventoryManager) mapElement.getValue();
            window.setDragging(false);
        }
        return false;
        //return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //inWindow(screenX,screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
