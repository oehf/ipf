package org.openehealth.ipf.platform.camel.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.event.DeliveryMode;
import org.openehealth.ipf.commons.event.EventChannelAdapter;
import org.openehealth.ipf.commons.event.EventEngine;
import org.openehealth.ipf.commons.event.EventFilter;
import org.openehealth.ipf.commons.event.Subscription;
import org.openehealth.ipf.commons.event.UnsupportedDeliveryModeException;

public class EventIntegrationTest {
    private TestHandler1 handler1;
    private TestHandler2 handler2;
    private EventEngine eventEngine;
    private EventFilter filter1;
    private EventFilter filter2;
    
    @Before
    public void setUp() {
        eventEngine = new EventEngine();
        
        handler1 = new TestHandler1();
        handler2 = new TestHandler2();
        
        filter1 = new TestFilter1();
        filter2 = new TestFilter2();
    }
    
    @Test
    public void testNonFilteredSubscription() {
        eventEngine.subscribe(new Subscription(handler1));
        eventEngine.subscribe(new Subscription(handler2));

        eventEngine.publish(null, new TestEventImpl2(), true);
        
        assertTrue(handler1.isHandled());
        assertTrue(handler2.isHandled());
    }

    @Test
    public void testNonDefaultTopic() {
        eventEngine.subscribe(new Subscription(handler1, "filtered"));
        eventEngine.subscribe(new Subscription(handler2));

        eventEngine.publish("filtered", new TestEventImpl2(), true);
        
        assertTrue(handler1.isHandled());
        assertFalse(handler2.isHandled());
    }    

    @Test
    public void testFilter() {
        eventEngine.subscribe(new Subscription(handler1, "filtered", filter1));
        eventEngine.subscribe(new Subscription(handler2, "filtered", filter2));

        eventEngine.publish("filtered", new TestEventImpl2(), true);
        
        assertTrue(handler1.isHandled());
        assertFalse(handler2.isHandled());
    }    
    
    @Test(expected=UnsupportedDeliveryModeException.class)
    public void testNonAvailableDeliveryOption() {
        eventEngine.publish(null, new TestEventImpl2(), false);        
    }

    @Test
    public void testCustomChannel() {
        eventEngine.registerChannel(new EventChannelAdapter(eventEngine) {
            /* (non-Javadoc)
             * @see org.openehealth.ipf.commons.event.EventChannelAdapter#accepts(org.openehealth.ipf.commons.event.DeliveryMode)
             */
            @Override
            public boolean accepts(DeliveryMode quality) {
                return true;
            }
        });
        
        eventEngine.subscribe(new Subscription(handler1));
        eventEngine.publish(null, new TestEventImpl2(), false);        
        assertTrue(handler1.isHandled());
    }
}
