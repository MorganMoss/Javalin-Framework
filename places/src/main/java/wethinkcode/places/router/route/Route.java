package wethinkcode.places.API;

import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

/**
 * An interface used to create plugin Handlers for the web server
 */
public interface Route extends Handler {

    /**
     * The verb that will describe what kind of handler this will be
     * @return A verb
     */
    @NotNull
    Verb getVerb();

    /**
     * The path after the URL to execute this Handler
     * @return
     */
    @NotNull
    String getPath();
}
