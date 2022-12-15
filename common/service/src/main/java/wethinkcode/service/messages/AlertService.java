package wethinkcode.service.messages;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.eclipse.jetty.util.BlockingArrayQueue;
import wethinkcode.service.Service;

import java.util.Queue;
import java.util.logging.Logger;

import static wethinkcode.logger.Logger.formatted;

/**
 * Used to send warnings and severe issues to nfty,
 * the internal alert queue and simultaneously log these issues.
 */
@Service.AsService
public class AlertService {
    @Service.Publish(destination = "alert")
    public static final Queue<String> ERRORS;
    private static final AlertService INSTANCE;
    private static final Thread RUNTIME_EXCEPTION_CATCHER ;

    private static final Logger LOGGER;

    static {
        LOGGER = formatted("ALERT", "", "");
        ERRORS = new BlockingArrayQueue<>();
        INSTANCE = new AlertService();

        RUNTIME_EXCEPTION_CATCHER = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    publishSevere("UNKNOWN", "Uncaught runtime exception found!", e);
                }
            }
        });
    }

    /**
     * Starts up this services posting to the internal alert
     * queue and also start up a thread that watches for uncaught runtime exceptions
     */
    public static void start(){
        if (RUNTIME_EXCEPTION_CATCHER.isAlive()){
            return;
        }
        new Service<>(INSTANCE).execute();
        RUNTIME_EXCEPTION_CATCHER.start();
    }

    private static void postToNTFY(String message){
        try {
            HttpResponse<JsonNode> request = Unirest.post("https://ntfy.sh/morgan-moss-alerts").body(message).asJson();
            int status = request.getStatus();
        } catch (UnirestException e) {
            LOGGER.warning("Cant reach nfty");
        }
    }

    /**
     * Marks an issue as severe, posts it on nfty, the alert queue and logs it.
     * Will also stop the program.
     * @param location i.e. where the issue lies
     * @param message what kind of issue is this?
     * @param e the exception thrown, for a full stack trace.
     */
    public static void publishSevere(String location, String message, Exception e){
        LOGGER.severe("["+location+ "] " + message);
        postToNTFY("["+location+ "] [SEVERE] " + message);
        ERRORS.add("["+location+ "] [SEVERE] " + message);
        e.printStackTrace();
        System.exit(6);
    }
    /**
     * Marks an issue as severe, posts it on nfty, the alert queue and logs it.
     * @param location i.e. where the issue lies
     * @param message what kind of issue is this?
     */
    public static void publishWarning(String location, String message){
        LOGGER.warning("["+location+ "] " + message);
        postToNTFY("["+location+ "] [WARNING] " + message);
        ERRORS.add("["+location+ "] [WARNING] " + message);
    }
}
