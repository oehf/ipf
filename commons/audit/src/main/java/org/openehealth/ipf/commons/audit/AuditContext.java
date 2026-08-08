/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit;

import io.micrometer.context.ContextRegistry;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler;
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.openehealth.ipf.commons.audit.types.AuditSource;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Stream;

/**
 * AuditContext is the central location where all aspects of serializing and sending out
 * Audit messages are defined. This includes
 * <ul>
 * <li>whether auditing is enabled at all</li>
 * <li>the transmission protocol (UDP, TLS, ...)</li>
 * <li>the queue implementation (synchronous, asynchronous, ...</li>
 * <li>the serialization strategy (e.g. which DICOM audit version shall be used)</li>
 * <li>global parameters like source ID, enterprise ID</li>
 * </ul>
 *
 * @author Christian Ohr
 * @since 3.5
 */
public interface AuditContext {

    /**
     * @return true if auditing is enabled, false otherwise
     */
    boolean isAuditEnabled();

    /**
     * @return hostname of the audit repository
     */
    String getAuditRepositoryHostName();

    /**
     * @return address of the audit repository
     */
    InetAddress getAuditRepositoryAddress();

    /**
     * @return port of the audit repository
     */
    int getAuditRepositoryPort();

    /**
     * @return context path of the audit repository, for transports that address it over HTTP. Empty
     *      unless the audit repository is reached through one.
     */
    default String getAuditRepositoryContextPath() {
        return "";
    }

    /**
     * @return where to find the claims in an access token the audited request carried. Not tied to any
     *      audit format; the defaults point at the standard JWT claims and the IHE IUA/BPPC extensions.
     */
    default JwtExtractorProperties getJwtExtractorProperties() {
        return new JwtExtractorProperties();
    }

    /**
     * Names of the HTTP headers that may carry the id correlating the audit records the two ends of a
     * transaction write about it, most preferred first. The first header the request actually carries
     * wins; a request carrying none is audited without such an id.
     * <p>
     * <a href="https://profiles.ihe.net/ITI/BALP/index.html">IHE BALP</a> asks for
     * {@link RequestIdHeaders#X_REQUEST_ID} and reports it as the transaction entity of an AuditEvent,
     * which is why that is the default. A deployment that already propagates a trace can name its
     * headers here instead -- {@link RequestIdHeaders#TRACEPARENT} for W3C Trace Context,
     * {@link RequestIdHeaders#B3} and {@link RequestIdHeaders#X_B3_TRACE_ID} for the single-header and
     * multi-header flavours of B3 -- and the id is reported in the same place. Naming both B3 headers is
     * the way to accept whichever flavour a caller happens to use; they yield the same id.
     * <p>
     * Any other name works too and is then taken at face value; see {@link RequestIdHeaders} for the
     * headers whose value needs more than that.
     *
     * @return the header names to look for, most preferred first. Empty disables the lookup.
     */
    default List<String> getRequestIdHeaderNames() {
        return List.of(RequestIdHeaders.X_REQUEST_ID);
    }

    /**
     * Participant object ID type to record the correlation id under, whichever of
     * {@link #getRequestIdHeaderNames()} it was found in. Set this only to flatten the distinction: by
     * default the type follows the header, so that the audit record says which propagation format the
     * id came from -- see {@link RequestIdHeaders#participantObjectIdType(String)}.
     * <p>
     * A deployment whose audit repository only understands
     * {@link ParticipantObjectIdTypeCode#XRequestId} would name that one here.
     *
     * @return the participant object ID type to force, or null to let it follow the header
     */
    default ParticipantObjectIdType getRequestIdType() {
        return null;
    }

    /**
     * @return sending application
     */
    String getSendingApplication();

    /**
     * @return the wire protocol to be used
     */
    AuditTransmissionProtocol getAuditTransmissionProtocol();

    /**
     * @return the queue implementation to be used
     */
    AuditMessageQueue getAuditMessageQueue();

    /**
     * @return the SSL socket factory to be used for TLS-based connections
     */
    TlsParameters getTlsParameters();

    /**
     * @return Audit dataset enricher for Web Service based transactions.
     */
    <T extends WsAuditDatasetEnricher> T getWsAuditDatasetEnricher();

    /**
     * @return Audit dataset enricher for FHIR based transactions.
     */
    <T extends FhirAuditDatasetEnricher> T getFhirAuditDatasetEnricher();

    /**
     * @return a post-processor for audit messages (defaults to a NO-OP implementation
     */
    default AuditMessagePostProcessor getAuditMessagePostProcessor() {
        return AuditMessagePostProcessor.noOp();
    }

    /**
     * @return the serialization strategy (defaults to the latest relevant DICOM version)
     */
    default SerializationStrategy getSerializationStrategy() {
        return Current.INSTANCE;
    }

    /**
     * Sends out the (potentially post-processed) audit messages as configured in this audit context
     *
     * @param messages audit messages to be sent
     */
    default void audit(AuditMessage... messages) {
        if (isAuditEnabled() && messages != null) {
            getAuditMessageQueue().audit(this, Stream.of(messages)
                .map(getAuditMessagePostProcessor())
                .toArray(AuditMessage[]::new));
        }
    }

    /**
     * Returns audit metadata e.g. for populating the RFC 5424 header
     * @return audit metadata
     */
    default AuditMetadataProvider getAuditMetadataProvider() {
        return AuditMetadataProvider.getDefault();
    }

    /**
     * Returns a value that is used when an otherwise mandatory attribute for an audit
     * record in missing (e.g. a participant object ID). In this case, we can still write an audit
     * record (e.g. documenting a failed request due to the missing attribute).
     * <p>
     * This can also be set to null at the risk that building the audit record might throw an
     * exception.
     *
     * @return a value that is used when an otherwise mandatory attribute for an audit record in missing
     */
    default String getAuditValueIfMissing() {
        return "UNKNOWN";
    }

    /**
     * @return Source ID attribute of the audit event
     */
    String getAuditSourceId();

    /**
     * @return Enterprise site ID attribute of the audit event
     */
    String getAuditEnterpriseSiteId();

    /**
     * @return type of audit source
     */
    AuditSource getAuditSource();

    /**
     * @return exception handler
     */
    AuditExceptionHandler getAuditExceptionHandler();

    /**
     * Determines whether participant object records shall be added to the audit message
     * that are derived from the response of a request. This specifically applies to
     * query results. The DICOM audit specification states that this should not be the case,
     * however, project and legal requirements sometimes mandate that e.g. patient identifiers
     * being retrieved shall be audited.
     *
     * @return true if participant object records shall be added, otherwise false
     */
    boolean isIncludeParticipantsFromResponse();

    /**
     * Returns a registry of {@link io.micrometer.context.ThreadLocalAccessor} instances that are used to propagate context
     * when asynchronously queuing audit records.
     *
     * @return context registry
     */
    ContextRegistry getContextRegistry();

    static AuditContext noAudit() {
        return DefaultAuditContext.NO_AUDIT;
    }
}
