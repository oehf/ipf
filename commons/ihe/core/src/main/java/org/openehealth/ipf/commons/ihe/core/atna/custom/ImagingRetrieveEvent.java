/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventActionCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.events.ihe.GenericIHEAuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * ATNA Audit event for RAD-69 and RAD-75.
 * @author Dmytro Rud
 */
public class ImagingRetrieveEvent extends GenericIHEAuditEventMessage {

    public ImagingRetrieveEvent(
            boolean systemIsSource,
            RFC3881EventCodes.RFC3881EventOutcomeCodes outcome,
            IHETransactionEventTypeCodes eventType)
    {
        super(systemIsSource,
              outcome,
              systemIsSource ? RFC3881EventCodes.RFC3881EventActionCodes.CREATE : RFC3881EventCodes.RFC3881EventActionCodes.READ,
              systemIsSource ? new DICOMEventIdCodes.Import() : new DICOMEventIdCodes.Export(),
              eventType);
    }


    protected void addTypeValuePair(List<TypeValuePairType> pairs, String type, String value) {
        if (! EventUtils.isEmptyOrNull(value)) {
            pairs.add(getTypeValuePair(type, value.getBytes()));
        }
    }


    public void addDocumentParticipantObject(
            String studyInstanceUniqueId,
            String seriesInstanceUniqueId,
            String documentUniqueId,
            String repositoryUniqueId,
            String homeCommunityId)
    {
        List<TypeValuePairType> pairs = new LinkedList<TypeValuePairType>();
        addTypeValuePair(pairs, "Study Instance Unique Id",  studyInstanceUniqueId);
        addTypeValuePair(pairs, "Series Instance Unique Id", seriesInstanceUniqueId);
        addTypeValuePair(pairs, "Repository Unique Id",      repositoryUniqueId);
        addTypeValuePair(pairs, "ihe:homeCommunityID",       homeCommunityId);

        addParticipantObjectIdentification(
                new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.ReportNumber(),
                null,
                null,
                pairs,
                documentUniqueId,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.SYSTEM,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.REPORT,
                null,
                null);
    }

}
