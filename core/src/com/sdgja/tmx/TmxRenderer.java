
//  ███████╗███╗   ██╗████████╗██╗████████╗██╗   ██╗███████╗████████╗██████╗ ██╗   ██╗ ██████╗████████╗██╗   ██╗██████╗ ███████╗██████╗  █████╗ ████████╗ █████╗
//  ██╔════╝████╗  ██║╚══██╔══╝██║╚══██╔══╝╚██╗ ██╔╝██╔════╝╚══██╔══╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██║   ██║██╔══██╗██╔════╝██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗
//  █████╗  ██╔██╗ ██║   ██║   ██║   ██║    ╚████╔╝ ███████╗   ██║   ██████╔╝██║   ██║██║        ██║   ██║   ██║██████╔╝█████╗  ██║  ██║███████║   ██║   ███████║
//  ██╔══╝  ██║╚██╗██║   ██║   ██║   ██║     ╚██╔╝  ╚════██║   ██║   ██╔══██╗██║   ██║██║        ██║   ██║   ██║██╔══██╗██╔══╝  ██║  ██║██╔══██║   ██║   ██╔══██║
//  ███████╗██║ ╚████║   ██║   ██║   ██║      ██║   ███████║   ██║   ██║  ██║╚██████╔╝╚██████╗   ██║   ╚██████╔╝██║  ██║███████╗██████╔╝██║  ██║   ██║   ██║  ██║
//  ╚══════╝╚═╝  ╚═══╝   ╚═╝   ╚═╝   ╚═╝      ╚═╝   ╚══════╝   ╚═╝   ╚═╝  ╚═╝ ╚═════╝  ╚═════╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝
//

package com.sdgja.tmx;

import com.badlogic.gdx.Gdx;
import com.sdgja.filesystem.IFileName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class EntityStructureData {
    int[][]structureData;
    int width,height;
}


//  ████████╗███╗   ███╗██╗  ██╗██████╗ ███████╗███╗   ██╗██████╗ ███████╗██████╗ ███████╗██████╗
//  ╚══██╔══╝████╗ ████║╚██╗██╔╝██╔══██╗██╔════╝████╗  ██║██╔══██╗██╔════╝██╔══██╗██╔════╝██╔══██╗
//     ██║   ██╔████╔██║ ╚███╔╝ ██████╔╝█████╗  ██╔██╗ ██║██║  ██║█████╗  ██████╔╝█████╗  ██████╔╝
//     ██║   ██║╚██╔╝██║ ██╔██╗ ██╔══██╗██╔══╝  ██║╚██╗██║██║  ██║██╔══╝  ██╔══██╗██╔══╝  ██╔══██╗
//     ██║   ██║ ╚═╝ ██║██╔╝ ██╗██║  ██║███████╗██║ ╚████║██████╔╝███████╗██║  ██║███████╗██║  ██║
//     ╚═╝   ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═════╝ ╚══════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝
//

public final class TmxRenderer {

    static {
        cache = new HashMap<>();
    }
    private static int width, height;

    private static HashMap<String, EntityStructureData> cache;

    private static short[][] _map;
    private static List<String> files = new ArrayList<String>();

    private static List<String> loadFiles() {

        File folder = new File("./core/assets/TMXbuildings");
        File[] listOfFiles = folder.listFiles();
        for(int i = 0; i < listOfFiles.length; i++){
            String filename = listOfFiles[i].getName();
            if(filename.endsWith(".xml")||filename.endsWith(".XML")) {
                files.add(filename.substring(0, filename.lastIndexOf('.')));
            }
        }
        return files;
    }

    public static void removeAll() {
        cache.clear();
    }
    public static List<String> getStructureKeys() { return files; }

