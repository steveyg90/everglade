package com.sdgja.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;

//import java.awt.*;

public final class GlobalVar {

    // REMEMBER TO CHECK 'MechaFirma.java' for the usage of these vars at boot time.

    public static boolean fullscreen = true;

    public static Graphics.DisplayMode displayMode;
    public static int coreX;
    public static int coreY;

    public static int spriteYAdjust =6+8;

//    public static void setFullScreen() {
//        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
//            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
//                if (!fullscreen) {
//                    fullscreen = true;
//                    switch (GetOS.getOS()) {
//                        case WINDOWS:
//        // FS window mode (windows only, not supported on other platforms)
//        //                            System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
//        //                            Gdx.graphics.setWindowedMode(GlobalVar.coreX, GlobalVar.coreY);
//        //                            break;
//        //                        default:
//        // True FS mode
//                            System.out.println(Gdx.graphics.getDisplayMode());
//                            System.out.println(GlobalVar.displayMode);
//
//                            Gdx.graphics.setWindowedMode(GlobalVar.coreX, GlobalVar.coreY);
//                            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
//                            break;
//                    };
//                } else {
//                    fullscreen = false;
//                    System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
//                    Gdx.graphics.setWindowedMode(GlobalVar.coreX/2, GlobalVar.coreY/2);
//                }
//            }
//        }
//    }

}
