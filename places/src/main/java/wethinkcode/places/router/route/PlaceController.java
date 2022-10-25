package wethinkcode.places.router.route;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import wethinkcode.places.PlaceNameService;
import wethinkcode.places.model.Municipality;

import java.util.Optional;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TownController implements Route {

    /**
     * Gets a town by name
     */
    void getTown(Context ctx){
        String name = ctx.pathParam("name");
        Optional<Municipality> town = PlaceNameService.places.municipality(name);

        if (town.isPresent()){
            ctx.json(town.get());
            ctx.status(HttpStatus.FOUND);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

    @NotNull
    @Override
    public EndpointGroup getEndPoints() {
        return () -> path("towns", () -> {
            path("{name}", () -> {
                get(this::getTown);
            });
        });
    }
}
