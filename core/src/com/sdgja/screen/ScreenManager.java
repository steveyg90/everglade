package com.sdgja.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.map.IMap;

public class ScreenManager {

    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to game
    private Game game;

    // Singleton: private constructor
    private ScreenManager() {
        super();
    }

    // Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Initialization with the game class
    public void initialize(Game game) {
        this.game = game;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(ScreenEnum screenEnum, Object... params) {

        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Check if main game screen (would only happen if not showing title/menu screen)
        if(screenEnum.equals(ScreenEnum.GAME)&&currentScreen instanceof IntoHellGame)
        {
            currentScreen.dispose();
            currentScreen = null;
        }

        AbstractScreen newScreen = screenEnum.getScreen(params);
        newScreen.buildStage();
        game.setScreen(newScreen);

        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}