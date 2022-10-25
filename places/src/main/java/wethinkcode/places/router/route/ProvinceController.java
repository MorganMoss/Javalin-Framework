package wethinkcode.places.router.route;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import wethinkcode.places.PlaceNameService;
import wethinkcode.places.model.Municipality;
import wethinkcode.places.model.Place;

import java.util.Optional;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class ProvinceController implements Route {
    /**
     * Gets a municipality by name
     */
    void getMunicipality(Context ctx){
        String name = ctx.pathParam("name");
        Optional<Municipality> municipality = PlaceNameService.places.municipality(name);

        if (municipality.isPresent()){
            ctx.json(municipality.get());
            ctx.status(HttpStatus.FOUND);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

    @NotNull
    @Override
    public EndpointGroup getEndPoints() {
        return () -> path("municipality", () -> {
            path("{name}", () -> {
                get(this::getMunicipality);
            });
        });
    }
}
