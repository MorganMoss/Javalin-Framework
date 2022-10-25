package wethinkcode.places;

import java.util.Hashtable;

public class Configuration {

    private Configuration(String propertiesFile){
        File file = getFile();
        
    }
    private Configuration(){
//        new Configuration("default.properties");
    }

    // Configuration keys
    public static final String CFG_CONFIG_FILE = "config.file";
    public static final String CFG_DATA_DIR = "resources/";//"data.dir";
    public static final String CFG_DATA_FILE = "PlaceNamesZA2008.csv";//"data.file";
    public static final int CFG_SERVICE_PORT = 6969;//"server.port";
}
