
//  ████████╗███████╗██╗  ██╗████████╗██╗███╗   ██╗██████╗ ██╗   ██╗████████╗
//  ╚══██╔══╝██╔════╝╚██╗██╔╝╚══██╔══╝██║████╗  ██║██╔══██╗██║   ██║╚══██╔══╝
//     ██║   █████╗   ╚███╔╝    ██║   ██║██╔██╗ ██║██████╔╝██║   ██║   ██║
//     ██║   ██╔══╝   ██╔██╗    ██║   ██║██║╚██╗██║██╔═══╝ ██║   ██║   ██║
//     ██║   ███████╗██╔╝ ██╗   ██║   ██║██║ ╚████║██║     ╚██████╔╝   ██║
//     ╚═╝   ╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝  ╚═══╝╚═╝      ╚═════╝    ╚═╝
//

package com.sdgja.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class TextInput implements Input.TextInputListener {

    private String text;
    @Override
    public void input(String text) {
        Gdx.app.log("Debug",text);
        this.text = text;

    }

    @Override
    public void canceled() {

    }

    public String getText(){
        return text;
    }
}
