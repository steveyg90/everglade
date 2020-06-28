package com.sdgja.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIFactory {

    public static ImageButton createButton(Texture texture) {
        return
               new ImageButton(
                        new TextureRegionDrawable(
                                new TextureRegion(texture) ) );
    }

    public static InputListener createListener(final ScreenEnum dstScreen, final Object... params) {
        return
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x,
                                             float y, int pointer, int button) {
                        ScreenManager.getInstance().showScreen(dstScreen, params);
                        return false;
                    }
                };
    }

    public static InputListener addOnMoveListener(ImageButton btn) {
        return
                new InputListener() {
                    public void enter(InputEvent event, float x,
                                             float y, int pointer, Actor from) {
                        btn.setColor(1,1,1,1f);

                    }
                };
    }
    public static InputListener addOnMoveExit(ImageButton btn) {
        return
                new InputListener() {
                    public void exit(InputEvent event, float x,
                                      float y, int pointer, Actor from) {
                        btn.setColor(1,1,1,.5f);

                    }
                };
    }

    public static void setBounds(ImageButton button) {
        button.setTouchable(Touchable.enabled);
        button.setBounds(button.getX(), button.getY(), button.getWidth(), button.getHeight());
    }
}
