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

import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;


/**
 * Basis for Strategy pattern implementation for ATNA Auditing in XDS transactions.
 * @author Dmytro Rud
 */
public abstract class XdsAuditStrategy<T extends XdsAuditDataset> extends AuditStrategySupport<T> {

    public static final String IHE_HOME_COMMUNITY_ID = "ihe:homeCommunityID";
    public static final String URN_IHE_ITI_XCA_2010_HOME_COMMUNITY_ID = "urn:ihe:iti:xca:2010:homeCommunityId";
    public static final String QUERY_ENCODING = "QueryEncoding";
    public static final String REPOSITORY_UNIQUE_ID = "Repository Unique Id";
    public static final String STUDY_INSTANCE_UNIQUE_ID = "Study Instance Unique Id";
    public static final String SERIES_INSTANCE_UNIQUE_ID = "Series Instance Unique Id";

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
    protected static EventOutcomeIndicator getEventOutcomeCodeFromRegistryResponse(EbXMLRegistryResponse response) {
        try {
            if (response.getStatus() == Status.SUCCESS) {
                return EventOutcomeIndicator.Success;
            }
            if (response.getErrors().isEmpty()) {
                return EventOutcomeIndicator.SeriousFailure;
            }
            // determine the highest error severity
            for (EbXMLRegistryError error : response.getErrors()) {
                if (error.getSeverity() == Severity.ERROR) {
                    return EventOutcomeIndicator.SeriousFailure;
                }
            }
            return EventOutcomeIndicator.MinorFailure;
        } catch (Exception e) {
            return EventOutcomeIndicator.SeriousFailure;
        }
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(T auditDataset, Object response) {
        EventOutcomeIndicator outcomeCodes = getEventOutcomeIndicator(response);
        auditDataset.setEventOutcomeIndicator(outcomeCodes);
        return outcomeCodes == EventOutcomeIndicator.Success;
    }

}
