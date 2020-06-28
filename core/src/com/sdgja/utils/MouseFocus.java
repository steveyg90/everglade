package com.sdgja.utils;

public final class MouseFocus {

    public static enum WindowFocus {
        Main,
        Gui,
        InputWindow,
        DebugWindow,
        Console,
    }
    private static WindowFocus focus;  // true=gameworld, false=gui

    public static void setFocus(WindowFocus f) {
        focus = f;
    }
    public static WindowFocus getFocus() {
        return focus;
    }
    static {
        focus = WindowFocus.Main;
    }

}
