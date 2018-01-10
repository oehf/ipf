/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEQueryBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy.QUERY_ENCODING;
import static org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy.URN_IHE_ITI_XCA_2010_HOME_COMMUNITY_ID;

/**
 * @author Christian Ohr
 */
public class XdsQueryBuilder extends IHEQueryBuilder<XdsQueryBuilder> {

    public XdsQueryBuilder(AuditContext auditContext, XdsQueryAuditDataset auditDataset, EventType eventType, List<PurposeOfUse> purposesOfUse) {
        super(auditContext, auditDataset, eventType, purposesOfUse);
    }

    public XdsQueryBuilder setQueryParameters(XdsQueryAuditDataset auditDataset, XdsParticipantObjectIdTypeCode participantObjectIdTypeCode) {
        // Add QueryEncoding and HomeCommunityId as details, if applicable
        List<TypeValuePairType> tvp = new LinkedList<>();
        if (auditDataset.getQueryUuid() != null && !auditDataset.getQueryUuid().isEmpty()) {
            tvp.add(new TypeValuePairType(QUERY_ENCODING, Charset.defaultCharset().toString()));
        }
        if (auditDataset.getHomeCommunityId() != null) {
            tvp.add(new TypeValuePairType(
                    URN_IHE_ITI_XCA_2010_HOME_COMMUNITY_ID,
                    auditDataset.getHomeCommunityId()));
        }
        return setQueryParameters(
                auditDataset.getQueryUuid(),
                participantObjectIdTypeCode,
                auditDataset.getRequestPayload(),
                tvp);
    }
}
