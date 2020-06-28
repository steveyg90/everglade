
//  ██╗  ██╗ █████╗ ████████╗
//  ██║  ██║██╔══██╗╚══██╔══╝
//  ███████║███████║   ██║
//  ██╔══██║██╔══██║   ██║
//  ██║  ██║██║  ██║   ██║
//  ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
//

package com.sdgja.GameSprite.apparel;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.sdgja.actor.Actor;
import com.sdgja.camera.Camera;

public class Hat extends Actor {
    public Hat(ShaderProgram shader, String scml, String animFrame, Vector2 position, Camera camera) {
        super(shader, scml, animFrame, position, camera);
    }

    @Override
    public void poll() {

    }

}


