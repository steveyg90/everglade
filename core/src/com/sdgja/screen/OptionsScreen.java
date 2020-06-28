package com.sdgja.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.sdgja.filesystem.IFileName;
import com.sdgja.screen.AbstractScreen;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.sdgja.screen.ScreenEnum;
import com.sdgja.screen.UIFactory;
import com.sdgja.utils.GlobalVar;
import javafx.animation.FadeTransition;

import static com.sdgja.filesystem.IFileName.FNGuiTexture;
import static com.sdgja.filesystem.IFileName.ROOT;


public class OptionsScreen extends AbstractScreen {

    private ImageButton backbutton;
    private Image logo;
    private Texture logoTexture;

    private Label resLabel, worldSize;
    private SelectBox<String> resSelectBox, worldSizeBox;

    private String[] resolutions = { "1024 x 768", "1280 x 900"};
    private String[] worldSizes = {"Small", "Medium", "Large"};

    @Override
    public void buildStage() {
        //  skin = new Skin(Gdx.files.absolute(IFileName.ROOT+"/skins/star-soldier/star-soldier-ui.json"));
        skin = new Skin(Gdx.files.absolute(IFileName.ROOT+"/skins/shade/uiskin.json"));

        logoTexture = new Texture(ROOT + "/Artwork/logo.png");
        logo = new Image(logoTexture);
        logo.setPosition(GlobalVar.coreX/2 - logoTexture.getWidth()/2,GlobalVar.coreY - logoTexture.getHeight());
        addActor(logo);

        int X_left= Gdx.graphics.getWidth()/3-logoTexture.getWidth()/2;
        int X_right = Gdx.graphics.getWidth()*2/3-logoTexture.getWidth()/2;
        int Y_top = Gdx.graphics.getHeight()*2/3-logoTexture.getHeight()/2;
        int Y_bottom = Gdx.graphics.getHeight()/3-logoTexture.getHeight()/2;
        ParallelAction topLeftRightParallelAction = new ParallelAction();
        topLeftRightParallelAction.addAction(Actions.moveTo(X_right,Y_top,1,Interpolation.exp5Out));
        topLeftRightParallelAction.addAction(Actions.scaleTo(2,2,1,Interpolation.exp5Out));

        MoveToAction moveBottomRightAction = new MoveToAction();
        moveBottomRightAction.setPosition(X_right,Y_bottom);
        moveBottomRightAction.setDuration(1);
        moveBottomRightAction.setInterpolation(Interpolation.smooth);

        ParallelAction bottomLeftRightParallelAction = new ParallelAction();
        bottomLeftRightParallelAction.addAction(Actions.moveTo(X_left,Y_bottom,1,Interpolation.sineOut));
        bottomLeftRightParallelAction.addAction(Actions.scaleTo(1,1,1));

        ParallelAction leftBottomTopParallelAction = new ParallelAction();
        leftBottomTopParallelAction.addAction(Actions.moveTo(X_left,Y_top,1, Interpolation.swingOut));
        leftBottomTopParallelAction.addAction(Actions.rotateBy(90,1));

        SequenceAction overallSequence = new SequenceAction();
        overallSequence.addAction(topLeftRightParallelAction);
        overallSequence.addAction(moveBottomRightAction);
        overallSequence.addAction(bottomLeftRightParallelAction);
        overallSequence.addAction(leftBottomTopParallelAction);

        RepeatAction infiniteLoop = new RepeatAction();
        infiniteLoop.setCount(RepeatAction.FOREVER);
        infiniteLoop.setAction(overallSequence);
        logo.addAction(infiniteLoop);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setColor(Color.GREEN);
//        rootTable.setDebug(true);
        rootTable.columnDefaults(1).width(400);
        addActor(rootTable);

        rootTable.row().height(50);
        resLabel = new Label("Resolution", skin);
        rootTable.add(resLabel).padRight(20);
        resSelectBox = new SelectBox<String>(skin);
        resSelectBox.setItems(resolutions);
        rootTable.add(resSelectBox);

        rootTable.row().height(50);
        worldSize = new Label("World Size", skin);
        rootTable.add(worldSize).pad(20);
        worldSizeBox = new SelectBox<String>(skin);
        worldSizeBox.setItems(worldSizes);
        rootTable.add(worldSizeBox);



        backbutton = UIFactory.createButton(new Texture(IFileName.FNOkButton));
        backbutton.setPosition(getCamera().viewportWidth-backbutton.getWidth() - 32,32);
        backbutton.addListener(UIFactory.createListener(ScreenEnum.TITLE));
        UIFactory.setBounds(backbutton);
        addActor(backbutton);

    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.act();
        super.draw();
    }

}
