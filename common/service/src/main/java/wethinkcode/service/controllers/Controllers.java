package wethinkcode.service.controllers;


import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import org.reflections.Reflections;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.reflections.scanners.Scanners.TypesAnnotated;
import static wethinkcode.logger.Logger.formatted;
import static wethinkcode.service.messages.AlertService.publishSevere;
import static wethinkcode.service.messages.AlertService.publishWarning;

public class Controllers {

    private final Object instance;
    private final Set<EndpointGroup> endpoints = new HashSet<>();

    private final Logger logger;


    public Controllers(Object instance) {
        this.instance = instance;
        logger = formatted(
            this.getClass().getSimpleName()
                    + " "
                    + instance.getClass().getSimpleName(),
                "\u001B[38;5;39m", "\u001B[38;5;45m");
    }

    /**
     * Get's all endpoints that this object will handle.
     * @return a set of all those endpoints
     */
    public Set<EndpointGroup> getEndpoints(){
        Set<Class<?>> controllers = findControllers();
        if (controllers.size() == 0){
            logger.info("No Controllers were found for this service");
        } else {
            collectEndpoints(controllers);
        }

        return endpoints;
    }

    private Stream<Method> findMappings(Class<?> clazz){
        String path = clazz.getAnnotation(Controller.class).value();
        logger.info("Adding endpoints for '"+ path +"' from the class " + clazz.getSimpleName());
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Mapping.class));
        }
    private void addEndpoints(Class<?> clazz){
        String path = clazz.getAnnotation(Controller.class).value();
        endpoints.add(() -> ApiBuilder.path(path, () -> findMappings(clazz).forEach(this::runMethod)));
        findEndpoints(clazz).forEach(endpoints::add);
    }

    private Stream<EndpointGroup> findEndpoints(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Endpoint.class))
                .map(method -> {
                    try {
                        return (EndpointGroup) method.invoke(instance, instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        publishSevere("Controllers", "Failed to invoke method " + instance.getClass().getSimpleName() + " : " +  method.getName(), e);
                        return null;
                    }
                });
    }

    private void runMethod(Method method) {
        Mapping annotation = method.getAnnotation(Mapping.class);
        String path = annotation.path();
        Verb verb = annotation.value();
        logger.info(verb.name()+ (path.equals("") ? "":" for '" + path + "'") + " from the method " + method.getName());
        verb.invoke(path, (ctx) -> {
            logger.info("Invoking " + method.getName());
            try {
                method.invoke(instance, ctx, instance);
            } catch (InvocationTargetException e){
                publishWarning(instance.getClass().getSimpleName(), "Failed to invoke " + method.getName());
            }
        });
    }

    /**
     * Looks in the instance class's package for any annotated classes
     * @return a set of classes that are candidates for searching for endpoints
     */
    private Set<Class<?>> findControllers(){
        logger.info("Searching for Controller annotated classes...");
        Reflections reflections = new Reflections(instance.getClass().getPackageName());
        return reflections.get(TypesAnnotated.with(Controller.class).asClass());
    }

    private void collectEndpoints(Set<Class<?>> classes){
        logger.info("Collecting Endpoints from " + classes);
        classes.forEach(this::addEndpoints);
    }

    /**
     * Annotate a class with this to mark it as a group
     * of static endpoints to handle requests for a service
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Controller {
        /**
         * This value is for the prefix path for the requests inside
         * @return the path
         */
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Mapping {
        /**
         * The verb is the kind of request this will be
         */
        Verb value();

        /**
         * The last section of the path for the endpoint, to be attached to the controllers path
         * @return the path
         */
        String path() default "";
    }

    /**
     * This is to make upgrading easier. If you already have a group of endpoints,
     * annotate them with this to have them be collected and added to the rest.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Endpoint{ }
}

