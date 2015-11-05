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

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CamelFhirServlet extends RestfulServer {

    private static final Logger LOG = LoggerFactory.getLogger(CamelFhirServlet.class);
    private static final Map<String, List<IResourceProvider>> PROVIDERS = new HashMap<>();
    private static final Map<String, CamelFhirServlet> SERVLETS = new HashMap<>();

    private static final String SERVLET_LOGGING_PARAMETER_NAME = "logging";
    private static final String SERVLET_RESPONSE_HIGHLIGHTING_PARAMETER_NAME = "highlight";

    static final String DEFAULT_SERVLET_NAME = "FhirServlet";


    private String servletName = DEFAULT_SERVLET_NAME;
    private boolean logging;
    private boolean responseHighlighting;

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

        logging = Boolean.parseBoolean(config.getInitParameter(SERVLET_LOGGING_PARAMETER_NAME));
        responseHighlighting = Boolean.parseBoolean(config.getInitParameter(SERVLET_RESPONSE_HIGHLIGHTING_PARAMETER_NAME));

        // This must happen afterwards, because it calls initialize()
        super.init(config);
    }

    /**
     * Called upon initialization of the servlet, which is too early to know about the existing FHIR consumers
     * initialization of Camel routes and endpoints.
     *
     * @throws ServletException
     */
    @Override
    protected void initialize() throws ServletException {
        setResourceProviders(PROVIDERS.get(getServletName()));

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
    }

    public static void registerProvider(String name, AbstractResourceProvider provider) {
        if (!PROVIDERS.containsKey(name)) {
            PROVIDERS.put(name, new ArrayList<IResourceProvider>());
        }
        PROVIDERS.get(name).add(provider);
    }

    public static void unregisterProvider(String name, AbstractResourceProvider provider) throws Exception {
        PROVIDERS.get(name).remove(provider);
        SERVLETS.get(name).unregisterProvider(provider);
    }

    public String getServletName() {
        return servletName;
    }

}
