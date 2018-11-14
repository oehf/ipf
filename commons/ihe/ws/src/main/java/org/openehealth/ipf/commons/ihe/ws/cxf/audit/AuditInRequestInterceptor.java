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
import org.apache.cxf.headers.Header;
import org.apache.cxf.wsdl.interceptors.DocLiteralInInterceptor;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder;

/**
 * CXF interceptor for ATNA auditing in WS-based IHE transactions with
 * WSA asynchrony support.  Handles <b>incoming</b> requests
 * on <b>service</b> (consumer) side.
 *
 * @author Dmytro Rud
 */
public class AuditInRequestInterceptor<T extends WsAuditDataset> extends AbstractAuditInterceptor<T> {

    private final WsTransactionConfiguration<T> wsTransactionConfiguration;

    /**
     * Constructor.
     */
    public AuditInRequestInterceptor(AuditStrategy<T> auditStrategy, AuditContext auditContext, WsTransactionConfiguration<T> wsTransactionConfiguration) {
        super(Phase.UNMARSHAL, auditStrategy, auditContext);
        addAfter(DocLiteralInInterceptor.class.getName());
        this.wsTransactionConfiguration = wsTransactionConfiguration;
    }

    
    @Override
    protected void process(SoapMessage message) {
        if (isGET(message)) {
            return;
        }

        T auditDataset = getAuditDataset(message);
        extractAddressesFromServletRequest(message, auditDataset);
        enrichAuditDatasetFromXuaToken(message, Header.Direction.DIRECTION_IN, auditDataset);
        // TODO Also extract basic auth user?
        extractClientCertificateCommonName(message, auditDataset);
        
        if (wsTransactionConfiguration.isAuditRequestPayload()) {
            auditDataset.setRequestPayload(message.getContent(StringPayloadHolder.class));
        }

        getAuditStrategy().enrichAuditDatasetFromRequest(auditDataset, extractPojo(message), message);
    }

}
