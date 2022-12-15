package wethinkcode.service.messages;

import javax.jms.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static wethinkcode.logger.Logger.formatted;
import static wethinkcode.service.messages.AlertService.publishWarning;
import static wethinkcode.service.messages.Broker.getDestination;
import static wethinkcode.service.messages.Broker.getSession;

/**
 * a listener that will get messages from an activeMQ server.
 */
public class Listener {

    private final Logger logger;
    private final String destinationName;

    private URI uri;

    private final String name;

    /**
     * Set up the initial parameters for the listener
     * @param destinationName i.e. the queue or topic name
     * @param name of the listener,
     *             generally will note the name of the method and class handling the message.
     */
    public Listener(String destinationName, String name) {
        this.logger = formatted("Listener " + name, "\u001b[38;5;9m", "\u001b[38;5;209m");
        this.destinationName = destinationName;
        this.uri = Broker.BROKER_URI;
        this.name = name;
    }

    /**
     * Starts up the listener, uses a consumer to accept the messages and handle them externally
     * @param messageConsumer to accept the message
     * @throws JMSException if it fails to start up.
     */
    public void listen(Consumer<String> messageConsumer) throws JMSException {
        Session session = getSession(uri);
        Destination destination = getDestination(session, destinationName);

        MessageConsumer consumer = session.createConsumer(destination);
            logger.info("Waiting for messages...");
            while (Broker.ACTIVE) {
                Message msg;
                try {
                    msg = consumer.receive();
                } catch (JMSException e) {
                    publishWarning("Listener " + name, "Could not receive messages");
                    return;
                }

                if (!(msg instanceof TextMessage)) {
                    logger.info("Unexpected message type: " + msg.getClass());
                    continue;
                }

                String body = ((TextMessage) msg).getText();
                logger.info("Received Message: " + "\u001b[38;5;203m" + body);

                if ("SHUTDOWN".equals(body)) {
                    logger.info("Shutting down Listener");
                    return;
                }
                messageConsumer.accept(body);
                logger.info("Message Processed");
            }
    }


    /**
     * If you want to use a amq service externally
     */
    public void overrideURL(String overrideURL) throws URISyntaxException {
        uri = new URI(overrideURL);
    }
}
