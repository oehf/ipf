/*
 * Copyright 2010 the original author or authors.
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

import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes;
import org.openhealthtools.ihe.atna.auditor.events.ihe.QueryEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Special query event element type for ITI-56 (XCPD patient location query).
 * @author Dmytro Rud
 */
public class Iti56QueryEvent extends QueryEvent {

    public Iti56QueryEvent(
            boolean systemIsSource,
            RFC3881EventOutcomeCodes outcome,
            IHETransactionEventTypeCodes eventType,
            List<CodedValueType> purposesOfUse)
    {
        super(systemIsSource, outcome, eventType, purposesOfUse);
    }

    
    protected void addQueryParametersObject(String payload) {
        addParticipantObjectIdentification(
                this.eventType,
                null,
                EventUtils.encodeBase64(payload.getBytes(Charset.defaultCharset())),
                null,
                "PatientLocationQueryRequest",
                RFC3881ParticipantObjectTypeCodes.SYSTEM,
                RFC3881ParticipantObjectTypeRoleCodes.QUERY,
                null,
                null);
    }
    
}
