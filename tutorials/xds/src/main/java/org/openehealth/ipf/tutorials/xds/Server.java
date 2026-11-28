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
package org.openehealth.ipf.tutorials.xds;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.jspecify.annotations.NonNull;
import org.openehealth.ipf.commons.ihe.ws.server.TomcatServer;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * Entry point for command line execution.
 *
 * @author Jens Riemschneider
 */
public final class Server {

    /** The port the server listens on, published to context.xml as system property {@code xds.port}. */
    private static final int PORT = 9091;

    /**
     * Holds the server's key and certificate, and also serves as the trust store of the repository when it
     * forwards to the registry over TLS -- this tutorial is both, so it has to trust itself. Its
     * certificate is issued for {@code localhost}. Published to context.xml as {@code xds.keystore}.
     */
    private static final String KEYSTORE = "keystore";
    private static final String KEYSTORE_PASSWORD = "changeit";

    private Server() {
    }

    /**
     * Starts the registry and repository on port 9091 and runs until the process is stopped.
     *
     * @param args {@code secure} to serve over TLS
     */
    public static void main(String[] args) throws Exception {
        start(PORT, args.length == 1 && "secure".equals(args[0]));
        Thread.currentThread().join();
    }

    /**
     * Starts the registry and repository.
     *
     * @param port   the port to listen on
     * @param secure whether to serve over TLS. The repository then forwards ITI-41 submissions to the
     *               registry over TLS as well.
     * @return the running server
     */
    public static TomcatServer start(int port, boolean secure) throws Exception {
        // read by context.xml, which builds the registry endpoint of the repository from them
        var keystore = new File(KEYSTORE).getAbsoluteFile();
        System.setProperty("xds.port", String.valueOf(port));
        System.setProperty("xds.secure", String.valueOf(secure));
        System.setProperty("xds.keystore", keystore.toURI().toString());

        var servletServer = createServletServer(port, secure, keystore);
        servletServer.start();
        return servletServer;
    }

    private static @NonNull TomcatServer createServletServer(int port, boolean secure, File keystore) throws IOException {
        var servletServer = new TomcatServer();
        servletServer.setContextResource(new ClassPathResource("context.xml").getURI().toString());
        servletServer.setPort(port);
        servletServer.setContextPath("");
        servletServer.setServletPath("/*");
        servletServer.setServlet(new CXFServlet());
        servletServer.setSecure(secure);
        servletServer.setKeystoreFile(keystore.getPath());
        servletServer.setKeystorePass(KEYSTORE_PASSWORD);
        servletServer.setTruststoreFile(keystore.getPath());
        servletServer.setTruststorePass(KEYSTORE_PASSWORD);
        return servletServer;
    }
}
