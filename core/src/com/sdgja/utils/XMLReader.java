//  ██╗  ██╗███╗   ███╗██╗     ██████╗ ███████╗ █████╗ ██████╗ ███████╗██████╗
//  ╚██╗██╔╝████╗ ████║██║     ██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝██╔══██╗
//   ╚███╔╝ ██╔████╔██║██║     ██████╔╝█████╗  ███████║██║  ██║█████╗  ██████╔╝
//   ██╔██╗ ██║╚██╔╝██║██║     ██╔══██╗██╔══╝  ██╔══██║██║  ██║██╔══╝  ██╔══██╗
//  ██╔╝ ██╗██║ ╚═╝ ██║███████╗██║  ██║███████╗██║  ██║██████╔╝███████╗██║  ██║
//  ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═════╝ ╚══════╝╚═╝  ╚═╝
//

package com.sdgja.utils;

import com.sdgja.filesystem.IFileName;
import java.io.*;
public final class XMLReader {
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static byte[] tileAttributes = new byte[32768];  // 16384 tiles (128x128) - 2 bytes per character

    public static void loadTileBinary() {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(IFileName.FNTileAttributes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            inputStream.read(tileAttributes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getTileId(int tile) {
        return (int) (tileAttributes[(tile * 2) + 1] * 256) + (tileAttributes[tile * 2]);

    }
}
