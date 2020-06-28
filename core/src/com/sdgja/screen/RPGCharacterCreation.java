package com.sdgja.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.gdx.Drawer;
import com.brashmonkey.spriter.gdx.Loader;
import com.sdgja.filesystem.IFileName;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.utils.PlayerAttributes;

import java.util.ArrayList;
import java.util.List;

public class RPGCharacterCreation extends AbstractScreen {

    private ImageButton backbutton, nextbutton, headleft,headright, torsoleft,torsoright, pantsleft,pantsright;

    private Player player;
    private Drawer drawer;
    private Loader loader;
    private Data data;
    private SCMLReader reader;
    private SpriteBatch batch;
    private Entity entity;
    private Texture fontTexture;

    public RPGCharacterCreation() {

        super();
        PlayerAttributes.head = 0;
    }


    private Texture box;

    private int headAmount = 3, whichhead = 0;
    private int torsoAmount = 2, whichtorso = 0;

    @Override
    public void buildStage() {

        fontTexture = new Texture(IFileName.FNFont);
        fontTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        box = new Texture(IFileName.ROOT + "/buttons/box.png");

        // Set up the buttons
        backbutton = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/63.png"));
        backbutton.setPosition(0,0);
        backbutton.addListener(UIFactory.createListener(ScreenEnum.TITLE));
        UIFactory.setBounds(backbutton);
        addActor(backbutton);

        nextbutton = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/62.png"));
        nextbutton.setPosition(Gdx.graphics.getWidth()-nextbutton.getWidth(),0);
        nextbutton.addListener(UIFactory.createListener(ScreenEnum.GAME));
        UIFactory.setBounds(nextbutton);
        addActor(nextbutton);

        batch = new SpriteBatch();

        create();  // create player sprite

        headleft = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/arrowleft.png"));
        headleft.setPosition(player.getX()-100,player.getY()-64);
        UIFactory.setBounds(headleft);
        headleft.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
             //   if(whichhead>0) whichhead--;
                {

                    setBody("head",whichhead);
                    PlayerAttributes.head = whichhead;
                }
                return false;
            }
        });
        addActor(headleft);

        headright = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/arrowright.png"));
        headright.setPosition(player.getX() + 70,headleft.getY());
        UIFactory.setBounds(headright);
        headright.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                      if(setBody("head",++whichhead)!=null)
                         PlayerAttributes.head = whichhead;

                    else
                        whichhead=0;
                return false;
            }
        });
        addActor(headright);

        torsoright = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/arrowright.png"));
        torsoright.setPosition(player.getX() + 70,headright.getY() - 30);
        UIFactory.setBounds(torsoright);
        torsoright.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                setBody("torso",1);
                return false;
            }
        });
        addActor(torsoright);
        torsoleft = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/arrowleft.png"));
        torsoleft.setPosition(headleft.getX() ,torsoright.getY() );
        UIFactory.setBounds(torsoleft);
        torsoleft.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                setBody("torso",0);
                return false;
            }
        });
        addActor(torsoleft);

        pantsright = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/arrowright.png"));
        pantsright.setPosition(headright.getX(),torsoright.getY() - 30);
        UIFactory.setBounds(torsoright);
        pantsright.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return false;
            }
        });
        addActor(pantsright);
        pantsleft = UIFactory.createButton(new Texture(IFileName.ROOT + "/buttons/arrowleft.png"));
        pantsleft.setPosition(headleft.getX() ,pantsright.getY() );
        UIFactory.setBounds(torsoleft);
        pantsleft.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return false;
            }
        });
        addActor(pantsleft);

        // Labels (head, torso, pants...)
        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = new BitmapFont();//Gdx.files.internal("assets/fonts/fontone.ttf"));

        label1Style.fontColor = Color.RED;
        com.badlogic.gdx.scenes.scene2d.ui.Label labelHead = new Label("Head",label1Style);
        labelHead.setPosition(headleft.getX() + 82,headleft.getY());
        addActor(labelHead);

        com.badlogic.gdx.scenes.scene2d.ui.Label labelTorso = new Label("Torso",label1Style);
        labelTorso.setPosition(torsoleft.getX() + 82,torsoleft.getY());
        addActor(labelTorso);

        com.badlogic.gdx.scenes.scene2d.ui.Label labelPants = new Label("Pants",label1Style);
        labelPants.setPosition(pantsleft.getX() + 82,pantsleft.getY());
        addActor(labelPants);

    }

    public Entity.CharacterMap setplayer(String s){
        return player.getEntity().getCharacterMap(s);
    //    return player.characterMap = entity.getCharacterMap(s);
    }
    public Entity.CharacterMap setBody(String s, int index) {
        return player.getEntity().getCharacterMap(s+index);// = entity.getCharacterMap(s+index);// characterMap.stream().filter(p->p.name.equals(part)).findAny().orElse(null);
    }

    public void create() {
      //  FileHandle scmlHandle = Gdx.files.internal("./core/assets/PlayerSprite/character.scml" );
        FileHandle scmlHandle = Gdx.files.internal("./core/assets/RPGPlayer/Male_48.scml" );
        reader = new SCMLReader(scmlHandle.read());
        data = reader.getData();
        loader = new Loader(data);
        loader.load(scmlHandle.file());
        drawer = new Drawer(loader, batch, null);
        player = new Player(data.getEntity(0));
        player.setAnimation("Walk_leftward");

        player.setPosition(Gdx.graphics.getWidth()/2 - 16, Gdx.graphics.getHeight()-200);

        // setup character map
        entity  = data.getEntity(0);

    }

    private final String[] animations = {"Walk_leftward","Walk_rightward"};//,"Walk_rightward"};// {"idle","walk","jump_up","jump_fall"};
    private int animCounter = 0, timeToChangeAnim = 0;

    public void render(float delta) {
        super.render(delta);
        batch.begin();
        player.update();
        batch.draw(box, player.getX()-40,player.getY()-30);
        drawer.draw(player);
        batch.end();

        player.setAnimation(animations[animCounter]);
        if(timeToChangeAnim>100) {
            if(animCounter<animations.length-1)
                animCounter++;
            else
                animCounter = 0;
            timeToChangeAnim = 0;
        } else {
            timeToChangeAnim++;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
