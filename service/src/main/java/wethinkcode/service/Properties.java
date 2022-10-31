package wethinkcode.service;

import com.google.common.io.Resources;
import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Properties implements Callable<Integer> {
    private static final String DEFAULT_PORT = "7000";
    static File DEFAULT_CONFIG;
    private final Logger logger;

    private final java.util.Properties properties = new java.util.Properties();

    /**
     * Holds the package URL for the routes
     */
    public String route_package;

    /**
     * Config File that holds all properties
     */
    @CommandLine.Option(
            names = {"--config", "-c"},
            description = "A file pathname referring to an (existing!) configuration file in standard Java properties-file format",
            type = File.class,
            echo = true
    )

    public File config = null;

    /**
     * Port for the service
     */
    @CommandLine.Option(
            names = {"--port", "-p"},
            description = "The name of a directory where CSV datafiles may be found. This option overrides and data-directory setting in a configuration file.",
            type = Integer.class
    )
    public Integer port = null;

    /**
     * This will get a non-standard field from your .properties file.
     * @param nonStandardField the name of the field
     * @return A string representing that field
     */
    public String get(String nonStandardField){
        return properties.getProperty(nonStandardField);
    }

    Properties() {
        this.logger = Logger.getLogger(this.getClass().getSimpleName());

        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            public synchronized String format(LogRecord lr) {
                return "[" + lr.getLoggerName() + "] " + lr.getLevel().getLocalizedName() + " " + lr.getMessage() + "\n";
            }
        });

        logger.addHandler(handler);
    }

    @Override
    public Integer call() {
        return 0;
    }

    void defaultNullValues() throws IOException {

        if (config == null){
            config = DEFAULT_CONFIG;
            logger.info("Loaded Default Config File: " + config);
        } else {
            logger.info("CLI Argument Found for Config File: " + config);
        }

        properties.load(new FileReader(config));

        if (port == null) {
            port = Integer.parseInt((String) properties.getOrDefault("port", DEFAULT_PORT));
            logger.info("Loaded Default Port: " + port);
        } else {
            logger.info("CLI Argument Found for Port: " + port);
        }
    }
}
