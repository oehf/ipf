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
package org.openehealth.ipf.commons.ihe.xcpd;

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * ATNA Audit dataset for XCPD.
 * @author Dmytro Rud
 */
public class XcpdAuditDataset extends WsAuditDataset {

    // as opposed to XDS, we determine outcome code as part of audit dataset 
    // enrichment, in order to not parse the payload twice.
    private RFC3881EventOutcomeCodes outcomeCode;
    
    // request payload
    private String requestPayload;
    
    public XcpdAuditDataset(boolean serverSide) {
        super(serverSide);
    }

    public void setOutcomeCode(RFC3881EventOutcomeCodes outcomeCode) {
        this.outcomeCode = outcomeCode;
    }

    public RFC3881EventOutcomeCodes getOutcomeCode() {
        return outcomeCode;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getRequestPayload() {
        return requestPayload;
    }
}
