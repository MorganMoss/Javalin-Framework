package wethinkcode.places;

import picocli.CommandLine;

import java.io.*;
import java.util.concurrent.Callable;

public class Properties implements Callable<Integer> {
    private static final String DEFAULT_CONFIG_FILE = "places/resources/default.properties";
    private static final String DEFAULT_DATA_FILE = "places/resources/PlaceNamesZA2008.csv";
    private static final String DEFAULT_PORT = "7000";

    @CommandLine.Option(
            names = {"--config", "-c"},
            description = "A file pathname referring to an (existing!) configuration file in standard Java properties-file format",
            defaultValue = DEFAULT_CONFIG_FILE,
            type = File.class
    )
    File config;

    @CommandLine.Option(
            names = {"--data", "-d"},
            description = "The name of a directory where CSV datafiles may be found. This option overrides and data-directory setting in a configuration file.",
            type = File.class
    )
    File data = null;

    @CommandLine.Option(
            names = {"--port", "-p"},
            description = "The name of a directory where CSV datafiles may be found. This option overrides and data-directory setting in a configuration file.",
            type = Integer.class
    )
    Integer port = null;

    @Override
    public Integer call(){
        java.util.Properties properties = new java.util.Properties();

        try {
            assert config != null;
            properties.load(new FileReader(config));
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        if (data == null) {
            data = new File((String) properties.getOrDefault("data", DEFAULT_DATA_FILE));
        }

        if (port == null) {
            port = Integer.parseInt((String) properties.getOrDefault("port", DEFAULT_PORT));
        }

        return 0;
    }
}
