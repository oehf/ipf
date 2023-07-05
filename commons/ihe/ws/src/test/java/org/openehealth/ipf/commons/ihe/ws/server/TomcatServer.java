/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.ws.server;

import org.apache.catalina.Wrapper;
import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.apache.catalina.startup.Tomcat;
import org.openehealth.ipf.commons.ihe.core.ClientAuthType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import jakarta.security.auth.message.config.AuthConfigFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A servlet server based on the embedded Tomcat.
 * <p>
 * Note: any exceptions thrown are treated as assertion failures.
 * @author Jens Riemschneider
 */
public class TomcatServer extends ServletServer {
    private static final Logger log = LoggerFactory.getLogger(TomcatServer.class);

    private static final AtomicInteger SERVLET_COUNTER = new AtomicInteger(0);
    private Tomcat embedded;
    private Wrapper wrapper;

    @Override
    public void start() {
        embedded = new Tomcat();
        AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());

        var context = embedded.addContext(getContextPath(), "/");
        context.addParameter("contextConfigLocation", getContextResource());
        context.addApplicationListener(ContextLoaderListener.class.getName());

        embedded.getHost().setAppBase("");

        // Each servlet should get an unique name, otherwise all servers will reuse
        // one and the same servlet instance.  Note that name clashes with servlets
        // created somewhere else are still possible.
        var servletName = getServletName() == null ?
                "ipf-servlet-" + SERVLET_COUNTER.getAndIncrement() :
                getServletName();

        wrapper = context.createWrapper();
        wrapper.setName(servletName);
        wrapper.setServletClass(getServlet().getClass().getName());

        for (var parameters : getInitParameters().entrySet()) {
            wrapper.addInitParameter(parameters.getKey(), parameters.getValue());
        }

        context.addChild(wrapper);
        context.addServletMappingDecoded(getServletPath(), servletName);

        /*
        VirtualWebappLoader loader = new VirtualWebappLoader(this.getClass().getClassLoader());
        loader.setVirtualClasspath(System.getProperty("java.class.path"));
        context.setLoader(loader);
        */
        var connector = embedded.getConnector();
        connector.setPort(getPort());
        if (isSecure()) {
            connector.setSecure(true);
            connector.setScheme("https");
            connector.setProperty("SSLEnabled", "true");
            connector.setProperty("sslProtocol", "TLS");
            connector.setProperty("keystoreFile", getKeystoreFile());
            connector.setProperty("keystorePass", getKeystorePass());
            connector.setProperty("truststoreFile", getTruststoreFile());
            connector.setProperty("truststorePass", getTruststorePass());
            if (getClientAuthType() == ClientAuthType.MUST) {
                connector.setProperty("clientAuth", "true");
            } else if (getClientAuthType() == ClientAuthType.WANT) {
                connector.setProperty("clientAuth", "want");
            }
        }

        try {
            embedded.start();
            wrapper.allocate();
            log.info("Started embedded Tomcat server");
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void stop() {
        if (embedded != null) {
            try {
                wrapper.deallocate(getServlet());
                embedded.stop();
                log.info("Stopped embedded Tomcat server");
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }
}
