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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.server;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.loader.VirtualWebappLoader;
import org.apache.catalina.startup.Embedded;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.InetAddress;

/**
 * A servlet server based on the embedded Tomcat.
 * <p>
 * Note: any exceptions thrown are treated as assertion failures.
 * @author Jens Riemschneider
 */
public class TomcatServer extends ServletServer {
    private static final Log log = LogFactory.getLog(TomcatServer.class);

    private Embedded embedded;
    private Wrapper wrapper;
    
    @Override
    public void start() {
        embedded = new Embedded();

        Context context = embedded.createContext(getContextPath(), new File(".").getAbsolutePath());
        TomcatServletWrapper.setServlet(getServlet());
        context.setWrapperClass(TomcatServletWrapper.class.getName());

        wrapper = context.createWrapper();
        wrapper.setName("servlet");        
        wrapper.setServletClass(getServlet().getClass().getName());

        context.addChild(wrapper);
        context.addServletMapping(getServletPath(), "servlet");

        VirtualWebappLoader loader = new VirtualWebappLoader(this.getClass().getClassLoader());
        loader.setVirtualClasspath(System.getProperty("java.class.path"));
        context.setLoader(loader);
        
        Host host = embedded.createHost("localhost", new File(".").getAbsolutePath());
        host.addChild(context);
        
        Engine engine = embedded.createEngine();
        engine.addChild(host);
        engine.setDefaultHost(host.getName());
        embedded.addEngine(engine);
        
        Connector connector = embedded.createConnector((InetAddress)null, getPort(), false);
        embedded.addConnector(connector);

        embedded.setAwait(true);
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
