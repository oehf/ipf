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
package org.openehealth.ipf.platform.camel.ihe.xds.commons;

import java.io.File;
import java.lang.reflect.Constructor;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.AfterClass;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.server.ServletServer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.server.TomcatServer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.server.UdpServer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.AuditTestFinalInterceptor;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Base class for tests that are run within an embedded web container.
 * This class requires that an application context named "context.xml" is
 * placed within the root of the test resources.
 * @author Jens Riemschneider
 */
public class StandardTestContainer {
    private static ProducerTemplate<Exchange> producerTemplate;
    private static ServletServer servletServer;
    private static ApplicationContext appContext;

    private static final int SYSLOG_PORT = 8888;
    private static UdpServer syslog;
    private static CamelContext camelContext;
    
    @SuppressWarnings("unchecked")  // Because of getting beans of generified classes.
    public static void startServer(Servlet servlet, String appContextName) throws Exception {
        File contextFile = new ClassPathResource(appContextName).getFile();
        
        servletServer = new TomcatServer();
        servletServer.setContextFile(contextFile);
        servletServer.setPort(9091);
        servletServer.setContextPath("");
        servletServer.setServletPath("/*");
        servletServer.setServlet(servlet);
        servletServer.start();
        
        ServletContext servletContext = servlet.getServletConfig().getServletContext();
        appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        producerTemplate = (ProducerTemplate<Exchange>)appContext.getBean("producerTemplate", ProducerTemplate.class);  
        camelContext = (CamelContext)appContext.getBean("camelContext", CamelContext.class);  

        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(SYSLOG_PORT);

        syslog = new UdpServer(SYSLOG_PORT);
        syslog.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        syslog.cancel();
        syslog.join();

        if (servletServer != null) {
            servletServer.stop();
        }
    }

    public static UdpServer getSyslog() {
        return syslog;
    }

    public static void setSyslog(UdpServer syslog) {
        StandardTestContainer.syslog = syslog;
    }

    /**
     * Installs transaction-specific audit test interceptors.
     * 
     * @param <T>
     *      class of interceptor     
     * @param interceptorClass
     *      class of interceptor     
     * @throws Exception
     *      when reflection fails
     */
    public static <T extends AuditTestFinalInterceptor> void 
        installTestInterceptors(Class<T> interceptorClass) throws Exception
    {
        Constructor<T> constructor = interceptorClass.getConstructor(boolean.class);
       
        T inboundInterceptor = constructor.newInstance(false);
        T outboundInterceptor = constructor.newInstance(true);
        
        JaxWsServerFactoryBean jaxwsBean = new JaxWsServerFactoryBean();
        CXFBusImpl bus = (CXFBusImpl)jaxwsBean.getBus();
        bus.getOutInterceptors().add(outboundInterceptor);
        bus.getInInterceptors().add(inboundInterceptor);
    }

    /**
     * Sends the given object to the endpoint.
     * @param endpoint
     *          the endpoint to send the object to.
     * @param in
     *          the input object.
     * @param outType
     *          the type of the output object.
     * @return the output object.
     */
    protected <T> T send(String endpoint, Object in, Class<T> outType) {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(in);        
        Exchange result = producerTemplate.send(endpoint, exchange);
        return Exchanges.resultMessage(result).getBody(outType);
    }
}