package org.openehealth.ipf.commons.ihe.fhir.audit.protocol;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.audit.CustomTlsParameters;
import org.openehealth.ipf.commons.audit.DefaultBalpAuditContext;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.ApplicationActivityBuilder;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(FhirAuditRepository.class)
public abstract class AbstractFhirRestTLSSenderIntegrationTest {

    protected DefaultBalpAuditContext auditContext;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractFhirRestTLSSenderIntegrationTest.class);

    @BeforeEach
    public void setup() {
        this.auditContext = new DefaultBalpAuditContext();
        auditContext.setAuditRepositoryPort(FhirAuditRepository.getServerHttpsPort());
        auditContext.setAuditRepositoryContextPath(FhirAuditRepository.getServerContextPath());
        auditContext.setAuditRepositoryHost("localhost");
        auditContext.setAuditEnabled(true);
        auditContext.setSerializationStrategy((auditMessage, writer, pretty) -> writer.write("<AuditEvent />"));
        var defaultTls = setupDefaultTlsParameter();
        auditContext.setTlsParameters(defaultTls);
    }

    TlsParameters setupDefaultTlsParameter() {
        try {
            var tlsParameters = new CustomTlsParameters();
            tlsParameters.setKeyStoreFile(Paths.get(AbstractFhirRestTLSSenderIntegrationTest.class.getResource("/security/client.p12").toURI()).toString());
            tlsParameters.setKeyStorePassword("init");
            tlsParameters.setTrustStoreFile(Paths.get(AbstractFhirRestTLSSenderIntegrationTest.class.getResource("/security/ca.keystore").toURI()).toString());
            tlsParameters.setTrustStorePassword("initinit");
            tlsParameters.setEnabledProtocols("TLSv1.2,TLSv1.3");
            return tlsParameters;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        LOG.info("FhirAuditRepository size: " + FhirAuditRepository.getAuditEvents().size() + ". Cleanup....");
        FhirAuditRepository.clearAuditEvents();
        LOG.info("FhirAuditRepository size: " + FhirAuditRepository.getAuditEvents().size());
    }

    @Test
    public void testTwoWayTLSFlooding() throws Exception {
        var count = 500;
        var threads = 2;
        var executor = Executors.newFixedThreadPool(threads);

        IntStream.range(0, count).forEach(i -> executor.execute(() -> sendAudit(Integer.toString(i))));
        CountDownLatch latch = new CountDownLatch(1);
        Executors.newSingleThreadExecutor().execute(new ExpectationRunnable(latch, count));
        assertTrue(latch.await(10, TimeUnit.SECONDS));
    }

    void sendAudit(String userName) {
        LOG.debug("Sending audit record");
        auditContext.audit(
            new ApplicationActivityBuilder.ApplicationStop(EventOutcomeIndicator.Success)
                .setAuditSource(auditContext)
                .setApplicationParticipant(
                    userName,
                    null,
                    null,
                    AuditUtils.getLocalHostName())
                .addApplicationStarterParticipant(System.getProperty("user.name"))
                .getMessages());
    }

    static class ExpectationRunnable implements Runnable {

        private final CountDownLatch latch;

        private final int expectedMessagesCount;

        public ExpectationRunnable(CountDownLatch latch, int expectedMessagesCount) {
            this.latch = latch;
            this.expectedMessagesCount = expectedMessagesCount;
        }

        @Override
        public void run() {
            while (FhirAuditRepository.getAuditEvents().size() < expectedMessagesCount) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            latch.countDown();
        }
    }
}
