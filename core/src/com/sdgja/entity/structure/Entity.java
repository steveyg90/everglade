//  ███████╗███╗   ██╗████████╗██╗████████╗██╗   ██╗
//  ██╔════╝████╗  ██║╚══██╔══╝██║╚══██╔══╝╚██╗ ██╔╝
//  █████╗  ██╔██╗ ██║   ██║   ██║   ██║    ╚████╔╝
//  ██╔══╝  ██║╚██╗██║   ██║   ██║   ██║     ╚██╔╝
//  ███████╗██║ ╚████║   ██║   ██║   ██║      ██║
//  ╚══════╝╚═╝  ╚═══╝   ╚═╝   ╚═╝   ╚═╝      ╚═╝
//

package com.sdgja.entity.structure;

import com.sdgja.tmx.TmxRenderer;

public class Entity  {
    final int growthSpeed = 20; // Base speed for building growth, use multiplier on this value

    protected boolean active;
    protected IFunc action;
    protected int x, y;
    protected int[][] structureMap;
    protected String name, xmlFilename;


    public Entity(String name, int x, int y, short[][] map) {
        this.name = name;
        this.active = true;
        this.x=x;
        this.y=y;
        TmxRenderer.readAndParse(this.name,map,this.x,this.y);
        structureMap = TmxRenderer.getStructureMap(this.name);
    }

    protected int[][]getStructureMap(String name) {
        return TmxRenderer.getStructureMap(name);
    }


    public boolean isActive() {
        return this.active;
    }

    public void addAction(IFunc action) {
        this.action = action;
    }

    public void start() {
        action.play();
    }

    public String getName() { return this.name; }
}


