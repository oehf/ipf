/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2.audit.iti64;

import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.PipeParser;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.xpid.v25.message.ADT_A43;

import java.util.Map;

/**
 * Generic audit strategy for ITI-64 (Notify XAD-PID Link Change).
 *
 * @author Christian Ohr
 * @author Boris Stanojevic
 * @author Dmytro Rud
 */
public class Iti64AuditStrategy extends AuditStrategySupport<Iti64AuditDataset> {

    private static final EncodingCharacters ENCODING_CHARACTERS =
            new EncodingCharacters('|', null);


    public Iti64AuditStrategy(boolean serverSide) {
        super(serverSide);
    }


    @Override
    public Iti64AuditDataset enrichAuditDatasetFromRequest(Iti64AuditDataset auditDataset, Object msg, Map<String, Object> parameters) {
        var message = (ADT_A43) msg;
        var patient = message.getPATIENT(0);

        var pidPatientIdList = patient.getPID().getPatientIdentifierList();
        if (pidPatientIdList.length > 0) {
            auditDataset.setNewPatientId(PipeParser.encode(pidPatientIdList[0], ENCODING_CHARACTERS));
            if (pidPatientIdList.length > 1) {
                auditDataset.setLocalPatientId(PipeParser.encode(pidPatientIdList[1], ENCODING_CHARACTERS));
            }
        }

        var mrgPatientIdList = patient.getMRG().getMrg1_PriorPatientIdentifierList();
        if (mrgPatientIdList.length > 0) {
            auditDataset.setPreviousPatientId(PipeParser.encode(mrgPatientIdList[0], ENCODING_CHARACTERS));
            if (mrgPatientIdList.length > 1) {
                auditDataset.setSubsumedLocalPatientId(PipeParser.encode(mrgPatientIdList[1], ENCODING_CHARACTERS));
            }
        }

        return auditDataset;
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, Iti64AuditDataset auditDataset) {
        var builder = new IHEPatientRecordChangeLinkBuilder<>(auditContext, auditDataset)
                .setLocalPatientId(auditDataset);
        if (auditDataset.getSubsumedLocalPatientId() != null) {
            builder.setSubsumedLocalPatientId(auditDataset);
        }
        return builder
                .setNewPatientId(auditDataset)
                .setPreviousPatientId(auditDataset)
                .setSubmissionSet(auditDataset)
                .getMessages();
    }


    @Override
    public Iti64AuditDataset createAuditDataset() {
        return new Iti64AuditDataset(isServerSide());
    }
}
