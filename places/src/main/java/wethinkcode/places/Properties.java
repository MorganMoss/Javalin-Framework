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
}
