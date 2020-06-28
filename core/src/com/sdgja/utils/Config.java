
//   ██████╗ ██████╗ ███╗   ██╗███████╗██╗ ██████╗
//  ██╔════╝██╔═══██╗████╗  ██║██╔════╝██║██╔════╝
//  ██║     ██║   ██║██╔██╗ ██║█████╗  ██║██║  ███╗
//  ██║     ██║   ██║██║╚██╗██║██╔══╝  ██║██║   ██║
//  ╚██████╗╚██████╔╝██║ ╚████║██║     ██║╚██████╔╝
//   ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝     ╚═╝ ╚═════╝
//

package com.sdgja.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public final class Config {

    private static boolean lightEnabled;
    private static String version;

    static { // put our key values here that are/will be in JSON file
         lightEnabled = false;
         version = "";
    }

    public static boolean readConfigFile(String filePath) {
        boolean result = true;
        JSONParser jsonParser = new JSONParser();
        try(FileReader reader = new FileReader(filePath)) {
            Object obj = jsonParser.parse(reader);
            JSONArray configList = (JSONArray)obj;
//            System.out.println(configList);
            configList.forEach(c->parseConfigObject( (JSONObject)c));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException | ParseException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public static boolean getLightEnable() {
        return lightEnabled;
    }
    public static String getVersion() {
        return version;
    }

    private static void parseConfigObject(JSONObject config) {
        JSONObject configObject = (JSONObject)config.get("config");
        // Get enablelight and store
        lightEnabled = (boolean)configObject.get("enablelight");
        version = (String)configObject.get("version");
    }
}

