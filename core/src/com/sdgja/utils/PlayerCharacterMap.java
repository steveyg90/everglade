package com.sdgja.utils;

import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// Our character map class - makes using character maps easier
public class PlayerCharacterMap  {
    private static HashMap<String, ArrayList<Entity.CharacterMap>> playerCharacterMap=new HashMap<>();

    public static void createCharacterMaps(final String[]bodyTypes, Player player) {
        Entity.CharacterMap[] cm =  player.getEntity().getCharacterMap();
        for(int j=0;j<bodyTypes.length;j++) {
            for (int i = 0; i < cm.length; i++) {
                if (cm[i].name.contains(bodyTypes[j])) {
                    PlayerCharacterMap.addMap(bodyTypes[j],player.getEntity().getCharacterMap(cm[i].name));
                }
            }
        }
    }
    // Add a character map
    public static void addMap(String type, Entity.CharacterMap characterMap) {
        ArrayList<Entity.CharacterMap> cmap = playerCharacterMap.get(type);
        if(cmap==null) { // first of this type
            playerCharacterMap.put(type, new ArrayList<Entity.CharacterMap>() ) ;
        }
        cmap = playerCharacterMap.get(type);  // get the body ty[e
        cmap.add(characterMap); // add character map to it

    }

    public static Entity.CharacterMap getCharMap(String type, int index) {
        ArrayList<Entity.CharacterMap> pcm = playerCharacterMap.get(type);
        if(pcm!=null)
            return pcm.get(index);
        return null;
    }

    // Get amount of character maps associated with this body part
    public static int getAmount(String type) {
        return playerCharacterMap.get(type).size()-1;
    }
}