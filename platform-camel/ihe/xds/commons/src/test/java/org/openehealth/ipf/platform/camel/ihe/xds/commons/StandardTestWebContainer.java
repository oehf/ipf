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

import org.apache.camel.ProducerTemplate;
import org.apache.cxf.bus.CXFBusImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.AfterClass;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.server.ServletServer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.server.TomcatServer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.AuditTestFinalInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Base class for tests that are run within an embedded web container.
 * This class requires that an application context named "context.xml" is
 * placed within the root of the test resources.
 * @author Jens Riemschneider
 */
public class StandardTestWebContainer {
    private static ProducerTemplate<?> producerTemplate;
    private static ServletServer servletServer;
    private static ApplicationContext appContext;

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
        producerTemplate = (ProducerTemplate<?>)appContext.getBean("producerTemplate", ProducerTemplate.class);  
    }

    @AfterClass
    public static void stopServer() throws Exception {
        if (servletServer != null) {
            servletServer.stop();
        }
    }

    public static ProducerTemplate<?> getProducerTemplate() {
        return producerTemplate;
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
}