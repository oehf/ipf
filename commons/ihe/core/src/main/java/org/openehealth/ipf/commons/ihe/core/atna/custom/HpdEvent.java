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

import java.util.List;

/**
 * ATNA Audit event for ITI-59.
 * @author Dmytro Rud
 */
public class HpdEvent extends GenericIHEAuditEventMessage {

    public HpdEvent(
            boolean systemIsSource,
            RFC3881EventCodes.RFC3881EventActionCodes actionCode,
            RFC3881EventCodes.RFC3881EventOutcomeCodes outcome,
            List<CodedValueType> purposesOfUse)
    {
        super(systemIsSource,
              outcome,
              systemIsSource ? RFC3881EventCodes.RFC3881EventActionCodes.READ : actionCode,
              systemIsSource ? new DICOMEventIdCodes.Export() : new DICOMEventIdCodes.Import(),
              new CustomIHETransactionEventTypeCodes.ProviderInformationFeed(),
              purposesOfUse);
    }

    public void addProviderParticipantObject(String providerId) {
        addParticipantObjectIdentification(
                CustomParticipantObjectIDTypeCodes.ISO21091_IDENTIFIER,
                null,
                null,
                null,
                providerId,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.ORGANIZATION,
                RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PROVIDER,
                null,
                null);
    }

}