    public static boolean loadStructures(short[][] map) {

        List<String> files = loadFiles();
        _map = map;
        boolean bLoaded = true;
        for(String xml : files) {
        String filename = String.format(IFileName.ROOT + "/TMXbuildings/%s.xml", xml);

            try {
                File file = new File(filename);
                DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFac.newDocumentBuilder();
                Document doc = docBuilder.parse(file);
                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("layer");
                Element eElement = (Element) nList.item(0);
                width = Integer.parseInt(eElement.getAttribute("width"));
                height = Integer.parseInt(eElement.getAttribute("height"));

                int[][] structure = new int[height][width];  // store structure in here
                int xIndex = 0, yIndex = 0;

                nList = doc.getElementsByTagName("data");
                Node nNode = nList.item(0);
                nList = nNode.getChildNodes();
                for (int i = 0; i < nList.getLength(); i++) {
                    nNode = nList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        eElement = (Element) nNode;
                        String tile = ((Element) nNode).getAttribute("gid");
                        tile = (tile.length() == 0 ? "0" : tile);
                        structure[xIndex][yIndex++] = (!tile.equals("0") ? Integer.parseInt(tile) - 1 : Integer.parseInt(tile));
                        if (yIndex >= width) {
                            yIndex = 0;
                            xIndex++;
                        }
                    }
                }
                EntityStructureData e = new EntityStructureData();
                e.width=width;
                e.height=height;
                e.structureData = structure;
                cache.put(xml, e);  // store this structure

            } catch (Exception e) {
                Gdx.app.error("ERROR in tmxRenderer", "Structure not found:" + xml);
                System.exit(1);
            } finally {
            }
        }

        return bLoaded;
    }


    public static boolean readAndParse(String key, short[][] map, int worldX, int worldY) {

        _map = map;

        String filename = String.format(IFileName.ROOT + "/TMXbuildings/%s.xml",key);
        boolean bLoaded = true;

        if(cache.containsKey(key)) {
            addStructure(key,worldX,worldY,map);  // use cached data
            return bLoaded;
        }

        try {
            File file = new File(filename);
            DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFac.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("layer");
            Element eElement = (Element) nList.item(0);
            width = Integer.parseInt(eElement.getAttribute("width"));
            height = Integer.parseInt(eElement.getAttribute("height"));

            int[][] structure = new int[height][width];  // store structure in here
            int xIndex = 0, yIndex = 0;

            nList = doc.getElementsByTagName("data");
            Node nNode = nList.item(0);
            nList = nNode.getChildNodes();
            for (int i = 0; i < nList.getLength(); i++) {
                nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    eElement = (Element) nNode;
                    String tile = ((Element) nNode).getAttribute("gid");
                    tile = (tile.length() == 0 ? "0" : tile);
                    structure[xIndex][yIndex++] = (!tile.equals("0") ? Integer.parseInt(tile) - 1 : Integer.parseInt(tile));
                    if (yIndex >= width) {
                        yIndex = 0;
                        xIndex++;
                    }
                }
            }

            // Copy into map array passed across
//            for (int x = 0; x < height; x++) {
//                for (int y = 0; y < width; y++) {
//                    if (structure[x][y] != 0) {        // Do not draw tile # 0
//                        map[y + worldX][x + worldY] = 1; //structure[x][y];
//                    }
//                }
//            }

            EntityStructureData e = new EntityStructureData();
            e.width=width;
            e.height=height;
            e.structureData = structure;
            cache.put(key, e);  // store this structure
            ;
        } catch (Exception e) {
            Gdx.app.error("ERROR in tmxRenderer","Structure not found:" + key);
            System.exit(1);
        } finally {
        }
        return bLoaded;
    }

    public static int getStructureWidth(String key){
        return cache.get(key).width;
    }
    public static int getStructureHeight(String key){ return cache.get(key).height; }
    public static EntityStructureData getEntityStructure(String key) {
        return cache.get(key);
    }

    public static int[][] getStructureMap(String key) {
        return cache.get(key).structureData;
    }
    public static short[][] getMap() {
        return _map;
    }

    // Draw the complete structure into the map, skipping all 0 tiles

    public static void drawStructureIntoMapSkip0(String key, short[][] map, int x, int y) {
        int [][] current = getStructureMap(key);
        int width =getStructureWidth(key);
        int height =getStructureHeight(key);

        for (int yy = 0; yy < height; yy++) {
            for (int xx = 0; xx < width; xx++) {
                if (current[yy][xx]!=0) { map[x+xx][y+yy] = (short) current[yy][xx];}
            }
        }
    }
    // Draw the complete structure into the map, skipping all 0 tiles, draws behind map!

    public static void drawStructureBehindMapSkip0(String key, short[][] map, int x, int y) {
        int [][] current = getStructureMap(key);
        int width =getStructureWidth(key);
        int height =getStructureHeight(key);

        for (int yy = 0; yy < height; yy++) {
            for (int xx = 0; xx < width; xx++) {
                if (current[yy][xx]!=0 && map[x+xx][y+yy]==0) { map[x+xx][y+yy] = (short) current[yy][xx];}
            }
        }
    }

