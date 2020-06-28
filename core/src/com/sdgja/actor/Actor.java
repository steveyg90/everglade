//   █████╗  ██████╗████████╗ ██████╗ ██████╗
//  ██╔══██╗██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗
//  ███████║██║        ██║   ██║   ██║██████╔╝
//  ██╔══██║██║        ██║   ██║   ██║██╔══██╗
//  ██║  ██║╚██████╗   ██║   ╚██████╔╝██║  ██║
//  ╚═╝  ╚═╝ ╚═════╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝
//

package com.sdgja.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.*;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.gdx.Drawer;
import com.brashmonkey.spriter.gdx.Loader;
import com.sdgja.camera.Camera;
import com.sdgja.map.IMap;
import com.sdgja.utils.PlayerAttributes;


abstract public class Actor  {

    protected  SpriteBatch _batch=null;  // only one of these
    protected boolean horizontalFacingRight = true;

    protected Vector2 position;

    protected Player spriter, monster;
    protected Drawer drawer;
    protected Loader loader;
    protected ShaderProgram shader;
    protected Camera camera;
    protected ShapeRenderer renderer;
    protected Data data;
    protected SCMLReader reader;

    public float xOffset = 0, yOffset = 0;

    public Actor(ShaderProgram shader, String scml, String animFrame, Vector2 position, Camera camera) {

       // if(_batch==null)
            _batch=new SpriteBatch();

        this.shader = shader;
        this.position = position;

        create(scml);

        spriter = new Player(data.getEntity(0));
        spriter.setAnimation(animFrame);
        this.camera = camera;

        spriter.characterMaps = new Entity.CharacterMap[PlayerAttributes.bodyTypes.length];
    }

    public void create(String scml) {
      //  renderer = new ShapeRenderer();
        FileHandle scmlHandle = Gdx.files.internal(scml);
        reader = new SCMLReader(scmlHandle.read());
        data = reader.getData();
        loader = new Loader(data);
        loader.load(scmlHandle.file());
        drawer = new Drawer(loader, _batch, null);
    }

    public void drawRectangle() {
        Rectangle r = spriter.getBoudingRectangle(null);
        drawer.rectangle(r.left,r.top,r.right-r.left,r.bottom-r.top);

    }
    public com.brashmonkey.spriter.Player getPlayer() {
        return this.spriter;
    }

    protected void update(Camera camera) {
        camera.update();
        spriter.update();
    }

    public void draw() {
        update(camera);
        _batch.begin();
        _batch.setShader(shader);
        drawer.draw(spriter);
        _batch.end();
    }

    public Vector2 getPlayerPosition() {
        return position;
    }

    protected float getX() {
        return position.x;
    }

    protected void setX(float x) {
        position.x = x;
    }

    protected float getY() {
        return position.y;
    }

    protected void setY(float y) {
        position.y = y;
    }

    public abstract void poll();

    public Player getSpriter() { return this.spriter;}
}