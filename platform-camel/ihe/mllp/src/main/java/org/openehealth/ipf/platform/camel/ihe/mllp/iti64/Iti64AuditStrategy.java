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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti64;

import ca.uhn.hl7v2.model.v25.group.ADT_A43_PATIENT;
import ca.uhn.hl7v2.model.v25.message.ADT_A43;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.PipeParser;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Generic audit strategy for ITI-64 (Notify XAD-PID Link Change).
 * @author Christian Ohr
 * @author Boris Stanojevic
 * @author Dmytro Rud
 */
class Iti64AuditStrategy extends MllpAuditStrategy<Iti64AuditDataset> {

    private static final EncodingCharacters ENCODING_CHARACTERS =
            new EncodingCharacters('|', '^', '~', '\\', '&');

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
            "SourcePatientId",
            "NewPatientId",
            "OldPatientId"};


    public Iti64AuditStrategy(boolean serverSide) {
        super(serverSide);
    }


    @Override
    public String[] getNecessaryFields(String eventTrigger) {
        return NECESSARY_AUDIT_FIELDS;
    }


    @Override
    public void enrichAuditDatasetFromRequest(
            Iti64AuditDataset auditDataset,
            MessageAdapter<?> msg,
            Exchange exchange)
    {
        ADT_A43 message = (ADT_A43) msg.getHapiMessage();
        ADT_A43_PATIENT patient = message.getPATIENT(0);

        auditDataset.setNewPatientId(PipeParser.encode(
                patient.getPID().getPatientIdentifierList(0), ENCODING_CHARACTERS));
        auditDataset.setSourcePatientId(PipeParser.encode(
                patient.getPID().getPatientIdentifierList(1), ENCODING_CHARACTERS));
        auditDataset.setOldPatientId(PipeParser.encode(
                patient.getMRG().getMrg1_PriorPatientIdentifierList(0), ENCODING_CHARACTERS));
    }


    @Override
    public void doAudit(RFC3881EventOutcomeCodes eventOutcome, Iti64AuditDataset auditDataset) {
        AuditorManager.getCustomPixAuditor().auditIti64(
                isServerSide(),
                eventOutcome,
                isServerSide() ? auditDataset.getRemoteAddress() : auditDataset.getLocalAddress(),
                auditDataset.getSendingFacility(),
                auditDataset.getSendingApplication(),
                isServerSide() ? auditDataset.getLocalAddress() : auditDataset.getRemoteAddress(),
                auditDataset.getReceivingFacility(),
                auditDataset.getReceivingApplication(),
                auditDataset.getMessageControlId(),
                auditDataset.getSourcePatientId(),
                auditDataset.getNewPatientId(),
                auditDataset.getOldPatientId()
        );
    }


    @Override
    public Iti64AuditDataset createAuditDataset() {
        return new Iti64AuditDataset(isServerSide());
    }
}
