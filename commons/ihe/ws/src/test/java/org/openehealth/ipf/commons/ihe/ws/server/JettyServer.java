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

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
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
    public void start() {
        server = new Server();
        var connector = isSecure()
                ? secureServerConnector(getPort())
                : new ServerConnector(server);

        server.addConnector(connector);
        connector.setPort(getPort());
        var context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setResourceBase("/");
        var listener = new ContextLoaderListener();

        context.getInitParams().put("contextConfigLocation", getContextResource());
        context.addEventListener(listener);

        context.setContextPath(getContextPath());
        var holder = new ServletHolder(getServlet());
        holder.setName(getServletName());
        context.addServlet(holder, getServletPath());

        for (var parameters : getInitParameters().entrySet()) {
            holder.setInitParameter(parameters.getKey(), parameters.getValue());
        }
        
        server.setHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private ServerConnector secureServerConnector(int port) {
        return new ServerConnector(server,
                createSecureConnectionFactory(),
                createHttpsConnectionFactory(port));
    }

    private HttpConnectionFactory createHttpsConnectionFactory(int port) {
        var httpsConfig = new HttpConfiguration();
        httpsConfig.setSecureScheme("https");
        httpsConfig.setSecurePort(port);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());
        return new HttpConnectionFactory(httpsConfig);
    }

    private SslConnectionFactory createSecureConnectionFactory() {
        var sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(getKeystoreFile());
        sslContextFactory.setKeyStorePassword(getKeystorePass());
        sslContextFactory.setTrustStorePath(getTruststoreFile());
        sslContextFactory.setTrustStorePassword(getTruststorePass());
        sslContextFactory.setNeedClientAuth(getClientAuthType() == ClientAuthType.MUST);
        sslContextFactory.setWantClientAuth(getClientAuthType() == ClientAuthType.WANT);
        var http11 = new HttpConnectionFactory();
        return new SslConnectionFactory(sslContextFactory, http11.getProtocol());
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
