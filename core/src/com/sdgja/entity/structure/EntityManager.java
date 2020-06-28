
//  ███████╗███╗   ██╗████████╗██╗████████╗██╗   ██╗███╗   ███╗ █████╗ ███╗   ██╗ █████╗  ██████╗ ███████╗██████╗
//  ██╔════╝████╗  ██║╚══██╔══╝██║╚══██╔══╝╚██╗ ██╔╝████╗ ████║██╔══██╗████╗  ██║██╔══██╗██╔════╝ ██╔════╝██╔══██╗
//  █████╗  ██╔██╗ ██║   ██║   ██║   ██║    ╚████╔╝ ██╔████╔██║███████║██╔██╗ ██║███████║██║  ███╗█████╗  ██████╔╝
//  ██╔══╝  ██║╚██╗██║   ██║   ██║   ██║     ╚██╔╝  ██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║██║   ██║██╔══╝  ██╔══██╗
//  ███████╗██║ ╚████║   ██║   ██║   ██║      ██║   ██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║╚██████╔╝███████╗██║  ██║
//  ╚══════╝╚═╝  ╚═══╝   ╚═╝   ╚═╝   ╚═╝      ╚═╝   ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝
//

package com.sdgja.entity.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public final class EntityManager {

    private static List<Entity> entityList;   // holds all entities
    private static HashMap<String, Integer > entMap;
    static {
        entityList = new ArrayList<>();
        entMap = new HashMap<>();
    }

    public static void removeEntity(Entity entity) {
        if(entityList.contains(entity)) {
            entityList.remove(entity);
        }
        if (entMap.containsKey(entity.name)) {
            Integer count = entMap.get(entity.name);
            count--;
            if(count<=0) { // we remove when no more of this entity
                entMap.remove(entity.name);
            } else {
                entMap.put(entity.name, count);  // update the count
            }
        }
    }

    public static void removeAll() {
        entMap.clear();
        entityList.clear();
    }
    public static HashMap<String, Integer > getEntityMap() {
        return entMap;
    }

    public static void addEntity(Entity entity) {
        if(!entMap.containsKey(entity.name)) {
            entMap.put(entity.name, 1);
        } else { // exists, so just add another to this key
            Integer count = entMap.get(entity.name);
            count++;
            entMap.put(entity.name, count);
        }

        entityList.add(entity);
    }

    public static void playAll() {
        for(int i = 0; i<entityList.size(); i++) {
            if(entityList.get(i).isActive()) {
                entityList.get(i).start();
            }
        }
        /*
        for(Entity e : entityList) {
            if(e.isActive()) {
                e.start();
            }
        }*/
    }

    public static void playLoop(Entity entity) {
        if(entity.isActive())
            entity.start();
    }

    public static List<Entity> getEntities() {
        return entityList;
    }
}
