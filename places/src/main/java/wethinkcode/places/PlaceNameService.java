package wethinkcode.places;


import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Resources;
import io.javalin.Javalin;
import picocli.CommandLine;
import wethinkcode.places.model.Places;
import wethinkcode.places.router.Router;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


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

    public static final int DEFAULT_SERVICE_PORT = 7000;
    public static final String CFG_DATA_FILE = "PlaceNamesZA2008.csv";

    public static void main( String[] args ) throws IOException, URISyntaxException, InterruptedException {
        final PlaceNameService svc = new PlaceNameService().initialise();
        final int exitCode = new CommandLine( svc ).execute( args );
        System.exit( exitCode );
    }

    // Instance state

    private Javalin server;

    public static Places places;

    public PlaceNameService(){

    }

    public void start() {
        int port = DEFAULT_SERVICE_PORT;
        if (Properties.get("port")!=null){
            port = Integer.parseInt(Properties.get("port"));
        }
        server.start();
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
    PlaceNameService initialise() throws IOException, URISyntaxException, InterruptedException {
        places = initPlacesDb();
        server = initHttpServer();
        return this;
    }

    @Override
    public void run(){
        throw new UnsupportedOperationException( "TODO" );
    }

    private Places initPlacesDb() throws IOException, URISyntaxException {
        File databaseFile = new File(Resources.getResource(CFG_DATA_FILE).toURI());
        return new PlacesCsvParser().parseCsvSource(databaseFile);
    }

    private Javalin initHttpServer() throws InterruptedException {
        Javalin server = Javalin.create();
        Router.loadRoutes(server);
        return server;
    }
}