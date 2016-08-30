package iceburg.logcontroller;


import iceburg.events.Event;
import iceburg.events.EventListener;
import iceburg.events.EventType;

import iceburg.events.rabbitmq.RabbitMQEventHub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;


/**
 * Application Logger for the Iceberg
 */
public class LoggerController {

    private static final String EVENTS_EXCHANGE = "events";

    private static final String RABBITMQ_URL = "amqp://localhost";

    private RabbitMQEventHub eventHub;

    private final Logger logger;

    public LoggerController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void init(String icebergId) {

        this.logger.debug("Init LoggerController: icebergId: {}", icebergId);

        this.eventHub = new RabbitMQEventHub();
        this.eventHub.setRabbitMQUrl(RABBITMQ_URL);
        this.eventHub.setExchangeName(EVENTS_EXCHANGE);
        this.eventHub.init(icebergId);

        this.eventHub.addListener(new EventListener() {

            public List<EventType> getWantedEvents() {
                return Arrays.asList(new EventType[]{EventType.TOUCH, EventType.PROXIMITY, EventType.MULTI_BERG});
            }

            public void handleEvent(final Event e) {
                LoggerController.this.logger.info("Event: ", e);
            }
        });
    }

    public static void main(String[] args) {
        LoggerController controller = new LoggerController();
        controller.init(args[0]);
    }
}
