package wethinkcode.places;

import com.google.common.io.Resources;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Properties {
    private final java.util.Properties properties = new java.util.Properties();

    public void load(File propertiesFile) throws IOException {
        FileReader reader = new FileReader(propertiesFile);
        properties.load(reader);
    }

    public void loadDefault() throws IOException, URISyntaxException {
        File f = new File(Resources.getResource("default.properties").toURI());
        load(f);
    }

    public String get(String property){
        return properties.getProperty(property);
    }

    public void set(String property, String value){
        properties.setProperty(property, value);
    }

    public void save(String fileName, String comment) throws IOException {
        try (OutputStream out = new FileOutputStream(fileName)){
            properties.store(out, comment);
        }
    }

    public void fromCLI(String ...s){
        Arrays.stream(s)
                .map((string) -> string.split("=", -1))
                .forEach((property) -> properties.setProperty(property[0], property[1]));
    }

//    // Configuration keys
//    public static final String CFG_CONFIG_FILE = "config.file";
//    public static final String CFG_DATA_DIR = "resources/";//"data.dir";
//    public static final String CFG_DATA_FILE = "PlaceNamesZA2008.csv";//"data.file";
//    public static final int CFG_SERVICE_PORT = 6969;//"server.port";
}
