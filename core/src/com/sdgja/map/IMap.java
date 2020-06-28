//  ██╗███╗   ███╗ █████╗ ██████╗
//  ██║████╗ ████║██╔══██╗██╔══██╗
//  ██║██╔████╔██║███████║██████╔╝
//  ██║██║╚██╔╝██║██╔══██║██╔═══╝
//  ██║██║ ╚═╝ ██║██║  ██║██║
//  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝
//

// Map dimensions

// Z +----------------------------------------+ A
//   |                                        |
//   |                                        |
//   +----------------------------------------+ B
//   |                                        |
//   |                                        |
//   +----------------------------------------+ C
//   |                                        |
//   |                                        |
//   |                                        |
//   |                                        |
//   |                                        |
//   |                                        |
//   |                                        |
//   +----------------------------------------+ D

// A to B = Platform height
// B to C = Terratin height
// A to D = height
// Z to A = width

package com.sdgja.map;
public interface IMap {

     boolean bMenuScreens = !false;      // false = skip title screen

     String VERSION = "0.1";            // Master version number
     String RAYCASTVERSION = "0.3";
     String TERRAINVERSION = "0.1";
     String GUIVERSION = "0.1";

     int PLATFORM_HEIGHT = 300;         // Number of tiles high for platform level at top of map
     int TERRAIN_HEIGHT = 110;          // Number of tiles high for terrain under platform area

     boolean DEBUG_MODE = true;         // false = normal mode, no debugging or cheats enabled/

     int WIDTH = 4000;                  // Width of map in tiles
     int HEIGHT = 1000;
     int SMOOTH = 6;                    // Used for cave generation
     int TILEWIDTH = 8;                 // Pixel dimensions of tiles
     int TILEHEIGHT = 8;

     float ZOOM = 0.50f;                // Zoom factor

// Used for fading clouds out, coal face in and darkening the caves for the raycasting.
     int TOPFADELIMIT = (((IMap.PLATFORM_HEIGHT-(256/8)) +IMap.TERRAIN_HEIGHT)* IMap.TILEHEIGHT);
     int BOTTOMFADELIMIT = ((TOPFADELIMIT-(256/8))+(8* IMap.TILEHEIGHT));

     String charset = ".!.#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ......abcdefghijklmnopqrstuvwxyz";

     int FONTWIDTH = 8;
     int FONTHEIGHT = 16;


}
