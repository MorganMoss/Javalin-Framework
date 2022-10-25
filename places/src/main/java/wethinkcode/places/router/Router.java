package wethinkcode.places.router;

import wethinkcode.places.router.route.Route;

import io.javalin.Javalin;

import org.reflections.Reflections;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;


public class Router {
    private static final String PLUGIN_PACKAGE_PREFIX = "wethinkcode.places.router.route";
    private static final Logger logger = getLogger("Router");

    private static Javalin server;

    /**
     * Gets a list of classes that implement Route
     * @return a set of classes
     */
    private static Set<Class<? extends Route>> getHandlers(){
        Reflections reflections = new Reflections(PLUGIN_PACKAGE_PREFIX);
        return reflections.getSubTypesOf(Route.class);
    }

    /**
     * Takes a route and adds it to the servers routes
     * @param route that controls handlers over a certain path.
     */
    private static void setupHandler(Route route){
        server.routes(route.getEndPoints());
    }

    /**
     * Creates a runnable that will add a handler to the server
     * @param handler to be added
     * @return the runnable
     */
    private static Runnable createRunnable(Class<? extends Route> handler){
        return () -> {
            try {
                setupHandler(handler.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            logger.info("The plugin '" + handler.getName() + "' has been loaded.");
        };
    }

    /**
     * Waits for all the handlers to load
     * @param pool of threads
     * @throws InterruptedException if interrupted
     */
    private static void waitForLoad(ExecutorService pool) throws InterruptedException {
        pool.shutdown();

        if (!pool.awaitTermination(5, TimeUnit.MINUTES)){
            throw new RuntimeException("Failed to load all Routes");
        }
    }

    /**
     * Sets up the router calls for the server, excluding the static pages
     */
    private static void setupAllHandlers() throws InterruptedException {
        Set<Class<? extends Route>> handlers = getHandlers();
        ExecutorService pool = Executors.newFixedThreadPool(handlers.size());

        logger.info("Starting to load Handlers...");

        for (Class<? extends Route> handler : handlers){
            pool.submit(createRunnable(handler));
        }
        waitForLoad(pool);
    }

    public static void loadRoutes(Javalin server) throws InterruptedException {
        Router.server = server;
        setupAllHandlers();
        Router.server = null;
    }

}
