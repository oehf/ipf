/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.tutorials.xds

import org.apache.cxf.transport.servlet.CXFServlet
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer
import org.openehealth.ipf.commons.ihe.ws.server.TomcatServer
import org.springframework.core.io.ClassPathResource

/**
 * Entry point for command line execution.
 * @author Jens Riemschneider
 */
class Server {

    private static final int SYSLOG_PORT = 514

    /**
     * Standard main.
     * @param args
     *          used to define usage of SSL.
     * @throws Exception
     *          any problem that occurred.
     */
    static void main(String[] args) {
        ClassPathResource contextResource = new ClassPathResource('context.xml')

        CXFServlet servlet = new CXFServlet()

        ServletServer servletServer = new TomcatServer()
        servletServer.contextResource = contextResource.getURI().toString()
        servletServer.port = 9091
        servletServer.contextPath = ''
        servletServer.servletPath = '/*'
        servletServer.servlet = servlet
        servletServer.secure = args.length == 1 && args[0] == 'secure'
        servletServer.keystoreFile = 'keystore'
        servletServer.keystorePass = 'changeit'
        servletServer.truststoreFile = 'keystore'
        servletServer.truststorePass = 'changeit'
        servletServer.start()

        while (true) Thread.sleep(10000)
    }
}
