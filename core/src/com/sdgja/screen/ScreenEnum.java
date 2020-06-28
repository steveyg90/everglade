package com.sdgja.screen;

import com.sdgja.intohell.IntoHellGame;

public enum ScreenEnum {

    TITLE {
        public AbstractScreen getScreen(Object... params) {
            return new TitleScreen();
        }
    },
    CHARACTER_SELECT {
        public AbstractScreen getScreen(Object... params) {
            return new CharacterCreation();
        }
    },
    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new IntoHellGame();
           // return new GameScreen((Integer) params[0]);
        }
    },

    OPTIONS {
        public AbstractScreen getScreen(Object... params) {
            return new OptionsScreen();
        }
    };
    public abstract AbstractScreen getScreen(Object... params);
}