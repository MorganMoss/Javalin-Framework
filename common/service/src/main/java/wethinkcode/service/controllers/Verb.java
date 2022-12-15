package wethinkcode.service.controllers;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Handler;

import java.util.function.BiConsumer;

/**
 * This enum defines the different kind of requests to be used by the controller's annotation.
 * It has a nifty feature of using the actual Api Builder functions directly,
 * so you can invoke these to call those functions
 */
public enum Verb {
    GET (ApiBuilder::get),
    POST (ApiBuilder::post);
    //TODO: Add the rest as needed
    private final BiConsumer<String, Handler> verb;

    /**
     * Takes a javalin request and accepts a path and handler to create a request for that
     * @param path for the request i.e /stage/current
     * @param handler the method that will handle the context.
     */
    public void invoke(String path, Handler handler){
        verb.accept(path, handler);
    }

    Verb(BiConsumer<String, Handler> verb){
        this.verb = verb;
    }
}
