package org.openehealth.ipf.platform.camel.event.adapter;

import static org.apache.commons.lang3.Validate.notNull;

import org.apache.camel.ProducerTemplate;
import org.openehealth.ipf.commons.event.EventChannelAdapter;
import org.openehealth.ipf.commons.event.EventEngine;
import org.openehealth.ipf.commons.event.EventObject;
import org.openehealth.ipf.commons.event.DeliveryMode;

/**
 * Channel adapter for channels that are accessed via Camel routes
 * <p>
 * This adapter uses two Camel routes to send and receive events.
 * Sending events is done by sending it to a route specified by an endpoint.
 * Receiving events is done by allowing a second route to access the 
 * {@link #send} method via the bean endpoint in Camel.
 *   
 * @author Jens Riemschneider
 */
public class RoutingChannelAdapter extends EventChannelAdapter {

    private final ProducerTemplate template;
    private final String sendEndpoint;
    private final boolean supportsAsync;
    
    /**
     * Constructs the adapter
     * @param engine
     *          the event engine that is called to distribute event objects
     * @param template
     *          the producer template to access the route for sending events
     * @param sendEndpoint
     *          the endpoint of the route that is used to send events to the channel
     * @param supportsAsync
     *          <code>true</code> if the channel adapter supports asynchronous delivery
     */
    public RoutingChannelAdapter(EventEngine engine, ProducerTemplate template, 
            String sendEndpoint, boolean supportsAsync) {
        
        super(engine);
        
        notNull(template, "template cannot be null");
        notNull(sendEndpoint, "sendEndpoint cannot be null");
        this.template = template;
        this.sendEndpoint = sendEndpoint;
        this.supportsAsync = supportsAsync;
    }

    @Override
    public void send(EventObject event, DeliveryMode quality) {
        notNull(event, "event cannot be null");
        notNull(quality, "quality cannot be null");

        template.sendBody(sendEndpoint, event);
    }

    /**
     * Delegates to {@link EventEngine#distributeToHandlers(EventObject)}
     * @param event
     *          the event to distribute
     */
    public void distributeToHandlers(EventObject event) {
        getEventEngine().distributeToHandlers(event);
    }
    
    @Override
    public boolean accepts(DeliveryMode mode) {
        return supportsAsync || super.accepts(mode);
    }
}
