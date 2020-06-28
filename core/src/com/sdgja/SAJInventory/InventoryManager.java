
//  ██╗███╗   ██╗██╗   ██╗███████╗███╗   ██╗████████╗ ██████╗ ██████╗ ██╗   ██╗███╗   ███╗ █████╗ ███╗   ██╗ █████╗  ██████╗ ███████╗██████╗
//  ██║████╗  ██║██║   ██║██╔════╝████╗  ██║╚══██╔══╝██╔═══██╗██╔══██╗╚██╗ ██╔╝████╗ ████║██╔══██╗████╗  ██║██╔══██╗██╔════╝ ██╔════╝██╔══██╗
//  ██║██╔██╗ ██║██║   ██║█████╗  ██╔██╗ ██║   ██║   ██║   ██║██████╔╝ ╚████╔╝ ██╔████╔██║███████║██╔██╗ ██║███████║██║  ███╗█████╗  ██████╔╝
//  ██║██║╚██╗██║╚██╗ ██╔╝██╔══╝  ██║╚██╗██║   ██║   ██║   ██║██╔══██╗  ╚██╔╝  ██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║██║   ██║██╔══╝  ██╔══██╗
//  ██║██║ ╚████║ ╚████╔╝ ███████╗██║ ╚████║   ██║   ╚██████╔╝██║  ██║   ██║   ██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║╚██████╔╝███████╗██║  ██║
//  ╚═╝╚═╝  ╚═══╝  ╚═══╝  ╚══════╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝
//

package com.sdgja.SAJInventory;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.sdgja.actor.Player;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.utils.MouseFocus;
import static com.sdgja.map.IMap.charset;
import static com.sdgja.map.IMap.FONTWIDTH;
import static com.sdgja.map.IMap.FONTHEIGHT;
import java.util.*;
import java.util.regex.Pattern;
import static com.sdgja.intohell.IntoHellGame.gamePort;
import com.sdgja.utils.GlobalVar;
import com.sdgja.utils.GlobalVar;


public class InventoryManager implements Disposable{
    private boolean bLMBPressed = false;
    private float screenX = 0, screenY = 0;

    private int zOrder = 0;
    private char[][] grid;
    private int w,h, tileWidth, tileHeight;
    private float x, y;
    private SpriteBatch batch;
    private Texture texture;
    private boolean showing = false;
    private boolean infoWindow= false;
    private HashMap<String, ParentWindow> windowsGrids = new HashMap<>();
    private boolean debugWindow = false;
    private String name = "";
    private WinType windowType = WinType.Inventory;

    public String getName() { return this.name; }

    public int getGridAmount() { return windowsGrids.size(); }

    public boolean isDebugWindow() { return this.debugWindow;}

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Reset the text contents in debug window as doesn't support scrolling
    public void resetInfoContents() {
        for (Map.Entry mapElement : windowsGrids.entrySet()) {
            ParentWindow window = (ParentWindow) mapElement.getValue();
            if( !(window  instanceof InfoWindow) && !(window instanceof Window)) {
                ((DebugWindow)window).reset();
            }
        }
    }

    public void setScreenXOffset(float xOffset) {
        this.screenX = xOffset;
    }
    public void setScreenYOffset(float yOffset) {
        this.screenY = yOffset;
    }

    public void setDragging(boolean bDrag) {
        this.bLMBPressed = bDrag;
    }

    public boolean isShowing() {
        return showing;
    }
    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public static int getHeight() {
        return GlobalVar.coreY;
    }
    public static int getWidth() {
        return GlobalVar.coreX;
    }

