package wethinkcode.service.messages;

/**
 * Notes if a listener/publisher will use a queue or topic.
 */
public enum Prefix {
    QUEUE("queue://"),
    TOPIC("topic://");

    /**
     * The start of the url to connect to, just add the domain and destination name after.
     */
    public final String prefix;

    Prefix(String s) {
        prefix = s;
    }
}
