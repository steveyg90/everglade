//  ████████╗███████╗██████╗ ██████╗  █████╗ ██╗███╗   ██╗ ██████╗ ███████╗███╗   ██╗███████╗██████╗  █████╗ ████████╗██╗ ██████╗ ███╗   ██╗
//  ╚══██╔══╝██╔════╝██╔══██╗██╔══██╗██╔══██╗██║████╗  ██║██╔════╝ ██╔════╝████╗  ██║██╔════╝██╔══██╗██╔══██╗╚══██╔══╝██║██╔═══██╗████╗  ██║
//     ██║   █████╗  ██████╔╝██████╔╝███████║██║██╔██╗ ██║██║  ███╗█████╗  ██╔██╗ ██║█████╗  ██████╔╝███████║   ██║   ██║██║   ██║██╔██╗ ██║
//     ██║   ██╔══╝  ██╔══██╗██╔══██╗██╔══██║██║██║╚██╗██║██║   ██║██╔══╝  ██║╚██╗██║██╔══╝  ██╔══██╗██╔══██║   ██║   ██║██║   ██║██║╚██╗██║
//     ██║   ███████╗██║  ██║██║  ██║██║  ██║██║██║ ╚████║╚██████╔╝███████╗██║ ╚████║███████╗██║  ██║██║  ██║   ██║   ██║╚██████╔╝██║ ╚████║
//     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚══════╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
//

package com.sdgja.map;

import java.util.Arrays;

public class TerrainGeneration {

    private static int[] surfacePoints = null;

    public static int[] getPoints() {
        return surfacePoints;
    }

    // Midpoint displacement (Diamond square algorithm)
    public static void generateTerrain(int width, int height, double displace, double roughness, int scale, int[][] map) {
        Double[] points = new Double[width + 1];

        int power = (int) Math.pow(2, Math.ceil(Math.log(width) / (Math.log(2))));

        points[0] = height / 2 + (Math.random() * displace * 2) - displace;

        points[power] = height / 2 + (Math.random() * displace * 2) - displace;
        displace *= roughness;

        for (int i = 1; i < power; i *= 2) {
            for (int j = (power / i) / 2; j < power; j += power / i) {
                points[j] = ((points[j - (power / i) / 2] + points[j + (power / i) / 2]) / 2);
                points[j] += (Math.random() * displace * 2) - displace;
            }
            displace *= roughness;
        }

        // Convert the Double array to int array
        surfacePoints = Arrays.stream(points).mapToInt(Double::intValue).toArray();
        for (int x = 0; x < width; x++) {
            int top = height-surfacePoints[x]/scale;
            for (int y = height; y > height - (surfacePoints[x]/scale); y--) {
                map[x][Math.abs(y)] = 'D';
            }
            map[x][Math.abs(top)] = 'T';
            // Add to top of map

        }
    }
}
