/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.events.ihe.GenericIHEAuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * ATNA Audit event for ITI-Y1.
 * @author Dmytro Rud
 *
 * @since 3.3
 */
public class RemoveDocumentsEvent extends GenericIHEAuditEventMessage {

    public RemoveDocumentsEvent(
            boolean systemIsSource,
            RFC3881EventCodes.RFC3881EventOutcomeCodes outcome,
            List<CodedValueType> purposesOfUse)
    {
        super(systemIsSource,
              outcome,
              RFC3881EventCodes.RFC3881EventActionCodes.DELETE,
              new DICOMEventIdCodes.PatientRecord(),
              new CustomIHETransactionEventTypeCodes.RemoveDocuments(),
              purposesOfUse);
    }


    protected void addTypeValuePair(List<TypeValuePairType> pairs, String type, String value) {
        if (! EventUtils.isEmptyOrNull(value)) {
            pairs.add(getTypeValuePair(type, value.getBytes(Charset.defaultCharset())));
        }
    }


    public void addRemovedDocumentParticipantObject(String documentUniqueId, String repositoryUniqueId) {
        List<TypeValuePairType> pairs = new LinkedList<>();
        addTypeValuePair(pairs, "urn:ihe:iti:xds:2007:repositoryUniqueId", repositoryUniqueId);

        addParticipantObjectIdentification(
                new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.ReportNumber(),
                null,
                null,
                pairs,
                documentUniqueId,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.SYSTEM,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.REPORT,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectDataLifeCycleCodes.PERMANENT_ERASURE,
                null);
    }

}
