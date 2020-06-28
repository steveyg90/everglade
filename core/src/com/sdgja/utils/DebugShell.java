
//  ██████╗ ███████╗██████╗ ██╗   ██╗ ██████╗ ███████╗██╗  ██╗███████╗██╗     ██╗
//  ██╔══██╗██╔════╝██╔══██╗██║   ██║██╔════╝ ██╔════╝██║  ██║██╔════╝██║     ██║
//  ██║  ██║█████╗  ██████╔╝██║   ██║██║  ███╗███████╗███████║█████╗  ██║     ██║
//  ██║  ██║██╔══╝  ██╔══██╗██║   ██║██║   ██║╚════██║██╔══██║██╔══╝  ██║     ██║
//  ██████╔╝███████╗██████╔╝╚██████╔╝╚██████╔╝███████║██║  ██║███████╗███████╗███████╗
//  ╚═════╝ ╚══════╝╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝
//

package com.sdgja.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public final class DebugShell {

    private static boolean[] layers = {true,true,true,true};
    private static boolean[] layerKeys = {false,false,false,false};
    private static TextInput listener;
    private static String spawnString = "";

    public static String getSpawnString() { return spawnString; }
    public static void clearSpawnString() { spawnString=""; }

    public static boolean renderBuilding() {
        return layers[1];
    }
    public static boolean renderSprites() {
        return layers[3];
    }
    public static boolean renderForeground() {
        return layers[2];
    }
    public static boolean renderBackMap() {
        return layers[0];
    }

    public static void processDebugKeyCheck() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))  {
            layers[0] = !layers[0];
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))  {
            layers[1] = !layers[1];
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))  {
            layers[2] = !layers[2];
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))  {
            layers[3] = !layers[3];
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F1))  {
            spawnString ="thumper";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2))  {
            spawnString ="sentry";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3))  {
            spawnString ="beacon";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F4))  {
            spawnString ="silo";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F5))  {
            spawnString ="torch";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F6))  {
            spawnString ="foundation";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F7))  {
            spawnString ="beanstalk";
        }
    }

//    public static void doDebugCommands() {
//
//        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
//            listener = new TextInput();
//            Gdx.input.getTextInput(listener, "Debug", "", "Enter debug command");
//        }
//
//        if(listener!=null) {
//            //spawnString = "";
//            if (listener.getText() != null) {
//                if (listener.getText().contains("spawn")) {
//                    String[] c = listener.getText().split(" ");
//                    spawnString = c[1];
//                }
//            }
//        }
//    }
}
