package com.sdgja.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.sdgja.utils.GlobalVar;


public abstract class AbstractScreen extends Stage implements Screen {

    public Skin skin;

    public static InputMultiplexer inputMultiplexer = new InputMultiplexer();

    protected AbstractScreen() {
        super( new ScalingViewport(Scaling.fit, GlobalVar.coreX,GlobalVar.coreY,new OrthographicCamera()));
    }

    // Subclasses must load actors in this method
    public abstract void buildStage();

    @Override
    public void render(float delta) {
        // Clear screen

//        GlobalVar.setFullScreen();
        super.act(delta);
        super.draw();
    }

    @Override
    public void show()
    {
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override public void hide() {}
    @Override public void pause() {
        System.out.println("Dragging window...");
    }
    @Override public void resume() {}
}