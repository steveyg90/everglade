
//  ██████╗  █████╗ ███╗   ██╗ ██████╗ ███████╗██████╗ ██████╗  █████╗ ███╗   ██╗██████╗  ██████╗ ███╗   ███╗
//  ██╔══██╗██╔══██╗████╗  ██║██╔════╝ ██╔════╝██╔══██╗██╔══██╗██╔══██╗████╗  ██║██╔══██╗██╔═══██╗████╗ ████║
//  ██████╔╝███████║██╔██╗ ██║██║  ███╗█████╗  ██║  ██║██████╔╝███████║██╔██╗ ██║██║  ██║██║   ██║██╔████╔██║
//  ██╔══██╗██╔══██║██║╚██╗██║██║   ██║██╔══╝  ██║  ██║██╔══██╗██╔══██║██║╚██╗██║██║  ██║██║   ██║██║╚██╔╝██║
//  ██║  ██║██║  ██║██║ ╚████║╚██████╔╝███████╗██████╔╝██║  ██║██║  ██║██║ ╚████║██████╔╝╚██████╔╝██║ ╚═╝ ██║
//  ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝
//

package com.sdgja.utils;

import java.util.Random;
import java.util.SplittableRandom;

public final class RangedRandom {

    // Return random number between specified limits (inclusive)
    public static int generate (int lowLimit,int highLimit) {
        SplittableRandom r = new SplittableRandom();
        return r.nextInt(highLimit - lowLimit) + lowLimit;
    }
}
