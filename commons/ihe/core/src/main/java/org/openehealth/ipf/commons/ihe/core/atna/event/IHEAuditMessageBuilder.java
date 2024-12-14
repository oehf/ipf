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

package org.openehealth.ipf.commons.ihe.core.atna.event;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.event.BaseAuditMessageBuilder;
import org.openehealth.ipf.commons.audit.event.DelegatingAuditMessageBuilder;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.audit.utils.AuditUtils.getHostFromUrl;
import static org.openehealth.ipf.commons.audit.utils.AuditUtils.getProcessId;

/**
 * Base class for building DICOM audit messages as specified in the various IHE transactions.
 * It provides some methods for setting audit event participants that are common across
 * different IHE transactions, namely local participant, remote participant, and human requestor
 *
 * @author Christian Ohr
 * @since 3.5
 */
public abstract class IHEAuditMessageBuilder<T extends IHEAuditMessageBuilder<T, D>, D extends BaseAuditMessageBuilder<D>>
        extends DelegatingAuditMessageBuilder<T, D> {

    public static final String IHE_HOME_COMMUNITY_ID = "ihe:homeCommunityID";
    public static final String URN_IHE_ITI_XCA_2010_HOME_COMMUNITY_ID = "urn:ihe:iti:xca:2010:homeCommunityId";
    public static final String QUERY_ENCODING = "QueryEncoding";
    public static final String REPOSITORY_UNIQUE_ID = "Repository Unique Id";
    public static final String STUDY_INSTANCE_UNIQUE_ID = "Study Instance Unique Id";
    public static final String SERIES_INSTANCE_UNIQUE_ID = "Series Instance Unique Id";
    public static final String DOCUMENT_UNIQUE_ID = "ihe:DocumentUniqueId";

    private final AuditContext auditContext;

    public IHEAuditMessageBuilder(AuditContext auditContext, AuditDataset auditDataset, D delegate) {
        super(delegate);
        this.auditContext = requireNonNull(auditContext, "auditContext must be not null");
        delegate.setAuditSource(auditContext);
        if (auditDataset.getW3cTraceContextId() != null) {
            addSwissW3CTraceContextIdParticipantObject(auditDataset.getW3cTraceContextId());
        }
    }

    public AuditContext getAuditContext() {
        return auditContext;
    }

    /**
     * Set the local participant, which is either the transaction destination (if it's
     * server-side) or the transaction source (if it's client-side)
     *
     * @param auditDataset audit data set
     * @return this
     */
    protected final T setLocalParticipant(AuditDataset auditDataset) {
        if (auditDataset.isServerSide())
            delegate.addDestinationActiveParticipant(
                    auditDataset.getDestinationUserId() != null ?
                            auditDataset.getDestinationUserId() :
                            auditContext.getAuditValueIfMissing(),
                    getProcessId(),
                    null,
                    auditDataset.getLocalAddress(),
                    auditDataset.isDestinationUserIsRequestor());
        else
            delegate.addSourceActiveParticipant(
                    auditDataset.getSourceUserId() != null ?
                            auditDataset.getSourceUserId() :
                            auditContext.getAuditValueIfMissing(),
                    getProcessId(),
                    auditDataset.getSourceUserName(),
                    auditDataset.getLocalAddress(),
                    auditDataset.isSourceUserIsRequestor());
        return self();
    }

    /**
     * Set the remote participant, which is either the transaction source (if it's
     * server-side) or the transaction destination (if it's client-side)
     *
     * @param auditDataset audit data set
     * @return this
     */
    protected final T setRemoteParticipant(AuditDataset auditDataset) {
        if (auditDataset.isServerSide())
            delegate.addSourceActiveParticipant(
                    auditDataset.getSourceUserId() != null ?
                            auditDataset.getSourceUserId() :
                            auditContext.getAuditValueIfMissing(),
                    null,
                    auditDataset.getSourceUserName(),
                    getHostFromUrl(auditDataset.getRemoteAddress()),
                    auditDataset.isSourceUserIsRequestor());
        else
            delegate.addDestinationActiveParticipant(
                    auditDataset.getDestinationUserId() != null ?
                            auditDataset.getDestinationUserId() :
                            auditContext.getAuditValueIfMissing(),
                    null,
                    null,
                    getHostFromUrl(auditDataset.getRemoteAddress()),
                    auditDataset.isDestinationUserIsRequestor());
        return self();
    }

    protected final T addHumanRequestor(AuditDataset auditDataset) {
        var isRequestor = true;
        for (var humanUser : auditDataset.getHumanUsers()) {
            if (!humanUser.isEmpty()) {
                delegate.addActiveParticipant(
                        humanUser.getId() != null ?
                                humanUser.getId() :
                                auditContext.getAuditValueIfMissing(),
                        null,
                        humanUser.getName(),
                        isRequestor,
                        humanUser.getRoles(),
                        null);
                isRequestor = false;
            }
        }
        return self();
    }


    public List<TypeValuePairType> documentDetails(String repositoryId,
                                                   String homeCommunityId,
                                                   String seriesInstanceId,
                                                   String studyInstanceId,
                                                   boolean isXcaHomeCommunityId) {
        return details(STUDY_INSTANCE_UNIQUE_ID, studyInstanceId, seriesInstanceId, repositoryId, homeCommunityId, isXcaHomeCommunityId);
    }


    public List<TypeValuePairType> dicomDetails(String repositoryId,
            String homeCommunityId,
            String documentInstanceId,
            String seriesInstanceId,
            boolean isXcaHomeCommunityId) {
        return details(DOCUMENT_UNIQUE_ID, documentInstanceId, seriesInstanceId, repositoryId, homeCommunityId, isXcaHomeCommunityId);
    }

    private List<TypeValuePairType> details(String instanceKey, String instanceId, String seriesInstanceId, String repositoryId, String homeCommunityId, boolean xcaHomeCommunityId) {
        var tvp = new ArrayList<TypeValuePairType>(0);
        if (instanceId != null) {
            tvp.add(getTypeValuePair(instanceKey, instanceId));
        }
        if (seriesInstanceId != null) {
            tvp.add(getTypeValuePair(SERIES_INSTANCE_UNIQUE_ID, seriesInstanceId));
        }
        if (repositoryId != null) {
            tvp.add(getTypeValuePair(REPOSITORY_UNIQUE_ID, repositoryId));
        }
        if (homeCommunityId != null) {
            var type = xcaHomeCommunityId ? URN_IHE_ITI_XCA_2010_HOME_COMMUNITY_ID : IHE_HOME_COMMUNITY_ID;
            tvp.add(getTypeValuePair(type, homeCommunityId));
        }
        return tvp;
    }

    /**
     * Adds a Participant Object representing a Security Resource involved in the event
     *
     * @param participantObjectIdType transaction-specific participant object type code
     * @param securityResourceId      security resource ID
     * @return this
     */
    public T addSecurityResourceParticipantObject(ParticipantObjectIdType participantObjectIdType, String securityResourceId) {
        delegate.addParticipantObjectIdentification(
                requireNonNull(participantObjectIdType, "security resource ID type must not be null"),
                null,
                null,
                null,
                requireNonNull(securityResourceId, "security resource ID must not be null"),
                ParticipantObjectTypeCode.System,
                ParticipantObjectTypeCodeRole.SecurityResource,
                null,
                null);
        return self();
    }

    /**
     * Adds a list Participant Objects representing Security Resources involved in the event
     *
     * @param participantObjectIdType transaction-specific participant object type code
     * @param securityResourceIds     list security resource IDs
     * @return this
     */
    public T addSecurityResourceParticipantObjects(ParticipantObjectIdType participantObjectIdType, List<String> securityResourceIds) {
        if (securityResourceIds != null) {
            securityResourceIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(sri -> addSecurityResourceParticipantObject(participantObjectIdType, sri));
        }
        return self();
    }

    /**
     * Adds a Participant Object representing a W3C Trace Context ID (specific for the Swiss EPR).
     */
    public T addSwissW3CTraceContextIdParticipantObject(String traceContextId) {
        delegate.addParticipantObjectIdentification(
            ParticipantObjectIdType.of("traceparent", "e-health-suisse", "traceparent"),
            null,
            null,
            null,
            requireNonNull(traceContextId, "trace context ID must not be null"),
            ParticipantObjectTypeCode.Other,
            ParticipantObjectTypeCodeRole.ProcessingElement,
            null,
            null);
        return self();
    }

}
