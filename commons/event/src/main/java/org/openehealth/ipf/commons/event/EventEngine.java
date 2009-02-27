package org.openehealth.ipf.commons.event;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Provides functionality to publish events
 * @author Jens Riemschneider
 */
public class EventEngine {
    /** 
     * The topic used by the event engine if an event is not published with 
     * a specific topic. 
     */
    public static final String DEFAULT_TOPIC = "default";

    private List<EventChannelAdapter> channelAdapters =
        new ArrayList<EventChannelAdapter>();
    
    private Map<String, List<Subscription>> topicSubscriptions = 
        new HashMap<String, List<Subscription>>();
    
    /**
     * Constructs an event engine
     * <p>
     * By default the event engine has a single channel adapter for synchronous event
     * delivery.
     */
    public EventEngine() {
        registerChannel(new EventChannelAdapter(this));
    }

    /**
     * Publishes an event
     * <p>
     * @param topic
     *          the topic that the event is published on, can be <code>null</code> to
     *          denote the default topic
     * @param eventObject
     *          the event that is published
     * @param sync  
     *          <code>true</code> for synchronous, <code>false</code> for
     *          asynchronous behavior
     */
    public void publish(String topic, EventObject eventObject, boolean sync) {
        notNull(eventObject, "eventObject cannot be null");
        
        DeliveryMode mode = new DeliveryMode();
        mode.setSync(sync);
        
        publish(topic, eventObject, mode);
    }
    
    /**
     * Distributes an event object to handlers subscribed to the given topic
     * <p>
     * Note: This method is usually called by the channel adapters. Standard publishing
     *       is done via {@link #publish}.
     *       
     * @param eventObject
     *          the event that is distributed
     */
    public void distributeToHandlers(EventObject eventObject) {
        notNull(eventObject, "eventObject cannot be null");
        
        String topic = (String) eventObject.getMetaData(EventObject.MetaDataKeys.TOPIC.getKey());
        
        List<Subscription> subscriptions = topicSubscriptions.get(topic);
        if (subscriptions != null) {
            for (Subscription subscription : subscriptions) {
                EventFilter filter = subscription.getFilter();
                if (filter == null || filter.accepts(eventObject)) {
                    subscription.getHandler().handle(eventObject);
                }
            }
        }
    }

    /**
     * Registers a handler with a specific topic
     * @param subscription
     *          the subscription information containing handler, filter and topic
     *          to perform the registration
     */
    public void subscribe(Subscription subscription) {
        notNull(subscription, "subscription cannot be null");
        notNull(subscription.getHandler(), "subscription.getHandler() cannot be null");
        
        String topic = normalize(subscription.getTopic());
        List<Subscription> subscriptions = topicSubscriptions.get(topic);
        if (subscriptions == null) {
            subscriptions = new ArrayList<Subscription>();            
        }
        subscriptions.add(subscription);
        topicSubscriptions.put(topic, subscriptions);
    }
    
    public void registerChannel(EventChannelAdapter adapter) {
        notNull(adapter, "adapter cannot be null");
        channelAdapters.add(0, adapter);
    }
    
    private String normalize(String topic) {
        if (topic == null) {
            return DEFAULT_TOPIC;
        }
        return topic.toLowerCase();
    }
    
    private void publish(String topic, EventObject eventObject, DeliveryMode mode) {
        eventObject.setMetaData(EventObject.MetaDataKeys.EVENT_ID.getKey(), UUID.randomUUID().toString());
        eventObject.setMetaData(EventObject.MetaDataKeys.TIME_STAMP.getKey(), new Date());
        eventObject.setMetaData(EventObject.MetaDataKeys.TOPIC.getKey(), normalize(topic));

        for (EventChannelAdapter adapter : channelAdapters) {
            if (adapter.accepts(mode)) {
                adapter.send(eventObject, mode);
                return;
            }
        }
        
        throw new UnsupportedDeliveryModeException(mode);
    }
}
