package org.openehealth.ipf.platform.camel.event;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.BeanComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.junit.Before;
import org.openehealth.ipf.platform.camel.event.adapter.RoutingChannelAdapter;

public abstract class BaseCamelRoutingChannelAdapterTest {

    private CamelContext context;
    private RoutingChannelAdapter adapter;

    @Before
    public void setUpRouting() throws Exception {
        context = new DefaultCamelContext(new CamelRegistry());

        context.addComponent("bean", new BeanComponent());
        
        addComponents();
        
        context.addRoutes(new CamelRoutes());
        context.start();
    }

    protected abstract void addComponents();

    public CamelContext getContext() {
        return context;
    }
    
    public RoutingChannelAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RoutingChannelAdapter adapter) {
        this.adapter = adapter;
    }

    protected abstract void configure(RouteBuilder routeBuilder);
    
    final class CamelRoutes extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            BaseCamelRoutingChannelAdapterTest.this.configure(this);
        }
    }

    final class CamelRegistry extends JndiRegistry {
        @Override
        public Object lookup(String name) {
            if ("adapter".equals(name)) {
                return adapter; 
            }
            return super.lookup(name);
        }

        @Override
        public <T> T lookup(String name, Class<T> type) {
            if ("adapter".equals(name) && type.equals(RoutingChannelAdapter.class)) {
                return type.cast(adapter); 
            }
            return super.lookup(name, type);
        }
    }
}