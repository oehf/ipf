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
    private static ProducerTemplate producerTemplate;
    private static ServletServer servletServer;
    private static ApplicationContext appContext;

    private static final int SYSLOG_PORT = 8888;
    private static UdpServer syslog;
    private static CamelContext camelContext;
    
    public static void startServer(Servlet servlet, String appContextName, boolean secure) throws Exception {
        ClassPathResource contextResource = new ClassPathResource(appContextName);
        
        servletServer = new TomcatServer();
        servletServer.setContextResource(contextResource.getURI().toString());
        servletServer.setPort(9091);
        servletServer.setContextPath("");
        servletServer.setServletPath("/*");
        servletServer.setServlet(servlet);
        servletServer.setSecure(secure);
        servletServer.setKeystoreFile("keystore");
        servletServer.setKeystorePass("changeit");
        servletServer.setTruststoreFile("keystore");
        servletServer.setTruststorePass("changeit");
        servletServer.start();
        
        ServletContext servletContext = servlet.getServletConfig().getServletContext();
        appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        producerTemplate = (ProducerTemplate)appContext.getBean("producerTemplate", ProducerTemplate.class);  
        camelContext = (CamelContext)appContext.getBean("camelContext", CamelContext.class);  

        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(SYSLOG_PORT);

        syslog = new UdpServer(SYSLOG_PORT);
        syslog.start();
    }

    public static void startServer(Servlet servlet, String appContextName) throws Exception {
        startServer(servlet, appContextName, false);
    }
    
    @AfterClass
    public static void stopServer() throws Exception {
        if (syslog != null) {
            syslog.cancel();
            syslog.join();
        }

        if (servletServer != null) {
            servletServer.stop();
        }
    }

    /**
     * @return the sys-log server being used in this test.
     */
    public static UdpServer getSyslog() {
        return syslog;
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
        
        getCxfBus().getOutInterceptors().add(outboundInterceptor);
        getCxfBus().getInInterceptors().add(inboundInterceptor);
    }

    /**
     * Returns a CXF bus where interceptors can be installed.
     * @return
     *      the bus instance
     */
    public static CXFBusImpl getCxfBus() {
        JaxWsServerFactoryBean jaxwsBean = new JaxWsServerFactoryBean();
        CXFBusImpl bus = (CXFBusImpl)jaxwsBean.getBus();
        return bus;
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
        return Exchanges.resultMessage(send(endpoint, in)).getBody(outType);
    }

    /**
     * Sends the given object to the endpoint.
     * @param endpoint
     *          the endpoint to send the object to.
     * @param in
     *          the input object.
     * @return the resulting exchange.
     */
    protected Exchange send(String endpoint, Object in) {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(in);        
        return producerTemplate.send(endpoint, exchange);        
    }
}