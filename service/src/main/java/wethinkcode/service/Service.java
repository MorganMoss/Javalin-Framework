package wethinkcode.service;


import com.google.common.annotations.VisibleForTesting;
import io.javalin.Javalin;
import picocli.CommandLine;
import wethinkcode.router.Router;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;


/**
 * <p>
 * <b>Generic Javalin Based Service</b>
 * </p>
 * <p>
 * This can be extended to make Services
 * </p>
 * <br/>
 * <p>
 * <b>Vital to have a routes package as a child to the package of the child service.</b>
 * </p>
 * <p>
 * That is where you implement Route Classes to be loaded into the service.
 * Override the initialise method for when you initialise data specific to the child service.
 * </p>
 * <br/>
 * <p>
 * <b>Vital to have a default.properties file in resources</b>
 * </p>
 * <p>
 * The .properties file holds any nonstandard configuration data, and the port
 * There is a CLI argument to choose an alternate config file.
 * </p>
 */
public abstract class Service implements Runnable {

    /**
     * Takes a service and runs it in a separate thread.
     * @param service to be run
     * @param name of that services thread
     */
    public static void activate(Service service, String name){
        Thread thread = new Thread(service);
        thread.setName(name);
        thread.start();
    }

    // Instance state
    private Javalin server;
    protected Properties properties;

    public void start() {
        server.start(properties.port);
    }

    public void stop() {
        server.stop();
    }

    /**
     * Why not put all of this into the constructor? Well, this way makes
     * it easier (possible!) to test an instance of Service without
     * starting up all the big machinery (i.e. without calling initialise()).
     */
    @VisibleForTesting
    public Service initialise(String ... args) throws IOException, URISyntaxException {
        properties = initProperties(args);
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
                    restart(Arrays.copyOfRange(args, 1, args.length));
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

    protected void restart(String... args){
        stop();
        try {
            initialise(args);
        } catch (Exception e) {
            System.out.println("Failed to restart, please try again.");
            return;
        }
        start();
    }

    private Javalin initHttpServer() {
        Javalin server = Javalin.create();
        Router.loadRoutes(properties.route_package).forEach(server::routes);
        return server;
    }

    private Properties initProperties(String... args) throws IOException, URISyntaxException {
        Properties.DEFAULT_CONFIG = new File(this.getClass().getResource("/default.properties").toURI());

        properties = new Properties();
        properties.route_package = this.getClass().getPackageName() + ".routes";
        new CommandLine(properties).execute(args);
        properties.defaultNullValues();
        return properties;
    }
}