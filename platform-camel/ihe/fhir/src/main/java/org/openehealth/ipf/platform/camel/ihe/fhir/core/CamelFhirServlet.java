package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.rest.server.RestfulServer;
import org.apache.camel.component.http.HttpServletResolveConsumerStrategy;
import org.apache.camel.component.http.ServletResolveConsumerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class CamelFhirServlet extends RestfulServer {

    private static final Logger LOG = LoggerFactory.getLogger(CamelFhirServlet.class);
    private final ConcurrentMap<String, FhirConsumer> consumers = new ConcurrentHashMap<>();

    private ServletResolveConsumerStrategy servletResolveConsumerStrategy = new HttpServletResolveConsumerStrategy();
    private String servletName;

    /**
     * RestfulServer assumes that all resource providers are known at init time, which is not the case here.
     *
     * @param config servlet config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.servletName = config.getServletName();
    }

    /**
     * Called upon initialization of the servlet, which is too early to know about the existing FHIR consumers
     * initialization of CAmel routes and endpoints.
     *
     * @throws ServletException
     */
    @Override
    protected void initialize() throws ServletException {
        super.initialize();
    }

    public void connect(FhirConsumer consumer) {
        LOG.debug("Connecting FHIR consumer: {}", consumer);
        consumers.put(consumer.getEndpoint().getEndpointUri(), consumer);

        // Add the resource providers for this consumer, but servlet is already initialized
        // Also inject consumer into resource provider

    }

    public void disconnect(FhirConsumer consumer) {
        LOG.debug("Disconnecting FHIR consumer: {}", consumer);
        consumers.remove(consumer.getEndpoint().getEndpointUri());

        // Remove the resource providers for this consumer
        // unregisterProviders(providers);
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public ServletResolveConsumerStrategy getServletResolveConsumerStrategy() {
        return servletResolveConsumerStrategy;
    }

    public void setServletResolveConsumerStrategy(ServletResolveConsumerStrategy servletResolveConsumerStrategy) {
        this.servletResolveConsumerStrategy = servletResolveConsumerStrategy;
    }

    public Map<String, FhirConsumer> getConsumers() {
        return Collections.unmodifiableMap(consumers);
    }
}
