package org.openehealth.ipf.commons.event;

import static org.junit.Assert.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.event.DeliveryMode;
import org.openehealth.ipf.commons.event.EventChannelAdapter;
import org.openehealth.ipf.commons.event.EventEngine;
import org.openehealth.ipf.commons.event.EventFilter;
import org.openehealth.ipf.commons.event.Subscription;

public class EventIntegrationTest {
    private MyHandler1 handler1;
    private MyHandler2 handler2;
    private EventEngine eventEngine;
    private EventFilter filter1;
    private EventFilter filter2;
    
    @Before
    public void setUp() {
        eventEngine = new EventEngine();
        
        handler1 = new MyHandler1();
        handler2 = new MyHandler2();
        
        filter1 = new MyFilter1();
        filter2 = new MyFilter2();
    }
    
    @Test
    public void testNonFilteredSubscription() {
        eventEngine.subscribe(new Subscription(handler1));
        eventEngine.subscribe(new Subscription(handler2));

        eventEngine.publish(null, new MyEventImpl2(), true);
        
        assertTrue(handler1.isHandled());
        assertTrue(handler2.isHandled());
    }

    @Test
    public void testNonDefaultTopic() {
        eventEngine.subscribe(new Subscription(handler1, "filtered"));
        eventEngine.subscribe(new Subscription(handler2));

        eventEngine.publish("filtered", new MyEventImpl2(), true);
        
        assertTrue(handler1.isHandled());
        assertFalse(handler2.isHandled());
    }    

    @Test
    public void testFilter() {
        eventEngine.subscribe(new Subscription(handler1, "filtered", filter1));
        eventEngine.subscribe(new Subscription(handler2, "filtered", filter2));

        eventEngine.publish("filtered", new MyEventImpl2(), true);
        
        assertTrue(handler1.isHandled());
        assertFalse(handler2.isHandled());
    }    
    
    @Test
    public void testAsyncDeliveryOption() throws Throwable {
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final Throwable[] exception = new Throwable[] { null };
        final boolean[] handled = new boolean[] { false };
        
        eventEngine.subscribe(new Subscription(new EventHandler() {
            @Override
            public void handle(EventObject eventObject) {
                handled[0] = true;
                try {
                    barrier.await(1, TimeUnit.SECONDS);
                }
                catch (TimeoutException e) {
                    exception[0] = new AssertionError("Event not send asynchronously");
                }
                catch (Exception e) {
                    exception[0] = e;
                }
            }            
        }));
        
        eventEngine.publish(null, new MyEventImpl2(), false);

        try {
            barrier.await(3, TimeUnit.SECONDS);
        }
        catch (BrokenBarrierException e) {
            throw exception[0] != null ? exception[0] : e;
        }
        
        assertTrue("Handler is not called at all", handled[0]);
    }

    @Test
    public void testCustomChannel() {
        final boolean sendByCustomChannel[] = new boolean[] { false };  
        eventEngine.registerChannel(new EventChannelAdapter(eventEngine) {
            @Override
            public boolean accepts(DeliveryMode quality) {
                return true;
            }
            
            @Override
            public void send(EventObject event, DeliveryMode mode) {
                sendByCustomChannel[0] = true;
                super.send(event, mode);
            }
        });
        
        eventEngine.subscribe(new Subscription(handler1));
        eventEngine.publish(null, new MyEventImpl2(), true);        
        assertTrue("Event was not handled", handler1.isHandled());
        assertTrue("Event was not send via custom channel adapter", sendByCustomChannel[0]);
    }
}
