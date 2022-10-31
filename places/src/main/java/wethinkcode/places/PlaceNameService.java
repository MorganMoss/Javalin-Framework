package wethinkcode.places;


import com.google.common.annotations.VisibleForTesting;
import io.javalin.Javalin;
import picocli.CommandLine;
import wethinkcode.places.model.Places;
import wethinkcode.router.Router;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;


/**
 * I provide a Province-names Service for places in South Africa.
 * <p>
 * I read place-name data from a CSV file that I read and
 * parse into the objects (domain model) that I use,
 * discarding unwanted data in the file (things like mountain/river names). With my "database"
 * built, I then serve-up place-name data as JSON to clients.
 * <p>
 * Clients can request:
 * <ul>
 * <li>a list of available Provinces
 * <li>a list of all Towns/PlaceNameService in a given Province
 * <li>a list of all neighbourhoods in a given Municipality
 * </ul>
 * I understand the following command-line arguments:
 * <dl>
 * <dt>-c | --config &lt;configfile&gt;
 * <dd>a file pathname referring to an (existing!) configuration file in standard Java
 *      properties-file format
 * <dt>-d | --datadir &lt;datadirectory&gt;
 * <dd>the name of a directory where CSV datafiles may be found. This option <em>overrides</em>
 *      and data-directory setting in a configuration file.
 * <dt>-p | --places &lt;csvdatafile&gt;
 * <dd>a file pathname referring to a CSV file of place-name data. This option
 *      <em>overrides</em> any value in a configuration file and will bypass any
 *      data-directory set via command-line or configuration.
 */
public class PlaceNameService implements Runnable {
    public static PlaceNameService svc;
    public static void main( String[] args ) throws IOException, URISyntaxException {
        svc = new PlaceNameService().initialise(args);
        Thread thread = new Thread(svc);
        thread.setName("Places-Service");
        thread.start();
    }

    // Instance state
    private Javalin server;
    private Properties properties;
    public Places places;

    public void start() {
        server.start(properties.port);
    }

    public void stop() {
        server.stop();
    }

    /**
     * Why not put all of this into the constructor? Well, this way makes
     * it easier (possible!) to test an instance of PlaceNameService without
     * starting up all the big machinery (i.e. without calling initialise()).
     */
    @VisibleForTesting
    PlaceNameService initialise(String ... args) throws IOException, URISyntaxException {
        properties = initProperties(args);
        places = initPlacesDb();
        server = initHttpServer();
        return this;
    }

    @Override
    public void run(){
        start();

        Scanner s = new Scanner(System.in);
        String nextLine;
        while ((nextLine = s.nextLine())!=null){
            String[] args = nextLine.split(" ");
            switch (args[0].toLowerCase()) {
                case "quit" -> {
                    stop();
                    return;
                }

                case "restart" -> {
                    stop();
                    try {
                        initialise(Arrays.copyOfRange(args, 1, args.length));
                    } catch (Exception e) {
                        System.out.println("Failed to restart, please try again.");
                        break;
                    }
                    start();
                }

                case "help" -> System.out.println(
                    """
                    commands available:
                        'help' - list of commands
                        'quit' - close the service
                        'restart' <args> - restart this service with new config
                    """
                );
            }
        }
    }

    private Places initPlacesDb() throws IOException{
        File databaseFile = properties.data;
        return new PlacesCsvParser().parseCsvSource(databaseFile);
    }

    private Javalin initHttpServer() {
        Javalin server = Javalin.create();
        Router.loadRoutes("wethinkcode.places.route").forEach(server::routes);
        return server;
    }

    private Properties initProperties(String... args) {
        properties = new Properties();
        new CommandLine(properties).execute(args);
        return properties;
    }
}