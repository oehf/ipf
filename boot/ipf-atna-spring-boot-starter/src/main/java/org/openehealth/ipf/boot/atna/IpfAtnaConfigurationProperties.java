/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.boot.atna;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.AuditMessagePostProcessor;
import org.openehealth.ipf.commons.audit.FhirAuditDatasetEnricher;
import org.openehealth.ipf.commons.audit.WsAuditDatasetEnricher;
import org.openehealth.ipf.commons.audit.codes.AuditSourceType;
import org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler;
import org.openehealth.ipf.commons.audit.handler.LoggingAuditExceptionHandler;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.openehealth.ipf.commons.audit.queue.SynchronousAuditMessageQueue;
import org.openehealth.ipf.commons.audit.types.AuditSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ipf.atna")
public class IpfAtnaConfigurationProperties {

    @Getter @Setter
    private boolean auditEnabled = true;

    @Getter @Setter
    private AuditSource auditSourceType = AuditSourceType.ApplicationServerProcess;

    @Getter @Setter
    private String auditSourceId;

    @Getter @Setter
    private String auditSendingApplication;

    /**
     * Sets the Audit transport (UDP, TLS)
     */
    @Getter @Setter
    private String auditRepositoryTransport = "UDP";

    /**
     * Sets the host of the audit repository. Deprecated, set ipf.atna.audit-repository-host
     */
    @Getter @Setter
    private String auditRepositoryHost = "localhost";

    /**
     * Sets the port of the audit repository. Deprecated, set ipf.atna.audit-repository-port
     */
    @Getter @Setter
    private int auditRepositoryPort = 514;

    /**
     * Enterprise Site Id
     */
    @Getter @Setter
    private String auditEnterpriseSiteId;

    /**
     * Sets the Audit Message Queue class to be used for sending ATNA records
     */
    @Getter @Setter
    private Class<? extends AuditMessageQueue> auditQueueClass = SynchronousAuditMessageQueue.class;

    /**
     * Sets the Audit Message Postprocessor class
     */
    @Getter @Setter
    private Class<? extends AuditMessagePostProcessor> auditMessagePostProcessorClass;

    /**
     * Sets the Audit Sender class to be used for sending ATNA records
     */
    @Getter @Setter
    private Class<? extends AuditTransmissionProtocol> auditSenderClass;

    /**
     * Sets the exception handler in case auditing fails
     */
    @Getter @Setter
    private Class<? extends AuditExceptionHandler> auditExceptionHandlerClass = LoggingAuditExceptionHandler.class;


    @Getter @Setter
    private boolean includeParticipantsFromResponse;

    @Getter @Setter
    private String auditValueIfMissing = "UNKNOWN";

    /**
     * Class of the optional audit dataset enricher for Web Service based transactions.
     */
    @Getter @Setter
    private Class<? extends WsAuditDatasetEnricher> wsAuditDatasetEnricherClass;

    /**
     * Class of the optional audit dataset enricher for FHIR based transactions.
     */
    @Getter @Setter
    private Class<? extends FhirAuditDatasetEnricher> fhirAuditDatasetEnricherClass;

    @Getter @Setter
    private Balp balp;

    public static class Balp {

        /**
         * Sets the context-path of the BALP audit record repository.
         */
        @Getter
        @Setter
        private String auditRepositoryContextPath = "";

        @Getter
        @Setter
        private String auditEventSerializationType = "json";

        @Getter
        @Setter
        private OAuth oauth;

        public static class OAuth {

            @Getter
            @Setter
            private String[] idPath;

            @Getter
            @Setter
            private String[] issuerPath;

            @Getter
            @Setter
            private String[] clientIdPath;

            @Getter
            @Setter
            private String[] subjectPath;

            @Getter
            @Setter
            private String[] subjectNamePath;

            @Getter
            @Setter
            private String[] subjectOrganizationPath;

            @Getter
            @Setter
            private String[] subjectOrganizationIdPath;

            @Getter
            @Setter
            private String[] subjectRolePath;

            @Getter
            @Setter
            private String[] purposeOfUsePath;

            @Getter
            @Setter
            private String[] homeCommunityIdPath;

            @Getter
            @Setter
            private String[] nationalProviderIdPath;

            @Getter
            @Setter
            private String[] personIdPath;

            @Getter
            @Setter
            private String[] patientIdPath;

            @Getter
            @Setter
            private String[] docIdPath;

            @Getter
            @Setter
            private String[] acpPath;
        }
    }
}
