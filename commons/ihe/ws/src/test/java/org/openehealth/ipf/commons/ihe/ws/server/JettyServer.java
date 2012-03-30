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

import org.eclipse.jetty.http.ssl.SslContextFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.openehealth.ipf.commons.ihe.core.ClientAuthType;
import org.springframework.web.context.ContextLoaderListener;

/**
 * A servlet server based on Jetty.
 * <p>
 * Note: any exceptions thrown are treated as assertion failures.
 * @author Jens Riemschneider
 */
public class JettyServer extends ServletServer {
    private Server server;

    @Override
    @SuppressWarnings("unchecked")  // Required by getInitParams implementation
    public void start() {
        Connector connector = isSecure() ? createSecureConnector() : new SelectChannelConnector(); 
        
        server = new Server();
        server.addConnector(connector);
        connector.setPort(getPort());
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setResourceBase("/");
        ContextLoaderListener listener = new ContextLoaderListener();

        context.getInitParams().put("contextConfigLocation", getContextResource());
        context.addEventListener(listener);

        context.setContextPath(getContextPath());
        ServletHolder holder = new ServletHolder(getServlet());
        context.addServlet(holder, getServletPath());
        
        server.setHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private SslSocketConnector createSecureConnector() {
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(getKeystoreFile());
        sslContextFactory.setKeyStorePassword(getKeystorePass());
        sslContextFactory.setTrustStore(getTruststoreFile());
        sslContextFactory.setTrustStorePassword(getTruststorePass());
        sslContextFactory.setNeedClientAuth(getClientAuthType() == ClientAuthType.MUST);
        sslContextFactory.setWantClientAuth(getClientAuthType() == ClientAuthType.WANT);
        return new SslSocketConnector(sslContextFactory);
    }

    @Override
    public void stop() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }
}
