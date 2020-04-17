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
package org.openehealth.ipf.commons.ihe.hl7v3.iti56

import groovy.xml.slurpersupport.GPathResult
import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditStrategy
import org.openehealth.ipf.commons.ihe.hl7v3.audit.codes.Hl7v3EventTypeCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.openehealth.ipf.commons.ihe.hl7v3.audit.codes.Hl7v3ParticipantObjectIdTypeCode.PatientLocationQuery
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.iiToCx

/**
 * Generic audit strategy for ITI-56 (XCPD).
 *
 * @author Dmytro Rud
 */
class Iti56AuditStrategy extends Hl7v3AuditStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(Iti56AuditStrategy)

    Iti56AuditStrategy(boolean serverSide) {
        super(serverSide)
    }

    /**
     * Returns ATNA response code -- SUCCESS if the name of the root element
     * is "PatientLocationQueryResponse", SERIOUS_FAILURE otherwise,
     * MAJOR_FAILURE on exception.
     *
     * @param gpath
     *      response message as {@link GPathResult}.
     */
    @Override
    EventOutcomeIndicator getEventOutcomeIndicator(Object gpath) {
        try {
            return ((gpath.name() == 'PatientLocationQueryResponse') &&
                    (gpath.namespaceURI() == 'urn:ihe:iti:xcpd:2009')) ?
                    EventOutcomeIndicator.Success : EventOutcomeIndicator.SeriousFailure
        } catch (Exception e) {
            LOG.error('Exception in ITI-56 audit strategy', e)
            return EventOutcomeIndicator.MajorFailure
        }
    }

    @Override
    Hl7v3AuditDataset enrichAuditDatasetFromRequest(Hl7v3AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        GPathResult patientId = slurp(request).RequestedPatientId
        auditDataset.setPatientIds([iiToCx(patientId)] as String[])
        auditDataset
    }

    @Override
    AuditMessage[] makeAuditMessage(AuditContext auditContext, Hl7v3AuditDataset auditDataset) {
        new QueryInformationBuilder<>(auditContext, auditDataset, Hl7v3EventTypeCode.PatientLocationQuery)
                .addPatients(auditDataset.patientIds)
                .setQueryParameters("PatientLocationQueryRequest", PatientLocationQuery, auditDataset.requestPayload)
                .getMessages()
    }

}
