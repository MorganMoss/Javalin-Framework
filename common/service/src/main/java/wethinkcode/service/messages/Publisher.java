package wethinkcode.service.messages;

import javax.jms.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.logging.Logger;

import static wethinkcode.logger.Logger.formatted;
import static wethinkcode.service.messages.AlertService.publishWarning;
import static wethinkcode.service.messages.Broker.getDestination;
import static wethinkcode.service.messages.Broker.getSession;

public class Publisher {
    private final Logger logger;
    private final String destinationName;
    private URI uri;
    private final String name;

    /**
     * Set up the initial parameters for the publisher
     * @param destinationName i.e. the queue or topic name
     * @param name of the publisher,
     *             generally will note the name of the method and class handling the message.
     */
    public Publisher(String destinationName, String name) {
        this.logger = formatted("Publisher " + name, "\u001b[38;5;9m", "\u001b[38;5;209m");
        this.destinationName = destinationName;
        this.uri = Broker.BROKER_URI;
        this.name = name;
    }

    /**
     * Starts up the publisher, uses a consumer to accept the messages and handle them externally
     * @param messages a queue of messages that it'll send through to the amq service
     * @throws JMSException if it fails to start up.
     * @throws InterruptedException If there's an issue sleeping
     */
    public void publish(Queue<String> messages) throws JMSException, InterruptedException {
        logger.info("Starting Publisher on " + destinationName);
        Session session = getSession(uri);
        Destination destination = getDestination(session, destinationName);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        while (Broker.ACTIVE){
            if (messages.isEmpty()){
                Thread.sleep(10);
                continue;
            }

            String message = messages.remove();
            try {
                producer.send(session.createTextMessage(message));
            } catch (JMSException e) {
                publishWarning("Publisher " + name, "Could not send messages");
                return;
            }
            logger.info("Sent Message: " + "\u001b[38;5;203m" + message);
        }
    }

    /**
     * If you want to use a amq service externally
     */
    public void overrideURL(String overrideURL) throws URISyntaxException {
        uri = new URI(overrideURL);
    }
}
