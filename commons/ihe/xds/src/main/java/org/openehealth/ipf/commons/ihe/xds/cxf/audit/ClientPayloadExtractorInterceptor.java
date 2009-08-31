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
package org.openehealth.ipf.commons.ihe.xds.cxf.audit;

import java.io.OutputStream;

import org.apache.cxf.binding.soap.interceptor.SoapOutInterceptor.SoapOutEndingInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.xds.utils.SoapUtils;


/**
 * CXF client-side interceptor that reads the payload collected by the output  
 * stream proxy installed in {@link ClientOutputStreamSubstituteInterceptor}.
 * <p>
 * Usable on client side only. 
 * 
 * @author Dmytro Rud
 */
public class ClientPayloadExtractorInterceptor extends AuditInterceptor {

    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     */
    public ClientPayloadExtractorInterceptor(ItiAuditStrategy auditStrategy) {
        super(Phase.WRITE_ENDING, auditStrategy);
        addAfter(SoapOutEndingInterceptor.class.getName());
    }
    
    
    @Override
    public void process(Message message) throws Exception {
        // check whether we should process
        if( ! getAuditStrategy().needSavePayload()) {
            return;
        }
        ItiAuditDataset auditDataset = getAuditDataset(message);

        // get the payload and store it in the audit dataset
        WrappedOutputStream wrapper = (WrappedOutputStream)message.getContent(OutputStream.class);
        String soapEnvelope = wrapper.getCollectedPayloadAndDeactivate();
        String payload = SoapUtils.extractSoapBody(soapEnvelope);
        auditDataset.setPayload(payload);
    }
}
