/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;


/**
 * Basis for Strategy pattern implementation for ATNA Auditing in XDS transactions.
 * @author Dmytro Rud
 */
public abstract class XdsAuditStrategy<T extends XdsAuditDataset> extends AuditStrategySupport<T> {

    /**
     * Constructs an XDS audit strategy.
     *   
     * @param serverSide
     *      whether this is a server-side or a client-side strategy.
     */
    public XdsAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    /**
     * A helper method that analyzes the given registry response and 
     * determines the corresponding RFC 3881 event outcome code.
     * @param response registry to analyze.
     * @return outcome code.
     */
    private static EventOutcomeIndicator getEventOutcomeCodeFromRegistryResponse(EbXMLRegistryResponse<RegistryResponseType> response) {
        try {
            if (response.getStatus() == Status.SUCCESS) {
                return EventOutcomeIndicator.Success;
            }
            if (response.getErrors().isEmpty()) {
                return EventOutcomeIndicator.SeriousFailure;
            }
            // determine the highest error severity
            for (var error : response.getErrors()) {
                if (error.getSeverity() == Severity.ERROR) {
                    return EventOutcomeIndicator.SeriousFailure;
                }
            }
            return EventOutcomeIndicator.MinorFailure;
        } catch (Exception e) {
            return EventOutcomeIndicator.SeriousFailure;
        }
    }

    private static String getEventOutcomeDescriptionFromRegistryResponse(EbXMLRegistryResponse<RegistryResponseType> response) {
        if (response.getErrors().isEmpty()) {
            return null;
        }
        for (var error : response.getErrors()) {
            if (error.getSeverity() == Severity.ERROR) {
                return error.getCodeContext();
            }
        }
        return response.getErrors().get(0).getCodeContext();
    }


    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(T auditDataset, Object pojo) {
        var response = (RegistryResponseType) pojo;
        EbXMLRegistryResponse<RegistryResponseType> ebXML = new EbXMLRegistryResponse30(response);
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }

    @Override
    public String getEventOutcomeDescription(T auditDataset, Object pojo) {
        var response = (RegistryResponseType) pojo;
        EbXMLRegistryResponse<RegistryResponseType> ebXML = new EbXMLRegistryResponse30(response);
        return getEventOutcomeDescriptionFromRegistryResponse(ebXML);
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(T auditDataset, Object response, AuditContext auditContext) {
        var outcomeCodes = getEventOutcomeIndicator(auditDataset, response);
        auditDataset.setEventOutcomeIndicator(outcomeCodes);
        auditDataset.setEventOutcomeDescription(getEventOutcomeDescription(auditDataset, response));
        return outcomeCodes == EventOutcomeIndicator.Success;
    }

}
