package wethinkcode.places;

import java.io.*;
import java.util.Arrays;

public class Properties {
    private static java.util.Properties properties;

    public static void load(String propertiesFile) throws IOException {
        // create a reader object on the properties file
        FileReader reader = new FileReader(propertiesFile);

        // create properties object
        properties = new java.util.Properties();

        // Add a wrapper around reader object
        properties.load(reader);
    }

    public static void loadDefault() throws IOException {
        load("default.properties");
    }

    public static String get(String property){
        return properties.getProperty(property);
    }

    public static void set(String property, String value){
        properties.setProperty(property, value);
    }

    public static void save(String fileName, String comment) throws IOException {
        try (OutputStream out = new FileOutputStream(fileName)){
            properties.store(out, comment);
        }
    }

    public static void fromCLI(String ...s){
        Arrays.stream(s)
                .map((string) -> string.split("=", 1))
                .forEach((property) -> properties.setProperty(property[0], property[1]));
    }

//    // Configuration keys
//    public static final String CFG_CONFIG_FILE = "config.file";
//    public static final String CFG_DATA_DIR = "resources/";//"data.dir";
//    public static final String CFG_DATA_FILE = "PlaceNamesZA2008.csv";//"data.file";
//    public static final int CFG_SERVICE_PORT = 6969;//"server.port";
}
