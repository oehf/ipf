/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import groovy.util.slurpersupport.GPathResult

/**
 * @author Dmytro Rud
 */
abstract class Hl7v3AuditStrategy extends WsAuditStrategy<Hl7v3AuditDataset> {
    protected static final transient Log LOG = LogFactory.getLog(Hl7v3AuditStrategy.class)

    Hl7v3AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit)
    }


    @Override
    public Hl7v3AuditDataset createAuditDataset() {
        return new Hl7v3AuditDataset(serverSide)
    }


    /**
     * Returns ATNA response code on the basis of Acknowledgement.typeCode
     * of the HL7 v3 output message:
     * <ul>
     *   <li> when the output message cannot be parsed -- "major failure",
     *   <li> when the typeCode is missing -- "major failure",
     *   <li> when the typeCode is 'AA' or 'CA' -- "success",
     *   <li> in all other cases ('AE' and 'QE') -- "serious failure".
     * </ul>
     *
     * @param gpath
     *      response message as {@link GPathResult}.
     */
    @Override
    RFC3881EventOutcomeCodes getEventOutcomeCode(Object gpath) {
        try {
            String code = gpath.acknowledgement[0].typeCode.@code.text()
            if (! code) {
                // code not found -- bad XML
                return RFC3881EventOutcomeCodes.MAJOR_FAILURE
            }
            return ((code == 'AA') || (code == 'CA')) ?
                    RFC3881EventOutcomeCodes.SUCCESS :
                    RFC3881EventOutcomeCodes.SERIOUS_FAILURE

        } catch (Exception e) {
            LOG.error('Exception in audit strategy', e)
            return RFC3881EventOutcomeCodes.MAJOR_FAILURE
        }
    }


    @Override
    void enrichDatasetFromResponse(Object response, Hl7v3AuditDataset auditDataset) {
        auditDataset.eventOutcomeCode = getEventOutcomeCode(slurp(response))
    }


    static void addPatientIds(GPathResult source, Set<String> target) {
        for (node in source) {
            target << Hl7v3Utils.iiToCx(node)
        }
    }


    static GPathResult slurp(Object message) {
        return (message instanceof GPathResult) ? message : Hl7v3Utils.slurp((String) message)
    }
}
