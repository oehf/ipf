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
package org.openehealth.ipf.commons.ihe.hl7v3.iti56;

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.iiToCx
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset

/**
 * Generic audit strategy for ITI-56 (XCPD).
 * @author Dmytro Rud
 */
class Iti56AuditStrategy extends Hl7v3AuditStrategy {

    private static final String[] NECESSARY_FIELD_NAMES = [
            'EventOutcomeCode',
            'UserId',
            'ServiceEndpointUrl',
            'RequestPayload',
            'PatientId',
    ]


    Iti56AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit)
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
    RFC3881EventOutcomeCodes getEventOutcomeCode(Object gpath) {
        try {
            return ((gpath.name() == 'PatientLocationQueryResponse') &&
                    (gpath.namespaceURI() == 'urn:ihe:iti:xcpd:2009')) ?
                            RFC3881EventOutcomeCodes.SUCCESS : RFC3881EventOutcomeCodes.SERIOUS_FAILURE
        } catch (Exception e) {
            LOG.error('Exception in ITI-56 audit strategy', e)
            return RFC3881EventOutcomeCodes.MAJOR_FAILURE
        }
    }


    @Override
    void enrichDatasetFromRequest(Object request, Hl7v3AuditDataset auditDataset) {
        GPathResult patientId = slurp(request).RequestedPatientId
        auditDataset.patientIds = [iiToCx(patientId)]
    }


    @Override
    void doAudit(Hl7v3AuditDataset auditDataset) {
        AuditorManager.hl7v3Auditor.auditIti56(
                serverSide,
                auditDataset.eventOutcomeCode,
                auditDataset.userId,
                auditDataset.userName,
                auditDataset.serviceEndpointUrl,
                auditDataset.clientIpAddress,
                auditDataset.requestPayload,
                auditDataset.patientId
        )
    }


    @Override
    String[] getNecessaryAuditFieldNames() {
        return NECESSARY_FIELD_NAMES
    }

}
