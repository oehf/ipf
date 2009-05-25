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
package org.openehealth.ipf.platform.camel.ihe.xdsb.commons.server;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

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
        Connector connector = new SelectChannelConnector();
        server = new Server();
        server.addConnector(connector);
        connector.setPort(getPort());
        Context context = new Context(Context.NO_SESSIONS);

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
