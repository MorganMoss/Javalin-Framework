package wethinkcode.router;

import io.javalin.apibuilder.EndpointGroup;
import org.jetbrains.annotations.NotNull;

/**
 * An interface used to create plugin Handlers for the web server
 */
public interface Route{
    /**
     * This contains all the handlers for this route
     * @return the group of handlers
     */
    @NotNull
    EndpointGroup getEndPoints();
}
