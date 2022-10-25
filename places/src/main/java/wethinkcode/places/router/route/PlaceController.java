package wethinkcode.places.router.route;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import wethinkcode.places.PlaceNameService;
import wethinkcode.places.model.Place;

import java.util.Optional;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PlaceController implements Route {

    /**
     * Gets a place by name
     */
    void getMunicipality(Context ctx){
        String name = ctx.pathParam("name");
        Optional<Place> place = PlaceNameService.places.place(name);

        if (place.isPresent()){
            ctx.json(place.get());
            ctx.status(HttpStatus.FOUND);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

    @NotNull
    @Override
    public EndpointGroup getEndPoints() {
        return () -> path("place", () -> {
            path("{name}", () -> {
                get(this::getMunicipality);
            });
        });
    }
}