//    // Draw the complete structure into the map, skipping all 0 tiles and INVERTED!!
//
//    public static void drawStructureIntoMapValley(String key, int [][]map,int x, int y) {
//        int [][] current = getStructureMap(key);
//        int width =getStructureWidth(key);
//        int height =getStructureHeight(key);
//
//        for (int yy = 0; yy < height; yy++) {
//            for (int xx = 0; xx < width; xx++) {
//                if (current[yy][xx]!=0) { map[x+xx][y+yy] = 0;}
//            }
//        }
//    }


// Draw the complete structure into the map

    public static void drawStructureIntoMap(String key, short[][] map, int x, int y) {
        int [][] current = getStructureMap(key);
        int width =getStructureWidth(key);
        int height =getStructureHeight(key);

        for (int yy = 0; yy < height; yy++) {
            for (int xx = 0; xx < width; xx++) {
                map[x+xx][y+yy] = (short) current[yy][xx];
            }
        }
    }
// Draw the complete structure into the map with 'SKIP' rows removed from ~~ TOP ~~ of structure

    public static void drawStructureIntoMapSkipTop(String key, short[][] map, int x, int y, int skip) {
        int [][] current = getStructureMap(key);
        int width =getStructureWidth(key);
        int height =getStructureHeight(key);

        for (int yy = skip; yy < height; yy++) {
            for (int xx = 0; xx < width; xx++) {
                map[x+xx][y+yy] = (short) current[yy][xx];
            }
        }
    }

// Draw the complete structure into the map with 'SKIP' rows removed from bottom of structure

    public static void drawStructureIntoMapSkip(String key, short[][] map, int x, int y, int skip) {
        int [][] current = getStructureMap(key);
        int width =getStructureWidth(key);
        int height =getStructureHeight(key);

        for (int yy = 0; yy < height-skip; yy++) {
            for (int xx = 0; xx < width; xx++) {
                map[x+xx][y+yy] = (short) current[yy][xx];
            }
        }
    }


// Grow a structure from the base up, draws for 'growth' number of tiles.

    public static void growStructureIntoMap(String key, short[][] map, int x, int y, int growth) {
        int [][] current = getStructureMap(key);
        int width =getStructureWidth(key);
        int height =getStructureHeight(key);

        int drawStart=height-growth;
        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < growth; yy++) {
                map[x+xx][drawStart+y+yy] = (short) current[yy][xx];
            }
        }
    }

// Grow a structure from the base up, this allocates the space for its final form.

    public static void allocateStructureIntoMap(String key, short[][] map, int x, int y) {
        int width =getStructureWidth(key);
        int height =getStructureHeight(key);

        for (int yy = 0; yy < height; yy++) {
            for (int xx = 0; xx < width; xx++) {
                map[x+xx][y+yy] = 127;
            }
        }
    }

    public static boolean containsStructure(String key) {
        return cache.containsKey(key);
    }
    public static void addStructure(String key, int worldX, int worldY, short[][] map) {
        if(containsStructure(key)) { // we can use previous structure data
            EntityStructureData e = cache.get(key);

//            storeStructure(worldX,worldY,e.width,e.height,map,e.structureData);
        }
    }

//    private static void storeStructure(int worldX, int worldY, int width, int height, int[][]map, int[][]structure) {
//        for (int x = 0; x < height; x++) {
//            for (int y = 0; y < width; y++) {
//                if (structure[x][y]!=0){        // Do not draw tile # 0
//                    map[y + worldX][x + worldY] = structure[x][y];
//                }
//            }
//        }
//    }
}
