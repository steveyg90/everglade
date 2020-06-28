package com.sdgja.intohell;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Graphics;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.sdgja.map.IMap;
import com.sdgja.screen.ScreenEnum;
import com.sdgja.screen.ScreenManager;
import com.sdgja.utils.GlobalVar;

import static com.sdgja.filesystem.IFileName.FNMousePointer;

public class MechaFirma extends Game {
    @Override
    public void create () {

        // TODO - is this the best way?
//        gamePort.getScreenWidth()
//        GlobalVar.coreX=Gdx.graphics.getDisplayMode().width;
//        GlobalVar.coreY=Gdx.graphics.getDisplayMode().height;
////////////////////////////////
        Pixmap MPoint = new Pixmap(Gdx.files.internal(FNMousePointer));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(MPoint, 0, 0));
        MPoint.dispose();
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen( IMap.bMenuScreens ? ScreenEnum.TITLE : ScreenEnum.GAME);
    }
}
