package com.sdgja.shader;

import com.badlogic.gdx.Gdx;

public class NightShader extends Shader {

    private float nightCol;

    public NightShader(String vertexShaderFile, String fragmentShaderFile,float nightCol) {
        super(vertexShaderFile, fragmentShaderFile);
        this.nightCol=nightCol;
    }

    public boolean compileShader() {
        boolean compiled = super.compileShader();
        if(!compiled) {
            Gdx.app.error("NightShader", shader.getLog());
        } else
        {
            shader.begin();
            shader.setUniformf("ambientColor", this.nightCol, this.nightCol, this.nightCol, this.nightCol);
            shader.end();
        }
        return compiled;
    }
}
