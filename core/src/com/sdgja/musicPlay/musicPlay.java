
//  ███╗   ███╗██╗   ██╗███████╗██╗ ██████╗██████╗ ██╗      █████╗ ██╗   ██╗
//  ████╗ ████║██║   ██║██╔════╝██║██╔════╝██╔══██╗██║     ██╔══██╗╚██╗ ██╔╝
//  ██╔████╔██║██║   ██║███████╗██║██║     ██████╔╝██║     ███████║ ╚████╔╝ 
//  ██║╚██╔╝██║██║   ██║╚════██║██║██║     ██╔═══╝ ██║     ██╔══██║  ╚██╔╝  
//  ██║ ╚═╝ ██║╚██████╔╝███████║██║╚██████╗██║     ███████╗██║  ██║   ██║   
//  ╚═╝     ╚═╝ ╚═════╝ ╚══════╝╚═╝ ╚═════╝╚═╝     ╚══════╝╚═╝  ╚═╝   ╚═╝   
//                                                                          
package com.sdgja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import static com.sdgja.filesystem.IFileName.*;
/////////////////////////////////////////////////////////////////////////////
public class musicPlay {

    private static Music myMusic=null;
    private static int currentTrack = 0;
    private static String[] trackList = {
            FNMusic0,FNMusic1,FNMusic2,FNMusic3,FNMusic4,FNMusic5,
            FNMusic6,FNMusic7,FNMusic8,FNMusic9,FNMusic10,FNMusic11
            };

/////////////////////////////////////////////////////////////////////////////

    public static void initMusic() {
        currentTrack=0;
        playMusic(trackList[0]);
    }

/////////////////////////////////////////////////////////////////////////////
    
    public static void playMusic(String name) {
        if (myMusic!=null){
            myMusic.stop();
        }
        myMusic = Gdx.audio.newMusic(Gdx.files.internal(name));
        myMusic.setVolume(0.0f);
        myMusic.setLooping(true);
        myMusic.play();
    }

/////////////////////////////////////////////////////////////////////////////
    
    public static void nextMusicTrack(){
        currentTrack++;
        if (currentTrack>=trackList.length){
            currentTrack=0;
        }
        playMusic(trackList[currentTrack]);
    }

/////////////////////////////////////////////////////////////////////////////
    
    public static void prevMusicTrack(){
        currentTrack--;
        if (currentTrack<0){
            currentTrack=trackList.length-1;
        }
        playMusic(trackList[currentTrack]);
    }

/////////////////////////////////////////////////////////////////////////////
    
    public static int getMusicName(){ return currentTrack;}

}
