package com.sdgja.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.gdx.Drawer;
import com.brashmonkey.spriter.gdx.Loader;
import com.sdgja.filesystem.IFileName;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.utils.PlayerAttributes;
import com.sdgja.utils.*;
import static com.sdgja.map.IMap.charset;
import static com.sdgja.map.IMap.FONTWIDTH;
import static com.sdgja.map.IMap.FONTHEIGHT;
import static com.sdgja.filesystem.IFileName.FNGuiTexture;


public class CharacterCreation extends AbstractScreen {

    public static PlayerCharacterMap PlayerCharacterMap;
    private ImageButton backbutton, nextbutton, headleft,headright, torsoleft,torsoright, pantsleft,pantsright;
    private Player player;
    private Drawer drawer;
    private Loader loader;
    private Data data;
    private SCMLReader reader;
    private SpriteBatch batch;
    private Entity entity;
    private Texture fontTexture, texture;
    private final String[] animations = {"idle","walk","jump_up","jump_fall"};
    private int animCounter = 0, timeToChangeAnim = 0;
    private Texture box;

    final int VERTICALGAP = 16;

    public CharacterCreation() {
        super();
        PlayerAttributes.head = 0;
        PlayerAttributes.torso = 0;
    }

    @Override
    public void buildStage() {
        fontTexture = new Texture(IFileName.FNFont);
        fontTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        box = new Texture(FNGuiTexture);;

        // Set up the buttons
        nextbutton = UIFactory.createButton(new Texture(IFileName.FNOkButton));
        nextbutton.setPosition(getCamera().viewportWidth-nextbutton.getWidth() - 32,32);
        nextbutton.addListener(UIFactory.createListener(ScreenEnum.GAME));
  //      nextbutton.addListener(UIFactory.addOnMoveListener(nextbutton));
  //      nextbutton.addListener(UIFactory.addOnMoveExit(nextbutton));
  //      nextbutton.setColor(1,1,1,1f);
        UIFactory.setBounds(nextbutton);
        addActor(nextbutton);

        backbutton = UIFactory.createButton(new Texture(IFileName.FNCancelButton));
        backbutton.setPosition(nextbutton.getX() - backbutton.getWidth() - 10,32);
        backbutton.addListener(UIFactory.createListener(ScreenEnum.TITLE));
        backbutton.addListener(UIFactory.addOnMoveListener(backbutton));
        backbutton.addListener(UIFactory.addOnMoveExit(backbutton));
        backbutton.setColor(1,1,1,0.5f);
        UIFactory.setBounds(backbutton);
        addActor(backbutton);

        batch = new SpriteBatch();

        create();  // create player sprite

        headleft = UIFactory.createButton(new Texture(IFileName.FNLeftArrow));
        headleft.setPosition(player.getX()-103,player.getY()-64);
        headleft.addListener(UIFactory.addOnMoveListener(headleft));
        headleft.addListener(UIFactory.addOnMoveExit(headleft));
        headleft.setColor(1,1,1,0.5f);

        UIFactory.setBounds(headleft);
        headleft.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if( PlayerAttributes.head>0) -- PlayerAttributes.head;
                    player.characterMaps[0] = PlayerCharacterMap.getCharMap("head", PlayerAttributes.head);
                return false;
            }
        });
        addActor(headleft);

        headright = UIFactory.createButton(new Texture(IFileName.FNRightArrow));
        headright.setPosition(player.getX() + 78,headleft.getY());
        headright.addListener(UIFactory.addOnMoveListener(headright));
        headright.addListener(UIFactory.addOnMoveExit(headright));
        headright.setColor(1,1,1,0.5f);

        UIFactory.setBounds(headright);
        headright.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                if( PlayerAttributes.head<PlayerCharacterMap.getAmount("head")) ++ PlayerAttributes.head;
                player.characterMaps[0] = PlayerCharacterMap.getCharMap("head", PlayerAttributes.head);

                return false;
            }
        });
        addActor(headright);

        torsoright = UIFactory.createButton(new Texture(IFileName.FNRightArrow));
        torsoright.setPosition(player.getX() + 78,headright.getY() - 30 - VERTICALGAP);
        torsoright.addListener(UIFactory.addOnMoveListener(torsoright));
        torsoright.addListener(UIFactory.addOnMoveExit(torsoright));
        torsoright.setColor(1,1,1,0.5f);
        UIFactory.setBounds(torsoright);
        torsoright.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if( PlayerAttributes.torso<PlayerCharacterMap.getAmount("torso")) ++ PlayerAttributes.torso;
                player.characterMaps[1] = PlayerCharacterMap.getCharMap("torso", PlayerAttributes.torso);

                return false;
            }
        });
        addActor(torsoright);
        torsoleft = UIFactory.createButton(new Texture(IFileName.FNLeftArrow));
        torsoleft.setPosition(headleft.getX() ,torsoright.getY() );
        torsoleft.addListener(UIFactory.addOnMoveListener(torsoleft));
        torsoleft.addListener(UIFactory.addOnMoveExit(torsoleft));
        torsoleft.setColor(1,1,1,0.5f);
        UIFactory.setBounds(torsoleft);
        torsoleft.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if( PlayerAttributes.torso>0) -- PlayerAttributes.torso;
                player.characterMaps[1] = PlayerCharacterMap.getCharMap("torso", PlayerAttributes.torso);

                return false;
            }
        });
        addActor(torsoleft);

        pantsright = UIFactory.createButton(new Texture(IFileName.FNRightArrow));
        pantsright.setPosition(headright.getX(),torsoright.getY() - 30 - VERTICALGAP);
        pantsright.addListener(UIFactory.addOnMoveListener(pantsright));
        pantsright.addListener(UIFactory.addOnMoveExit(pantsright));
        pantsright.setColor(1,1,1,0.5f);
        UIFactory.setBounds(torsoright);
        pantsright.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return false;
            }
        });
        addActor(pantsright);
        pantsleft = UIFactory.createButton(new Texture(IFileName.FNLeftArrow));
        pantsleft.setPosition(headleft.getX() ,pantsright.getY() );
        pantsleft.addListener(UIFactory.addOnMoveListener(pantsleft));
        pantsleft.addListener(UIFactory.addOnMoveExit(pantsleft));
        pantsleft.setColor(1,1,1,0.5f);
        UIFactory.setBounds(torsoleft);
        pantsleft.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return false;
            }
        });
        addActor(pantsleft);
    }

    private int getCharPos(char letter) {
        return charset.indexOf(letter)*FONTWIDTH;
    }

    public Entity.CharacterMap setBody(String s, int index) {
        return player.getEntity().getCharacterMap(s+index);// characterMap = entity.getCharacterMap(s+index);// characterMap.stream().filter(p->p.name.equals(part)).findAny().orElse(null);
    }

    public void create() {
        FileHandle scmlHandle = Gdx.files.internal(IFileName.FNPlayerSprite );

        this.texture = new Texture(FNGuiTexture);  // get texture which has font etc on it

        reader = new SCMLReader(scmlHandle.read());
        data = reader.getData();
        loader = new Loader(data);
        loader.load(scmlHandle.file());
        drawer = new Drawer(loader, batch, null);
        player = new Player(data.getEntity(0));
        player.setAnimation("idle");

        player.setPosition(Gdx.graphics.getWidth()/2 - 16, Gdx.graphics.getHeight()-200);

        // 2 as we have head and torso
        player.characterMaps = new Entity.CharacterMap[PlayerAttributes.bodyTypes.length];

        // Set up character map for player
        PlayerCharacterMap.createCharacterMaps(PlayerAttributes.bodyTypes,player);

        player.characterMaps[1] = PlayerCharacterMap.getCharMap("torso", PlayerAttributes.torso);
        player.characterMaps[0] = PlayerCharacterMap.getCharMap("head", PlayerAttributes.head);
    }


    String headText =  "HEAD";
    String torsoText = "BODY";
    String legText =   "LEGS";
    String nameText = "ENTER NAME";
    final String[] menuItems = {headText, torsoText, legText};

    private void drawBackgroundBox(boolean input, int len, float x, float y) {
        batch.draw(texture, x+.2f,y+.2f, 140, 0, 12,28 );
        int i=0;
        for(i=0; i<len;i++) {
            batch.draw(texture, x + 12.2f + (i*7),y+.2f, input ? 152 : 176, 0, 7,28 );
        }
        batch.draw(texture, x + ((i+1)*7),y+.2f, 160, 0, 12,28 );
    }

    public void render(float delta) {
        float y = 450;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.act();
        super.draw();

        batch.begin();
        player.update();
        batch.draw(box, player.getX()-40, player.getY()-30, 0, 96, 80, 126);
        drawer.draw(player);

        int offset;
        int yoffset = 0;
        for(offset=0;offset<5;offset++) {
            drawBackgroundBox(offset!=4 ? true : false,20, player.getX() - 100 + 17, player.getY() - (66 + yoffset + (offset * (30+VERTICALGAP))));
            yoffset += (offset==2) ? FONTHEIGHT + VERTICALGAP  : 0;
        }
        for(int i=0; i<nameText.length();i++)
            batch.draw(this.texture, 22 + getCamera().viewportWidth / 2 + (i * FONTWIDTH) - nameText.length() * FONTWIDTH, 288 , FONTWIDTH, FONTHEIGHT, getCharPos(nameText.charAt(i)), 224, FONTWIDTH, FONTHEIGHT, false, false);

        // Draw Text
        for(int j=0; j<menuItems.length;j++) {
            for (int i = 0; i < menuItems[j].length(); i++) {
                batch.draw(this.texture, getCamera().viewportWidth / 2 + (i * FONTWIDTH) - menuItems[j].length() * FONTWIDTH, y + FONTHEIGHT/2 , FONTWIDTH, FONTHEIGHT, getCharPos(menuItems[j].charAt(i)), 224, FONTWIDTH, FONTHEIGHT, false, false);
            }
            y-=30 + VERTICALGAP;
        }

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

