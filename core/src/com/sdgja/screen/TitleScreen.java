package com.sdgja.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sdgja.filesystem.IFileName;
import static com.sdgja.filesystem.IFileName.FNGuiTexture;

public class TitleScreen extends AbstractScreen {
    private Texture texture, buttonTexture;
    private TextureRegion myTextureRegion;
    private TextureRegionDrawable myTexRegionDrawable;
    private ImageButton createButton, optionsButton, creditsButton;
    private Skin skin;

    public TitleScreen() {
        super();
    }

    @Override
    public void buildStage() {
        MyActor act = new MyActor();
        act.setTouchable(Touchable.enabled);
        act.setBounds(act.actorX+getCamera().viewportWidth/2-550/2,act.actorY+getCamera().viewportHeight-213,550,213);
        addActor(act);

        createButton = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/1.png"));
        createButton.setPosition(getCamera().viewportWidth/2-createButton.getWidth()/2,act.actorY+getCamera().viewportHeight - (act.getHeight()*1.5f)-70);
        UIFactory.setBounds(createButton);
        createButton.setColor(1,1,1,0.5f);
        createButton.addListener(UIFactory.createListener(ScreenEnum.CHARACTER_SELECT));
        createButton.addListener(UIFactory.addOnMoveListener(createButton));
        createButton.addListener(UIFactory.addOnMoveExit(createButton));
        addActor(createButton);

        optionsButton = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/1.png"));
        optionsButton.setColor(1,1,1,0.5f);
        optionsButton.setPosition(getCamera().viewportWidth/2-optionsButton.getWidth()/2,createButton.getY() - createButton.getHeight()-50);
        UIFactory.setBounds(optionsButton);
        optionsButton.addListener(UIFactory.createListener(ScreenEnum.OPTIONS));
        optionsButton.addListener(UIFactory.addOnMoveListener(optionsButton));
        optionsButton.addListener(UIFactory.addOnMoveExit(optionsButton));
    //    optionsButton.addListener(UIFactory.createListener(ScreenEnum.CHARACTER_SELECT));
        addActor(optionsButton);

        creditsButton = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/1.png"));
        creditsButton.setColor(1,1,1,0.5f);
        creditsButton.setPosition(getCamera().viewportWidth/2-creditsButton.getWidth()/2, optionsButton.getY() - optionsButton.getHeight()-50);
        UIFactory.setBounds(creditsButton);
        creditsButton.addListener(UIFactory.addOnMoveListener(creditsButton));
        creditsButton.addListener(UIFactory.addOnMoveExit(creditsButton));
    //    creditsButton.addListener(UIFactory.createListener(ScreenEnum.CHARACTER_SELECT));
        addActor(creditsButton);

    }

    public void render(float delta) {
       // super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.act(delta);
        super.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    class MyActor extends Actor {
        float actorX = 0, actorY = 0, angle = 0;
        public MyActor(){
            texture = new Texture(FNGuiTexture);

            addListener(new InputListener(){
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    ScreenManager.getInstance().showScreen( ScreenEnum.GAME);
                    return false;
                }
            });
        }
        public void draw(Batch batch, float alpha){
            batch.setColor(1,1,1,1);
            batch.draw(texture, actorX + getCamera().viewportWidth/2-(596/2),actorY+ getCamera().viewportHeight-296,0,242,600,280);
//            batch.draw(texture,actorX+ Gdx.graphics.getWidth()/2-texture.getWidth()/2,actorY+Gdx.graphics.getHeight()-texture.getHeight()-20);
//            actorX += Math.cos(angle) * 10;//20;
//            angle += 0.1f;
        }
        @Override
        public void act(float delta){
            super.act(delta);
        }


    }
}


