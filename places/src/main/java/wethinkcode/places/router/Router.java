package wethinkcode.places.API;

import java.util.Set;

public class Router {

    private static final String PLUGIN_PACKAGE_PREFIX = "za.co.mathart.routes";

    /**
     * Gets a list of classes that implement APIHandler
     *
     * @return a set of classes
     */
    private Set<Class<? extends Route>> getHandlers(){
        Reflections reflections = new Reflections(PLUGIN_PACKAGE_PREFIX);
        return reflections.getSubTypesOf(Route.class);
    }


    /**
     * Gets the path, verb and Handler from the plugin and adds it to the javalin object.
     * @param route the Object that implements APIHandler
     */
    public void setupHandler(Route route){
        String path = route.getPath();

        switch (route.getVerb()){
            case BEFORE     -> javalin.before   (path, route);
            case GET        -> javalin.get      (path, route);
            case POST       -> javalin.post     (path, route);
            case PUT        -> javalin.put      (path, route);
            case PATCH      -> javalin.patch    (path, route);
            case DELETE     -> javalin.delete   (path, route);
            case AFTER      -> javalin.after    (path, route);
            case HEAD       -> javalin.head     (path, route);
            case OPTIONS    -> javalin.options  (path, route);
            case TRACE      -> throw new RuntimeException("Trace not implemented");
            case CONNECT    -> throw new RuntimeException("Connect not implemented");
        }
    }


    /**
     * Sets up the API calls for the server, excluding the static pages
     */
    public void setupHandlers(){
        for (Class<? extends Route> apiHandler : getAPIHandlers()){
            try {
                setupAPIHandler(apiHandler.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | InvocationTargetException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            JavalinLogger.info("The plugin '" + apiHandler.getName() + "' has been loaded.");
        }
    }

}
