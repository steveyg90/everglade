
//  ███╗   ███╗ ██████╗ ███╗   ██╗███████╗████████╗███████╗██████╗
//  ████╗ ████║██╔═══██╗████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
//  ██╔████╔██║██║   ██║██╔██╗ ██║███████╗   ██║   █████╗  ██████╔╝
//  ██║╚██╔╝██║██║   ██║██║╚██╗██║╚════██║   ██║   ██╔══╝  ██╔══██╗
//  ██║ ╚═╝ ██║╚██████╔╝██║ ╚████║███████║   ██║   ███████╗██║  ██║
//  ╚═╝     ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
//

package com.sdgja.GameSprite;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.sdgja.actor.Actor;
import com.sdgja.camera.Camera;

public class Monster extends Actor {
    public Monster(ShaderProgram shader, String scml, String animFrame, Vector2 position, Camera camera) {
        super(shader, scml, animFrame, position, camera);
    }

  /*  @Override
    public void draw() {
        update(camera);
        _batch.begin();
        _batch.setShader(shader);
        drawer.draw(spriter);
        _batch.end();
    }*/

    @Override
    public void poll() {

    }
}
