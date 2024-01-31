/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.audit.server;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

import java.io.Closeable;
import java.io.IOException;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.servlet;

public class TLSBalpRepository implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(TLSBalpRepository.class);
    protected final TlsParameters tlsParameters;
    private Undertow server;
    private final int httpsPort;

    public TLSBalpRepository(TlsParameters tlsParameters, int httpsPort) {
        this.tlsParameters = tlsParameters;
        this.httpsPort = httpsPort;
    }

    public TLSBalpRepository(int httpsPort) {
        this.tlsParameters = TlsParameters.getDefault();
        this.httpsPort = httpsPort;
    }

    @Override
    public void close() throws IOException {
        stop();
    }

    public void stop() {
        if (server != null) server.stop();
        LOG.info("successfully stopped FHIR Audit Server");
    }

    public Undertow start() throws ServletException {
        DeploymentInfo servletBuilder = deployment()
            .setClassLoader(FhirAuditRepository.class.getClassLoader())
            .setContextPath("/fhir")
            .setDeploymentName("FHIR-Deployment")
            .addServlets(
                servlet("FhirAuditServer", FhirAuditServer.class, new FhirServletInitiator(new FhirAuditServer()))
                    .addMapping("/*"));

        DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
        manager.deploy();

        HttpHandler servletHandler = manager.start();
        PathHandler path = Handlers
            .path(Handlers.redirect("/"))
            .addPrefixPath("/", servletHandler);
        server = Undertow.builder()
            .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
            .addHttpsListener(
                httpsPort,"localhost", tlsParameters.getSSLContext(true))
            .setHandler(path)
            .build();
        server.start();
        LOG.info("successfully started FHIR Audit Server on port {}", httpsPort);
        return server;
    }
    static class FhirServletInitiator implements InstanceFactory<FhirAuditServer> {

        private final FhirAuditServer fhirAuditServer;

        public FhirServletInitiator(FhirAuditServer fhirAuditServer) {
            this.fhirAuditServer = fhirAuditServer;
        }

        @Override
        public InstanceHandle<FhirAuditServer> createInstance() throws InstantiationException {
            return new InstanceHandle<>() {
                @Override
                public FhirAuditServer getInstance() {
                    return fhirAuditServer;
                }

                @Override
                public void release() {

                }
            };
        }
    }

}
