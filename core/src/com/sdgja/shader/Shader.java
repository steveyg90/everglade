package com.sdgja.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {

    protected ShaderProgram shader;

    public Shader(String vertexShaderFile, String fragmentShaderFile) {
        shader =  new ShaderProgram(vertexShaderFile, fragmentShaderFile);
        if (!shader.isCompiled()) {
            Gdx.app.error("NightShader", shader.getLog());
        } else {
            System.out.println("OK");
        }
    }

    public boolean compileShader() {
        return shader.isCompiled();
    }

}
