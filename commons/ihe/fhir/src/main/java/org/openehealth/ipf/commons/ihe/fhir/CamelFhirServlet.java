/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * HAPI FHIR {@link RestfulServer} implementation, adding a few configuration bits using servlet
 * init parameters:
 * <ul>
 *     <li>logging (boolean): add global logging interceptor</li>
 *     <li>highlight (boolean)</li>: add response formatting if request was issued from a browser
 *     <li>pretty (boolean)</li>: pretty-print the response
 *     <li>pagingProviderSize (integer)</li>: maximum number of concurrent paging requests
 * </ul>
 *
 * @since 3.1
 */
public class CamelFhirServlet extends RestfulServer {

    private static final Logger LOG = LoggerFactory.getLogger(CamelFhirServlet.class);
    private static final Map<String, Collection<IResourceProvider>> RESOURCE_PROVIDERS = new HashMap<>();
    private static final Map<String, Collection<Object>> PLAIN_PROVIDERS = new HashMap<>();
    private static final Map<String, CamelFhirServlet> SERVLETS = new HashMap<>();

    private static final String SERVLET_LOGGING_PARAMETER_NAME = "logging";
    private static final String SERVLET_RESPONSE_HIGHLIGHTING_PARAMETER_NAME = "highlight";
    private static final String SERVLET_PRETTY_PRINT_PARAMETER_NAME = "pretty";
    private static final String SERVLET_PAGING_PROVIDER_SIZE_PARAMETER_NAME = "pagingProviderSize";

    public static final String DEFAULT_SERVLET_NAME = "FhirServlet";


    private String servletName = DEFAULT_SERVLET_NAME;
    private boolean logging;
    private boolean responseHighlighting;
    private boolean prettyPrint;
    private int pagingProviderSize = 100;

    public CamelFhirServlet() {
        super();
    }

    /**
     * RestfulServer assumes that all resource providers are known at init time, which is not the case here.
     *
     * @param config servlet config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletName = config.getServletName();
        SERVLETS.put(servletName, this);
        LOG.debug("Initializing CamelFhirServlet " + servletName);

        logging = Boolean.parseBoolean(config.getInitParameter(SERVLET_LOGGING_PARAMETER_NAME));
        responseHighlighting = Boolean.parseBoolean(config.getInitParameter(SERVLET_RESPONSE_HIGHLIGHTING_PARAMETER_NAME));
        prettyPrint = Boolean.parseBoolean(config.getInitParameter(SERVLET_PRETTY_PRINT_PARAMETER_NAME));
        String pagingProviderSizeParameter = config.getInitParameter(SERVLET_PAGING_PROVIDER_SIZE_PARAMETER_NAME);

        if (pagingProviderSizeParameter != null && !pagingProviderSizeParameter.isEmpty()) {
            pagingProviderSize = Integer.parseInt(pagingProviderSizeParameter);
        }

        // This must happen after setting all the attributes above, because it calls initialize() that
        // access these attributes.
        super.init(config);
    }

    /**
     * Returns the instance of {@link IPagingProvider} to be used. This implemention returns {@link FifoMemoryPagingProvider},
     * you may overwrite this e.g. to add a provider backed by a decent Cache implementation.
     *
     * @param pagingProviderSize maximum number of parallel paged requests. Note that each request may have an
     *                           aribitrary number of result resources though.
     * @return implementation of {@link IPagingProvider}
     *
     * @see IPagingProvider
     */
    protected IPagingProvider getDefaultPagingProvider(int pagingProviderSize) {
        return new FifoMemoryPagingProvider(pagingProviderSize);
    }

    /**
     * Called upon initialization of the servlet, which is too early to know about the existing FHIR consumers
     * initialization of Camel routes and endpoints.
     *
     * @throws ServletException
     */
    @Override
    protected void initialize() throws ServletException {
        setFhirContext(FhirContext.forDstu2Hl7Org());
        setResourceProviders(RESOURCE_PROVIDERS.get(getServletName()));
        setPlainProviders(PLAIN_PROVIDERS.get(getServletName()));

        if (logging) {
            LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
            registerInterceptor(loggingInterceptor);
            loggingInterceptor.setLoggerName(CamelFhirServlet.class.getName());

            // This is the format for each line. A number of substitution variables may
            // be used here. See the JavaDoc for LoggingInterceptor for information on
            // what is available.
            loggingInterceptor.setMessageFormat("Source[${remoteAddr}] Operation[${operationType} ${idOrResourceName}] User-Agent[${requestHeader.user-agent}] Params[${requestParameters}]");
        }

        if (responseHighlighting) {
            ResponseHighlighterInterceptor interceptor = new ResponseHighlighterInterceptor();
            registerInterceptor(interceptor);
        }

        setPagingProvider(getDefaultPagingProvider(pagingProviderSize));
        setDefaultPrettyPrint(prettyPrint);

        /*
         * Use a narrative generator. This is a completely optional step,
		 * but can be useful as it causes HAPI to generate narratives for
		 * resources which don't otherwise have one.
		 */
        //INarrativeGenerator narrativeGen = new DefaultThymeleafNarrativeGenerator();
        //getFhirContext().setNarrativeGenerator(narrativeGen);

    }

    public static void registerProvider(String name, AbstractPlainProvider provider) {
        if (provider instanceof IResourceProvider) {
            if (!RESOURCE_PROVIDERS.containsKey(name)) {
                RESOURCE_PROVIDERS.put(name, new ArrayList<IResourceProvider>());
            }
            RESOURCE_PROVIDERS.get(name).add((IResourceProvider) provider);
        } else {
            if (!PLAIN_PROVIDERS.containsKey(name)) {
                PLAIN_PROVIDERS.put(name, new ArrayList<>());
            }
            PLAIN_PROVIDERS.get(name).add(provider);
        }
    }

    public static void unregisterProvider(String name, AbstractPlainProvider provider) throws Exception {
        if (provider instanceof IResourceProvider) {
            RESOURCE_PROVIDERS.get(name).remove(provider);
        } else {
            PLAIN_PROVIDERS.get(name).remove(provider);
        }
        SERVLETS.get(name).unregisterProvider(provider);
    }

    public String getServletName() {
        return servletName;
    }

}
