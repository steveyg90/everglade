package com.sdgja.intohell.desktop;

		import com.badlogic.gdx.ApplicationListener;
		import com.badlogic.gdx.Gdx;
		import com.badlogic.gdx.Graphics;
		import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
		import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
		import com.sdgja.intohell.IntoHellGame;
		import com.sdgja.intohell.MechaFirma;
		import com.sdgja.map.IMap;
		import com.sdgja.utils.GlobalVar;
//		import org.lwjgl.opengl.DisplayMode;

		import static com.sdgja.utils.GlobalVar.*;

public class DesktopLauncher {
//	private Graphics.DisplayMode[] modes;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		Graphics.DisplayMode[] modes = LwjglApplicationConfiguration.getDisplayModes();

		for (int i=0; i<modes.length; i++){
			System.out.println(modes[i]);
		}

// ** Jays Home Monitor (1920x1200)
//  0 - 640 x 480 					Too small
//  1 - 800 x 600 					Too small
//  2 - 1024 x 768 					(sinks into map) + too small?
//  3 - 1280 x 720 ok
//  4 - 1280 x 800 (sinks into map)
//  5 - 1280 x 1024 (sinks into map)
//  6 - 1366 x 768 (sinks into map) + screen edges
//  7 - 1440 x 900 (sinks into map)
//  8 - 1600 x 900 (sinks into map)
//  9 - 1600 x 1200 ok
// 10 - 1680 x 1050 screen edges
// 11 - 1920 x 1080 ok
// 12 - 1920 x 1200

// ** Jays Work Monitor (1920x1080)
//	0	640x480, bpp: 32, hz: 50
//	1	720x480, bpp: 32, hz: 50
//	2	720x576, bpp: 32, hz: 50
//	3	800x600, bpp: 32, hz: 50
//	4	1024x768, bpp: 32, hz: 50
//	5	1280x720, bpp: 32, hz: 50
//	6	1280x800, bpp: 32, hz: 50
//	7	1280x1024, bpp: 32, hz: 50
//	8	1366x768, bpp: 32, hz: 50
//	9	1600x900, bpp: 32, hz: 50
//	10	1680x1050, bpp: 32, hz: 50
//	11	1920x1080, bpp: 32, hz: 50

// **** Jays Laptop (1920x1080)
//	0	640x480, bpp: 32, hz: 60 	Too small
//	1	800x600, bpp: 32, hz: 60	Too small
//	2	1024x768, bpp: 32, hz: 60
//	3	1280x1024, bpp: 32, hz: 60
//	4	1366x768, bpp: 32, hz: 60
//	5	1600x900, bpp: 32, hz: 60
//	6	1920x1080, bpp: 32, hz: 60

// **** Ste's Desktop (1920x1080)
//   0 640x480, bpp: 32, hz: 59
//   1 720x400, bpp: 32, hz: 59
//	 2 720x480, bpp: 32, hz: 59
//	 3 720x576, bpp: 32, hz: 50
//   4 800x600, bpp: 32, hz: 56
//   5 832x624, bpp: 32, hz: 59
//	 6 1024x768, bpp: 32, hz: 60
//	 7 1152x864, bpp: 32, hz: 59
//	 8 1152x872, bpp: 32, hz: 59
//	 9 1280x720, bpp: 32, hz: 50
//  10 1280x800, bpp: 32, hz: 60
//	11 1280x960, bpp: 32, hz: 60
//	12 1280x1024, bpp: 32, hz: 60
//	13 1440x900, bpp: 32, hz: 60
//	14 1600x900, bpp: 32, hz: 60
//	15	1680x1050, bpp: 32, hz: 60
//	16	1920x1080, bpp: 32, hz: 25

// **** Stes Laptop (1920x1080)
//	0	640x480, bpp: 32, hz: 60 	Too small
//	1	800x600, bpp: 32, hz: 60	Too small
//	2	1024x768, bpp: 32, hz: 60
//	3	1280x1024, bpp: 32, hz: 60
//	4	1366x768, bpp: 32, hz: 60
//	5	1600x900, bpp: 32, hz: 60
//	6	1920x1080, bpp: 32, hz: 60

		GlobalVar.displayMode=modes[5];

//		Graphics.DisplayMode displayMode = GlobalVar.displayMode;
		config.setFromDisplayMode(GlobalVar.displayMode);
		config.width = GlobalVar.coreX = GlobalVar.displayMode.width;
		config.height = GlobalVar.coreY = GlobalVar.displayMode.height;

		config.title = "Everglade";
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.resizable = false;
		config.vSyncEnabled = true;
		config.fullscreen = GlobalVar.fullscreen = !true;

		new LwjglApplication(new MechaFirma(),config);
	}
}
