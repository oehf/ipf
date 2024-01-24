package org.openehealth.ipf.commons.ihe.fhir.extension;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openehealth.ipf.commons.audit.CustomTlsParameters;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.net.ServerSocket;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.servlet;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class FhirAuditRepository implements BeforeAllCallback, BeforeEachCallback {
    private static Undertow server;
    private ExtensionContext extensionContext;
    private static FhirAuditServer fhirAuditServer;
    private static final String STORE_KEY = "undertow";
    private static int httpsPort ;
    static final String SERVER_KEY_STORE;
    static final String SERVER_KEY_STORE_PASS = "init";
    static final String TRUST_STORE;
    static final String TRUST_STORE_PASS = "initinit";

    private static final Logger LOGGER = LoggerFactory.getLogger(FhirAuditRepository.class);

    static {
        try {
            URI s = new ClassPathResource("/security/server.p12").getURI();
            URI t = new ClassPathResource("/security/ca.keystore").getURI();
            SERVER_KEY_STORE = Paths.get(s).toAbsolutePath().toString();
            TRUST_STORE = Paths.get(t).toAbsolutePath().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    TlsParameters setupDefaultTlsParameter() {
        var tlsParameters = new CustomTlsParameters();
        tlsParameters.setKeyStoreFile(SERVER_KEY_STORE);
        tlsParameters.setKeyStorePassword(SERVER_KEY_STORE_PASS);
        tlsParameters.setTrustStoreFile(TRUST_STORE);
        tlsParameters.setTrustStorePassword(TRUST_STORE_PASS);
        tlsParameters.setEnabledProtocols("TLSv1.2,TLSv1.3");
        return tlsParameters;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        this.extensionContext = extensionContext;
        if (hasStartedUndertow()) return;

        httpsPort = freePort();
        registerShutdownHook();
    }

    private int freePort() throws Exception {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        if (server == null) {
            fhirAuditServer = new FhirAuditServer();
            DeploymentInfo servletBuilder = deployment()
                    .setClassLoader(FhirAuditRepository.class.getClassLoader())
                    .setContextPath("/")
                    .setDeploymentName("FHIR-Deployment")
                    .addServlets(
                            servlet("FhirAuditServer", FhirAuditServer.class, new FhirServletInitiator(fhirAuditServer))
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
                        httpsPort,"localhost", setupDefaultTlsParameter().getSSLContext(true))
                    .setHandler(path)
                    .build();
            server.start();
        }
    }

    private void registerShutdownHook() {
        ExtensionContext.Store.CloseableResource closeableResource = () -> {
            LOGGER.info("stopping undertow server...");
            if (server != null) server.stop();
            LOGGER.info("successfully stopped undertow server");
        };
        extensionContext.getRoot().getStore(GLOBAL).put(STORE_KEY, closeableResource);
    }

    public static int getServerHttpsPort(){
        return httpsPort;
    }
    public static List<AuditEvent> getAuditEvents() {
        return fhirAuditServer.getAuditEvents();
    }

    private boolean hasStartedUndertow(){
        return extensionContext.getRoot().getStore(GLOBAL).get(STORE_KEY) != null;
    }

    static class FhirAuditServer extends RestfulServer implements IResourceProvider {

        private final List<AuditEvent> auditEvents = new CopyOnWriteArrayList<>();

        public FhirAuditServer() {
            setFhirContext(FhirContext.forR4());
            setResourceProviders(this);
        }

        public List<AuditEvent> getAuditEvents() {
            return auditEvents;
        }

        @Create()
        public MethodOutcome create(@ResourceParam AuditEvent auditEvent) {
            auditEvents.add(auditEvent);
            return new MethodOutcome(
                new IdType(ResourceType.AuditEvent.name(),
                UUID.randomUUID().toString()), true);
        }

        @Override
        public Class<? extends IBaseResource> getResourceType() {
            return AuditEvent.class;
        }
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