    public Vector2 getPosition() {
        return new Vector2(this.x-16, getHeight() - this.y -32);
        //return new Vector2(this.x-16, Gdx.graphics.getHeight() - this.y -32);
    }
    public Vector2 getDimensions() {
        return new Vector2(this.w * tileWidth, 32);
    }
    public Vector2 getWidthAndHeight() {
        return new Vector2(this.w*tileWidth, this.h*tileHeight);
    }

    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }
    public int getzOrder() { return this.zOrder; }

    public boolean isInfoWindow() { return this.infoWindow; }

    enum WinType {
        Inventory,
        Debug,
        Info,
        Input,
        Console,
    };

    public WinType getWindowType() {
        return this.windowType;
    }

    // used to add either debug or info window type
    public InventoryManager(String name, boolean infoWindow, boolean bDebug, int w, int h,  float x, float y) {
        this.name = name;
        ParentWindow pw = null;
        init(w,h,x,y,32,32);
        this.infoWindow = infoWindow;
        if(infoWindow && bDebug) {
            pw = new DebugWindow(x, y, w, h);
            windowType = WinType.Debug;
        } else {
            pw = new InfoWindow(x,y,w,h);
            windowType = WinType.Info;
        }
        windowsGrids.put(name,pw);
    }

    // Used to add input window type
    public InventoryManager(String name, int w, int h, int tileWidth, int tileHeight, float x, float y, InputMultiplexer inputMultiplexer) {
        init(w,h,x,y,tileWidth,tileHeight);
        this.grid = new char[h][w];
        this.infoWindow = false;
        this.name=name;
        windowType = WinType.Input;
        InputWindow iw = new InputWindow(x,y,w,h);
        windowsGrids.put(name,iw);
        inputMultiplexer.addProcessor(iw);
    }

    // Used to add inventory window type
    public InventoryManager(int w, int h, int tileWidth, int tileHeight, float x, float y) {
        init(w,h,x,y,tileWidth,tileHeight);
        this.grid = new char[h][w];
        this.infoWindow = false;
        windowType = WinType.Inventory;
    }

    private void init(int w, int h, float x, float y, int tileWidth, int tileHeight) {
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
        this.texture = IntoHellGame.getGuiTexture();
        this.batch = new SpriteBatch();
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
//        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        batch.setShader(null);
    }

    public void positionWindow(Vector2 position) {
        this.x = position.x;
        this.y = position.y;
    }
    public void startBatch() {
        batch.begin();
    }
    public void drawInventory(String name, boolean scrollBars) {
        String title = name;
        float x=this.x;
        float y=this.y;

        batch.begin();

        batch.setColor(1,1,1,0.75f);
        for(int i=0;i<h;i++) {
            for(int j=0;j<w;j++) {
                if(j>0 && j<w-1 &&i>0&&i<h-1)
                    batch.draw(texture,x,y+0.2f,32,32,tileWidth,tileHeight);
                if(j==0 && i==0)
                    batch.draw(texture,x,y,0,0,tileWidth,tileHeight);
                if(j==w-1 && i==0)
                    batch.draw(texture,x,y+0.2f,64,0,tileWidth,tileHeight);
                if(j>0 && j<w-1 && i==0)
                    batch.draw(texture,x,y,32,0,tileWidth,tileHeight);
                if(j==0 && i>0&&i<h-1)
                    batch.draw(texture,x,y+0.2f,0,32,tileWidth,tileHeight);
                if(j==w-1 && i>0&&i<h-1)
                    batch.draw(texture,x,y+0.2f,64,32,tileWidth,tileHeight);
                if(i==h-1 && j>0&&j<w-1)
                    batch.draw(texture,x,y,32,64,tileWidth,tileHeight);
                if(j==0 && i==h-1)
                    batch.draw(texture,x,y,0,64,tileWidth,tileHeight);
                if(j==w-1 && i==h-1)
                    batch.draw(texture,x,y,64,64,tileWidth,tileHeight);

                x+=tileWidth;
            }
            x=this.x;
            y-=tileHeight;
        }
        x = this.x;
        x += (w*tileWidth)/2;
        x -= (title.length()/2)*FONTWIDTH;

        //   batch.setColor(1,1,1,1);
        for(int i=0; i<title.length();i++) {
            batch.draw(this.texture, x + (i*FONTWIDTH), this.y+FONTHEIGHT/2, FONTWIDTH, FONTHEIGHT, getCharPos(title.charAt(i)), 224, FONTWIDTH, FONTHEIGHT, false, false);
        }

        Vector2 wh = getWidthAndHeight();
        Vector2 pos = this.getPosition();
        if(scrollBars) {  // need to draw some scroll bars
            batch.draw(this.texture, this.x + (wh.x-20) , this.y-(tileHeight>>1)-8, FONTWIDTH, FONTHEIGHT, getCharPos(charset.charAt(29)), 224, FONTWIDTH, FONTHEIGHT, false, false);
            batch.draw(this.texture, this.x + (wh.x-20) , this.y-wh.y+48, FONTWIDTH, FONTHEIGHT, getCharPos(charset.charAt(30)), 224, FONTWIDTH, FONTHEIGHT, false, false);
        }

        // Need to show flashing cursor
        if(getWindowType() == windowType.Input) {
            InputWindow iw = (InputWindow)getFirstWindow(this.name);
            if(iw!=null) {
                if(iw.getFlashCount()>0 && iw.getCursorOn()) {
                    float c = iw.getFlashCount();
                    c-=0.1f;
                    iw.setFlashCount(c);
                    batch.draw(this.texture, this.x+10 , this.y - wh.y + 44, FONTWIDTH, FONTHEIGHT, getCharPos(charset.charAt(26)), 224, FONTWIDTH, FONTHEIGHT, false, false);
                } else
                {
                    iw.setCursorOn(false);
                    batch.draw(this.texture, this.x+10 , this.y - wh.y + 44, FONTWIDTH, FONTHEIGHT, getCharPos(charset.charAt(0)), 224, FONTWIDTH, FONTHEIGHT, false, false);
                    if(iw.getFlashCount()>2) {
                        iw.setCursorOn(true);
                    } else {
                        float c = iw.getFlashCount();
                        c+=0.1f;
                        iw.setFlashCount(c);
                    }
                }
            }
        }

      //  batch.end();

    }

    public void endBatch() {
        batch.end();
    }
    public void processWindowDrag() {
        batch.end();
        if(bLMBPressed) {
            float xDiv = (float) gamePort.getScreenWidth()/GlobalVar.coreX;      // Scale to viewport dimensions
            float yDiv = (float) gamePort.getScreenHeight()/GlobalVar.coreY;

            float mouseX = ((Gdx.input.getX()-gamePort.getScreenX())/xDiv);
            float mouseY = ((Gdx.input.getY()-gamePort.getScreenY())/yDiv);


            //       float mouseX =Gdx.input.getX() / (IntoHellGame.fullScreen ? 1.5f : 1.0f);
            //     float mouseY = Gdx.input.getY() / (IntoHellGame.fullScreen ? 1.5f : 1.0f);
            mouseY = getHeight() - mouseY;
//            mouseY = Gdx.graphics.getHeight() - mouseY;
            mouseX -= screenX;
            mouseY += screenY;
            positionWindow(new Vector2(mouseX, mouseY));
            float tx=this.x;
            if(this.x <= 10) {
                positionWindow(new Vector2(0,mouseY));
                tx=0;
            } else {
                if (this.x >= getWidth() /*Gdx.graphics.getWidth()*/ - tileWidth * w) {
                    positionWindow(new Vector2(  getWidth() /*Gdx.graphics.getWidth()*/ - tileWidth * w, mouseY));
                    tx=getWidth() /*Gdx.graphics.getWidth()*/ - tileWidth * w;
                }
            }
            float height = (h-1)*tileHeight;
            if(this.y <= height ){
                positionWindow(new Vector2(tx, height));
            } else {
                if (this.y >= getHeight() /*Gdx.graphics.getHeight()*/ - tileHeight)
                    positionWindow(new Vector2(tx, getHeight() /*Gdx.graphics.getHeight()*/ - tileHeight));
            }
        }
    }

    public void addGrid(String name, int w, int h, float x, float y) {
        if(!windowsGrids.containsKey(name)) {
            Window window = new Window(x,y,w,h);
            windowsGrids.put(name,window);
        }
    }

    public void displayGrid(String name, int w, int h, int x, int y) {
        ParentWindow window = windowsGrids.get(name);
        if(window!=null) {
            drawGrid(name,w,h,x,y);
        }
    }

    public void drawGrid(String name, int w, int h, float x, float y) {
        float ty = y;
        //   batch.begin();
        x *=tileWidth;
        x+=this.x+tileWidth;
        y=0 - y;
        y *=tileHeight;
        y+=this.y-tileHeight;
        float tx = x;
        batch.setColor(1,1,1,0.75f);
        for(int i=0;i<h;i++) {
            for (int j = 0; j < w; j++) {
                batch.draw(texture,x,y,0,0,tileWidth,tileHeight,1,1,0,96,0,tileWidth,tileHeight,false,false);
                x+=tileWidth;
            }
            x=tx;
            y-=tileHeight;
        }
        x=tx;
        for(int i=0; i<name.length();i++) {
            batch.draw(this.texture, tx + (i*FONTWIDTH), y+16, FONTWIDTH, FONTHEIGHT, getCharPos(name.charAt(i)), 224, FONTWIDTH, FONTHEIGHT, false, false);
        }
        //    batch.end();
    }

    private int getCharPos(char letter) {
        return charset.indexOf(letter)*FONTWIDTH;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Get inventory items for given grid
    public ParentWindow getInventoryGrid(String name) {
        return windowsGrids.get(name);
    }

    public void drawWindowItems() {
        for (Map.Entry mapElement : windowsGrids.entrySet()) {
            Window windowItem = (Window) mapElement.getValue();
            drawGrid(mapElement.getKey().toString(), windowItem.getW(), windowItem.getH(), windowItem.getX(),windowItem.getY());
        }
    }

    public ParentWindow getFirstWindow(String name) {
        return windowsGrids.get(name);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // Used if debug window, information window
    public void drawText() {

        float tx = x + FONTWIDTH + 4;
        float ty = y-FONTHEIGHT;
        String name="";
        // if(infoWindow) { // sanity check
        for (Map.Entry mapElement : windowsGrids.entrySet())
            name = mapElement.getKey().toString();
        ParentWindow window = (ParentWindow)windowsGrids.get(name);
        if(window instanceof InfoWindow) {  // could be InfoWindow OR InputWindow
            if(window instanceof InputWindow) {// && MouseFocus.getFocus()==MouseFocus.WindowFocus.InputWindow) {
                float posY = window.getY();
                // need to allow input
                String s = ((InputWindow)window).getString().toString();
                for(int j=0; j < s.length();j++) {
                    batch.draw(this.texture, 10+tx + (j * FONTWIDTH), y - ((window.getH()+3)*FONTHEIGHT)-4 , FONTWIDTH, FONTHEIGHT, getCharPos(s.charAt(j)), 224, FONTWIDTH, FONTHEIGHT, false, false);
                }
                // now draw stored commands
                InputWindow win = (InputWindow)window;
                Stack<String> text = win.getInfo();
                int maxamount = window.h * 2;
                for (int i = win.getScrollYPos(); i < maxamount + win.getScrollYPos() - 4; i++) {
                    if (i < win.getCount()) {
                        String str = text.get(i);
                        for (int j = 0; j < str.length(); j++) {
                            batch.draw(this.texture, tx + (j * FONTWIDTH), ty, FONTWIDTH, FONTHEIGHT, getCharPos(str.charAt(j)), 224, FONTWIDTH, FONTHEIGHT, false, false);
                        }
                        tx = x + FONTWIDTH + 2;
                        ty -= FONTHEIGHT;
                    }
                }
            } else {
                InfoWindow win = (InfoWindow)window;
                if(window!=null ) {
                    List<String> text = win.getInformationText();
                    int maxamount = window.h * 2;
                    for (int i = win.getScrollYPos(); i < maxamount + win.getScrollYPos() - 3; i++) {
                        if (i < win.getCount() && text.size()>0) {
                            String str = text.get(i);
                            for (int j = 0; j < str.length(); j++) {
                                batch.draw(this.texture, tx + (j * FONTWIDTH), ty, FONTWIDTH, FONTHEIGHT, getCharPos(str.charAt(j)), 224, FONTWIDTH, FONTHEIGHT, false, false);
                            }
                            tx = x + FONTWIDTH + 2;
                            ty -= FONTHEIGHT;
                        }
                    }
                }
            }
        } else { // must be a debug window, not implemented scrolling for this as yet - TODO: scrolling for debug window???....
            if (window != null) {
                DebugWindow dw = (DebugWindow)window;
                List<String> text = dw.getInformationText();
                for (String str : text) {
                    for (int i = 0; i < str.length(); i++) {
                        batch.draw(this.texture, tx + (i * FONTWIDTH), ty, FONTWIDTH, FONTHEIGHT, getCharPos(str.charAt(i)), 224, FONTWIDTH, FONTHEIGHT, false, false);
                    }
                    tx = x + FONTWIDTH + 2;
                    ty -= FONTHEIGHT;
                }
            }
        }
        // }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    // Add text to information window
    public void addinfo(String windowname, String text) {
        ParentWindow window = null;
        if(windowsGrids.get(windowname) instanceof InfoWindow) {
            window = (InfoWindow) windowsGrids.get(windowname);
        } else window = (DebugWindow) windowsGrids.get(windowname);
        if(window!=null) {
            window.addInfo(text);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    // Allows you to add an inventory item to given grid at given cellx, celly
    public void addItem(String name, InventoryItem item, int x, int y) {
        ParentWindow window = windowsGrids.get(name);
        if(window!=null) {
            if(window instanceof Window) {
                ((Window) window).getItems()[y][x] = item;
            }
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}


//  ██╗    ██╗██╗███╗   ██╗██████╗  ██████╗ ██╗    ██╗
//  ██║    ██║██║████╗  ██║██╔══██╗██╔═══██╗██║    ██║
//  ██║ █╗ ██║██║██╔██╗ ██║██║  ██║██║   ██║██║ █╗ ██║
//  ██║███╗██║██║██║╚██╗██║██║  ██║██║   ██║██║███╗██║
//  ╚███╔███╔╝██║██║ ╚████║██████╔╝╚██████╔╝╚███╔███╔╝
//   ╚══╝╚══╝ ╚═╝╚═╝  ╚═══╝╚═════╝  ╚═════╝  ╚══╝╚══╝
//

abstract class ParentWindow {
    protected float x, y;
    protected int w, h;
    public float getX() { return x;}
    public float getY() { return y;}
    public int getW() { return w;}
    public int getH() { return h; }

    public ParentWindow(float x, float y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    abstract void addInfo(String text);
}

class Window extends ParentWindow {

    public Window(float x, float y, int w, int h) {
        super(x,y,w,h);
        items = new InventoryItem[h][w];
    }

    private InventoryItem[][] items;
    public InventoryItem[][] getItems() { return this.items; }
    public void addInfo(String text) {
        //
    }
}


class DebugWindow extends ParentWindow {
    protected List<String> info;
    protected int scrollYPos;

    public DebugWindow(float x, float y, int w, int h) {
        super(x, y, w, h);
        info = new ArrayList<>();
        scrollYPos = 8;
    }

    public void setScrollYPos() { this.scrollYPos = info.size()-h-(h/2);}

    public List<String>getInformationText() { return this.info; }
    public int getScrollYPos() { return this.scrollYPos; }
    public int getCount() { return info.size(); }
    public void incScrollYPos() {
        if(scrollYPos<info.size() - h - (h/2))
            scrollYPos++;
    }
    public void decScrollYPos() {
        if(scrollYPos>0)
            scrollYPos--;
    }

    public void addInfo(String text) {
        //   if(info.size()<(h*2)-3) // Remove later when got scrolling
        if(info.size()>25) {
            info.remove(0);
        }
        info.add(text);
        incScrollYPos();

    }

    public void reset() {
        info.clear();
    }

}
class InputWindow extends InfoWindow implements Disposable, InputProcessor{
    private Stack<String> info;

    public Stack<String>getInfo() { return this.info; }

    private float flashCount = 2;
    private boolean cursorOn = true;

    private String strCommand;
    private StringBuilder strBuilder;
    public StringBuilder getString() { return this.strBuilder; }

    private int historyPos = 0, hp = 0;

    private final String[] playerFeatureCommands = {"head","torso","legs","feet","arms"};

    private CMDS _commands;

    public float getFlashCount() {
        return this.flashCount;
    }
    public void setFlashCount(float count) {
        this.flashCount=count;
    }
    public boolean getCursorOn() {
        return this.cursorOn;
    }
    public void setCursorOn(boolean b) {
        this.cursorOn = b;
    }

    private InfoWindow infoWindow;

    public InputWindow(float x, float y, int w, int h) {
        super(x, y, w, h);
        scrollYPos = 0;
        strBuilder = new StringBuilder();
        strBuilder.setLength(0);
        info = new Stack<>();
        _commands = new CMDS(playerFeatureCommands);
        InventoryManager i = IntoHellGame.getWinManager().getWindow("Console");
        infoWindow = (InfoWindow)i.getFirstWindow("Console");

    }

    @Override
    public void addInfo(String text) {
        info.push(text);
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        if(MouseFocus.getFocus()==MouseFocus.WindowFocus.InputWindow) {
            if(keycode==Input.Keys.DEL) {
                if(strBuilder.length()>0) {
                    String temp = strBuilder.substring(0, strBuilder.length() - 1);
                    strBuilder.setLength(0);
                    strBuilder.append(temp);

                }
            }
            if(keycode == Input.Keys.UP) {
                // need to get previous string
                if(info.size()>=0) {
                    String tmp = strBuilder.toString();
                    strBuilder.setLength(0);
                    int pos = historyPos;//.size()-1 - historyPos;
                    if(pos>0) {
                        tmp = info.get(historyPos);//.size() - 1 - historyPos);
                        strBuilder.append(tmp);
                        if(historyPos>0)
                            historyPos--;
                    } else {
                        strBuilder.append(tmp);
                        //  historyPos=info.size()-1;
                    }

                }
            }
            if(keycode == Input.Keys.DOWN) {
                // need to get previous string
                if(info.size()>0) {
                    String tmp = strBuilder.toString();
                    strBuilder.setLength(0);
                    if(historyPos<info.size()-1)
                        historyPos++;
                    int pos = historyPos;//.size()-1 - historyPos;
                    if(pos>0) {
                        tmp = info.get(historyPos);//.size() - 1 - historyPos);
                        strBuilder.append(tmp);
                        //     if(historyPos<info.size()-1)
                        //        historyPos++;
                    } else {
                        strBuilder.append(tmp);
                        //historyPos=0;//info.size()-1;
                    }

                }
            }
            if (keycode == Input.Keys.ENTER) {
                String strCommand = strBuilder.toString();
                strCommand.replace("\r","");
                strCommand = strCommand.trim();
                parseCommand(strCommand);
                strBuilder.setLength(0);
                if(scrollYPos<info.size()-9) {
                    scrollYPos=info.size()-9;
                }
                this.incScrollYPos();
                if(historyPos<info.size()) {
                    historyPos=info.size()-1;
                }
                //     historyPos++;
            }

        }
        return true;
    }

    // private final String[] playerFeatureCommands = {"head","torso","legs","feet","arms"};
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private String parseCommand(String strCommand) {
        String[] strSyntax = strCommand.split(" ");
        strCommand = strCommand.replaceAll(" .+$", "");
        for (String c : Arrays.asList(strSyntax)) {
            if(strCommand.startsWith(c)) {
                if(strSyntax.length>1 && isNumeric(strSyntax[1])) {
                    Player player = (Player) IntoHellGame.getPlayer();
                    int num = Integer.parseInt(strSyntax[1]);
                    strCommand = this._commands.execute(strCommand,num);
                } else {
                    infoWindow.addInfo("Command not recognised " + strCommand);
                    return "";
                }
            }
        }
        return strCommand;// + " " + strSyntax[1] + " successful";
    }
    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    @Override
    public void incScrollYPos() {
        if(scrollYPos<info.size() - h + 1 - (h/2))
            scrollYPos++;
    }
    @Override
    public void decScrollYPos() {
        if(scrollYPos>0)
            scrollYPos--;
    }
    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if(MouseFocus.getFocus()==MouseFocus.WindowFocus.InputWindow)
            if(character!='\b' && character!='\r' && character!='\n') // if not a backspace
                strBuilder.append(character);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
    }


    class CMDS {
        Player player = null;
        HashMap<String, ICmd> commandMap = new HashMap<>();

        public CMDS(String[] c) {
            commandMap.put("head", executeHead);
            commandMap.put("torso", executeTorso);
        }

        public void addCommand(String operation, ICmd command) {
            if (!commandMap.containsKey(operation)) {
                commandMap.put(operation, command);
            }
        }

        public String execute(String operation, int i) {
            if (commandMap.containsKey(operation)) {
                ICmd command = commandMap.get(operation);
                command.execute(operation,i);
                return "";

            }
            infoWindow.addInfo("Invalid command");
            return "";
        }

        ICmd executeHead = (s,i) -> {
            player = (Player) IntoHellGame.getPlayer();
            if (player.getHeadAmount() > i) {
                player.setHead(i);
                addInfo(s + " " + i);
            } else {
                infoWindow.addInfo("*** Array out of bounds (0-" + (player.getHeadAmount() - 1) + ")");
            }
            return "";
            //  return s + " " + i;
        };

        ICmd executeTorso = (s,i) -> {
            player = (Player) IntoHellGame.getPlayer();
            return "Set Torso successfully executed";
        };
    }
}

interface ICmd {
    String execute(String s,int i);
}

class InfoWindow extends DebugWindow {

    public InfoWindow(float x, float y, int w, int h) {
        super(x, y, w, h);
    }

    public void addInfo(String text) {
        super.addInfo(text);
        //  info.add(text);
    }
}


//  ██╗███╗   ██╗██╗   ██╗███████╗███╗   ██╗████████╗ ██████╗ ██████╗ ██╗   ██╗██╗████████╗███████╗███╗   ███╗
//  ██║████╗  ██║██║   ██║██╔════╝████╗  ██║╚══██╔══╝██╔═══██╗██╔══██╗╚██╗ ██╔╝██║╚══██╔══╝██╔════╝████╗ ████║
//  ██║██╔██╗ ██║██║   ██║█████╗  ██╔██╗ ██║   ██║   ██║   ██║██████╔╝ ╚████╔╝ ██║   ██║   █████╗  ██╔████╔██║
//  ██║██║╚██╗██║╚██╗ ██╔╝██╔══╝  ██║╚██╗██║   ██║   ██║   ██║██╔══██╗  ╚██╔╝  ██║   ██║   ██╔══╝  ██║╚██╔╝██║
//  ██║██║ ╚████║ ╚████╔╝ ███████╗██║ ╚████║   ██║   ╚██████╔╝██║  ██║   ██║   ██║   ██║   ███████╗██║ ╚═╝ ██║
//  ╚═╝╚═╝  ╚═══╝  ╚═══╝  ╚══════╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝   ╚═╝   ╚══════╝╚═╝     ╚═╝
//

class InventoryItem {
    private int x,y;
    private String name;
    private Texture image;
}

