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
package org.openehealth.ipf.platform.camel.ihe.ws

import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.impl.DefaultExchange
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.AfterClass
import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.queue.AbstractMockedAuditMessageQueue
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

/**
 * Base class for tests that are run within an embedded web container.
 * This class requires that an application context named "context.xml" is
 * placed within the root of the test resources.
 * @author Jens Riemschneider
 */
class StandardTestContainer {
    private static final transient Logger log = LoggerFactory.getLogger(StandardTestContainer.class)

    static ProducerTemplate producerTemplate
    static ServletServer servletServer
    static ApplicationContext appContext
    private static AbstractMockedAuditMessageQueue auditSender

    static CamelContext camelContext

    static int port

    /*
     *  Port to be used, when the test is subclassed by java applications.
     */
    public static int DEMO_APP_PORT = 8999

    static void startServer(servlet, String appContextName, boolean secure, int serverPort, String servletName = null) {
        URL contextResource = StandardTestContainer.class.getResource(appContextName.startsWith("/") ? appContextName : "/" + appContextName)

        port = serverPort
        log.info("Publishing services on port: ${port}")

        servletServer = new JettyServer(
                contextResource: contextResource.toURI().toString(),
                port: port,
                contextPath: '/',
                servletPath: '/*',
                servlet: servlet,
                servletName: servletName,
                secure: secure,
                keystoreFile: 'server.jks',
                keystorePass: 'changeit',
                truststoreFile: 'server.jks',
                truststorePass: 'changeit')

        servletServer.start()

        def servletContext = servlet.servletConfig.servletContext
        appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)
        producerTemplate = appContext.getBean('template', ProducerTemplate.class)
        camelContext = appContext.getBean('camelContext', CamelContext.class)
        auditSender = appContext.getBean('auditContext', AuditContext.class).getAuditMessageQueue()

    }

    static AbstractMockedAuditMessageQueue getAuditSender() {
        return auditSender
    }

    static int startServer(servlet, String appContextName, String servletName = null) {
        startServer(servlet, appContextName, false, servletName)
    }

    static int startServer(servlet, String appContextName, boolean secure, String servletName = null) {
        int port = JettyServer.freePort
        startServer(servlet, appContextName, secure, port, servletName)
        port
    }


    @AfterClass
    static void stopServer() {
        if (servletServer != null) {
            servletServer.stop()
        }
    }

    @After
    void tearDown() {
        if (servletServer == null) {
            String msg = "The test server is not started!\n"
            msg += "You should call startServer(...) in the @BeforeClass method of your test to initialize the server."
            throw new IllegalStateException(msg)
        }
        auditSender.clear()
    }

    /**
     * Sends the given object to the endpoint.
     * @param endpoint
     *          the endpoint to send the object to.
     * @param input
     *          the input object.
     * @param outType
     *          the type of the output object.
     * @param headers
     *          optional Camel request message headers.
     * @return the output object.
     */
    def send(endpoint, input, outType, Map headers = null) {
        Exchange result = send(endpoint, input, headers)
        Exchanges.resultMessage(result).getBody(outType)
    }

    /**
     * Sends the given object to the endpoint.
     * @param endpoint
     *          the endpoint to send the object to.
     * @param input
     *          the input object.
     * @param headers
     *          optional Camel request message headers.
     * @return the resulting exchange.
     */
    def send(endpoint, input, Map headers = null) {
        def exchange = new DefaultExchange(camelContext)
        exchange.in.body = input
        if (headers) {
            exchange.in.headers.putAll(headers)
        }
        Exchange result = producerTemplate.send(endpoint, exchange)
        if (result.exception) {
            throw result.exception
        }
        return result
    }

    static String readFile(String fn) {
        InputStream inputStream
        String s = null
        try {
            inputStream = StandardTestContainer.class.classLoader.getResourceAsStream(fn)
            s = IOUtils.toString(inputStream)
        } finally {
            IOUtils.closeQuietly(inputStream)
            return s
        }
    }
}
