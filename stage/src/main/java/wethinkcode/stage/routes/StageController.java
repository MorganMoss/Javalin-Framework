package wethinkcode.stage.routes;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.reflections.serializers.JsonSerializer;
import wethinkcode.router.Route;
import wethinkcode.stage.model.BadStageException;
import wethinkcode.stage.model.Stage;

import java.util.HashMap;
import java.util.Objects;

import static io.javalin.apibuilder.ApiBuilder.*;
import static wethinkcode.stage.StageService.SERVICE;

public class StageController implements Route {

    /**
     * Gets a place by name
     */
    void getStage(Context ctx){
        ctx.json(SERVICE.stage.toString());
        ctx.status(HttpStatus.OK);
    }

    void setStage(Context ctx){
        try {
            Object stage = ctx.bodyAsClass(HashMap.class).get("stage");
            Objects.requireNonNull(stage);
            SERVICE.stage =  Stage.stageFromNumber((int) stage);
            ctx.status(HttpStatus.ACCEPTED);
        } catch (BadStageException | NullPointerException | ClassCastException e) {
            ctx.json(e);
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }


    @NotNull
    @Override
    public EndpointGroup getEndPoints() {
        return () -> {
            path("stage", () -> {
                get(this::getStage);
                post(this::setStage);
            });

        };
    }
}
