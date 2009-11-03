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

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
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
        Context context = new Context(Context.NO_SESSIONS);
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
        SslSocketConnector sslConnector = new SslSocketConnector();
        sslConnector.setKeystore(getKeystoreFile());
        sslConnector.setKeyPassword(getKeystorePass());
        sslConnector.setTruststore(getTruststoreFile());
        sslConnector.setTrustPassword(getTruststorePass());
        return sslConnector;
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
