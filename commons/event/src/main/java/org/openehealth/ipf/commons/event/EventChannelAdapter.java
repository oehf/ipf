package org.openehealth.ipf.commons.event;

import static org.apache.commons.lang.Validate.notNull;

/**
 * Base class for channel adapter implementations
 * <p>
 * Channel adapters are called by the framework to consume events published
 * via the local engine. Consuming events leads to the distribution of the
 * event via the channel that is encapsulated by this adapter.
 * A channel adapter is also responsible for accepting messages from the 
 * channel and distributing them to the local event engine via 
 * {@link EventEngine#distributeToHandlers}.
 * <p>
 * Adapters can deny event object distribution if they do not provide the
 * necessary quality of service. In this case {@link #canHandle} returns
 * <code>false</code> and {@link #consume} is not called by the engine.
 * @author Jens Riemschneider
 */
public class EventChannelAdapter {
    private EventEngine engine;

    /**
     * Constructs the channel adapter 
     * @param engine
     *          the event engine that is called to distribute event objects
     */
    public EventChannelAdapter(EventEngine engine) {
        notNull(engine, "engine cannot be null");        
        this.engine = engine;
    }
    
    /**
     * Called by the framework to consume an event and send it to the channel
     * <p>
     * The engine calls this method after the event source published the event.
     * <p>
     * The base class implementation simply distributes the event synchronously 
     * to subscribed handlers via the event engine
     * @param event
     *          the event to consume
     * @param quality 
     *          the quality of service parameters for the delivery
     */
    public void send(EventObject event, DeliveryMode quality) {
        notNull(event, "event cannot be null");
        notNull(quality, "quality cannot be null");
        
        engine.distributeToHandlers(event);
    }

    /**
     * Determines if this adapter accepts the given delivery mode
     * <p>
     * This base class can only provide synchronous handling of events.
     * @param mode
     *          the delivery mode
     * @return <code>true</code> if this adapter can process an event object with 
     *         the given delivery mode
     */
    public boolean accepts(DeliveryMode mode) {
        if (mode.isSync()) {
            return true;
        }
        return false;
    }
    
    /**
     * @return the event engine configured for this adapter
     */
    protected EventEngine getEventEngine() {
        return engine;
    }
}
