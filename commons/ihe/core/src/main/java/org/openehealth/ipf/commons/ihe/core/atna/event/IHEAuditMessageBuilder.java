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
import org.openehealth.ipf.commons.audit.event.BaseAuditMessageBuilder;
import org.openehealth.ipf.commons.audit.event.DelegatingAuditMessageBuilder;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.ArrayList;
import java.util.List;

import static org.openehealth.ipf.commons.audit.utils.AuditUtils.getHostFromUrl;
import static org.openehealth.ipf.commons.audit.utils.AuditUtils.getProcessId;

/**
 * Base class for building DICOM audit messages as specified in the various IHE transactions.
 * It provides some methods for setting audit event participants that are common across
 * different IHE transactions, namely local participant, remote participant, and human requestor
 *
 * @author Christian Ohr
 */
public abstract class IHEAuditMessageBuilder<T extends IHEAuditMessageBuilder<T, D>, D extends BaseAuditMessageBuilder<D>>
        extends DelegatingAuditMessageBuilder<T, D> {

    public static final String IHE_HOME_COMMUNITY_ID = "ihe:homeCommunityID";
    public static final String URN_IHE_ITI_XCA_2010_HOME_COMMUNITY_ID = "urn:ihe:iti:xca:2010:homeCommunityId";
    public static final String QUERY_ENCODING = "QueryEncoding";
    public static final String REPOSITORY_UNIQUE_ID = "Repository Unique Id";
    public static final String STUDY_INSTANCE_UNIQUE_ID = "Study Instance Unique Id";
    public static final String SERIES_INSTANCE_UNIQUE_ID = "Series Instance Unique Id";

    public IHEAuditMessageBuilder(AuditContext auditContext, D delegate) {
        super(delegate);
        delegate.setAuditSource(auditContext);
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
            delegate.addDestinationActiveParticipant(auditDataset.getDestinationUserId(),
                    getProcessId(),
                    null,
                    auditDataset.getLocalAddress(),
                    auditDataset.isDestinationUserIsRequestor());
        else
            delegate.addSourceActiveParticipant(auditDataset.getSourceUserId(),
                    getProcessId(),
                    null,
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
            delegate.addSourceActiveParticipant(auditDataset.getSourceUserId(),
                    null,
                    null,
                    getHostFromUrl(auditDataset.getRemoteAddress()),
                    auditDataset.isSourceUserIsRequestor());
        else
            delegate.addDestinationActiveParticipant(auditDataset.getDestinationUserId(),
                    null,
                    null,
                    getHostFromUrl(auditDataset.getRemoteAddress()),
                    auditDataset.isDestinationUserIsRequestor());
        return self();
    }

    protected final T addHumanRequestor(AuditDataset auditDataset) {
        if (auditDataset.getUserName() != null && !auditDataset.getUserName().isEmpty()) {
            delegate.addActiveParticipant(auditDataset.getUserName(), null, auditDataset.getUserName(),
                    true, auditDataset.getUserRoles(), null);
        }
        return self();
    }

    protected static List<TypeValuePairType> makeDocumentDetail(String repositoryId, String homeCommunityId, String seriesInstanceId, String studyInstanceId) {
        List<TypeValuePairType> tvp = new ArrayList<>();
        if (studyInstanceId != null) {
            tvp.add(new TypeValuePairType(STUDY_INSTANCE_UNIQUE_ID, studyInstanceId));
        }
        if (seriesInstanceId != null) {
            tvp.add(new TypeValuePairType(SERIES_INSTANCE_UNIQUE_ID, seriesInstanceId));
        }
        if (repositoryId != null) {
            tvp.add(new TypeValuePairType(REPOSITORY_UNIQUE_ID, repositoryId));
        }
        if (homeCommunityId != null) {
            tvp.add(new TypeValuePairType(IHE_HOME_COMMUNITY_ID, homeCommunityId));
        }
        return tvp;
    }
}
