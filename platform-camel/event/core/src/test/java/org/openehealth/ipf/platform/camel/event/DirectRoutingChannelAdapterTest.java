package org.openehealth.ipf.platform.camel.event;

import static org.junit.Assert.*;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.event.EventEngine;
import org.openehealth.ipf.commons.event.Subscription;
import org.openehealth.ipf.platform.camel.event.adapter.RoutingChannelAdapter;

public class DirectRoutingChannelAdapterTest extends BaseCamelRoutingChannelAdapterTest {
    private TestHandler1 handler;
    private ProducerTemplate producerTemplate;
    private EventEngine eventEngine;

    @Before
    public void setUp() throws Exception {
        eventEngine = new EventEngine();
        producerTemplate = getContext().createProducerTemplate();
    
        setAdapter(new RoutingChannelAdapter(eventEngine, producerTemplate, "direct:send", false));
        eventEngine.registerChannel(getAdapter());
        
        handler = new TestHandler1();
        Subscription subscription = new Subscription();
        subscription.setHandler(handler);
        eventEngine.subscribe(subscription);
    }

    @Test
    public void testDirectChannel() {
        TestEventImpl1 event = new TestEventImpl1("hello world");
        eventEngine.publish(null, event, true);
        
        TestEventImpl1 handledEvent = handler.getHandledEvent();
        assertNotNull(handledEvent);
        assertEquals("hello world", handler.getProp());
    }

    @Override
    protected void configure(RouteBuilder routeBuilder) {
        // Sender route
        routeBuilder.from("direct:send")
            .to("direct:channel");
        
        // Channel
        routeBuilder.from("direct:channel")
            .to("direct:receive");
        
        // Receiving route
        routeBuilder.from("direct:receive")
            .to("bean:adapter?method=distributeToHandlers");
    }

    @Override
    protected void addComponents() {}
}
