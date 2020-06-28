package com.sdgja.utils;

public class GetOS {
    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS
    };// Operating systems.

/*
Usage:
		switch (GetOS.getOS()) {
			case WINDOWS:
				OSstring = OSstring + "Windows";
				break;
			case MAC:
				OSstring = OSstring + "Mac";
				break;
			case LINUX:
				OSstring = OSstring + "Linux";
				break;
			case SOLARIS:
				OSstring = OSstring + "Solaris";
				break;
			default:
				break;
		};

 */

    private static OS os = null;

    public static OS getOS() {
        if (os == null) {
            String operSys = System.getProperty("os.name").toLowerCase();
            if (operSys.contains("win")) {
                os = OS.WINDOWS;
            } else if (operSys.contains("nix") || operSys.contains("nux")
                    || operSys.contains("aix")) {
                os = OS.LINUX;
            } else if (operSys.contains("mac")) {
                os = OS.MAC;
            } else if (operSys.contains("sunos")) {
                os = OS.SOLARIS;
            }
        }
        return os;
    }
}
