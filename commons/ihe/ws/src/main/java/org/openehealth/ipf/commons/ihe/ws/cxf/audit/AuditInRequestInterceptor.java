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
package org.openehealth.ipf.commons.ihe.ws.cxf.audit;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.DocLiteralInInterceptor;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder;

/**
 * CXF interceptor for ATNA auditing in WS-based IHE transactions with
 * WSA asynchrony support.  Handles <b>incoming</b> requests
 * on <b>service</b> (consumer) side.
 *
 * @author Dmytro Rud
 */
public class AuditInRequestInterceptor extends AbstractAuditInterceptor {
    private final WsTransactionConfiguration wsTransactionConfiguration;

    /**
     * Constructor.
     */
    public AuditInRequestInterceptor(WsAuditStrategy auditStrategy, WsTransactionConfiguration wsTransactionConfiguration) {
        super(Phase.UNMARSHAL, auditStrategy);
        addAfter(DocLiteralInInterceptor.class.getName());
        this.wsTransactionConfiguration = wsTransactionConfiguration;
    }

    
    @Override
    protected void process(SoapMessage message) throws Exception {
        if (isGET(message)) {
            return;
        }

        WsAuditDataset auditDataset = getAuditDataset(message);
        extractAddressesFromServletRequest(message, auditDataset);
        extractXuaUserNameFromSaml2Assertion(message, auditDataset);
        
        if (wsTransactionConfiguration.isAuditRequestPayload()) {
            auditDataset.setRequestPayload(message.getContent(StringPayloadHolder.class));
        }

        getAuditStrategy().enrichDatasetFromRequest(extractPojo(message), auditDataset);
    }

}
