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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.AttachmentInInterceptor;
import org.apache.cxf.interceptor.StaxInInterceptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * CXF server-side interceptor that saves SOAP message body (XML payload)
 * into audit dataset. 
 * <p>
 * Usable on server side only.
 * 
 * @author Dmytro Rud
 */
public class ServerPayloadExtractorInterceptor extends AuditInterceptor {

    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     */
    public ServerPayloadExtractorInterceptor(AuditStrategy auditStrategy) {
        super(Phase.PRE_STREAM, auditStrategy);
        addAfter(AttachmentInInterceptor.class.getName());
        addBefore(StaxInInterceptor.class.getName());
    }

    
    @Override
    public void handleMessage(Message message) throws Fault {
        // check whether we should process
        if( ! getAuditStrategy().needSavePayload()) {
            return;
        }
        AuditDataset auditDataset = getAuditDataset(message);
        
        // extract XML body bytes and save them into the AuditDataset
        try {
            InputStream stream = message.getContent(InputStream.class);
            byte[] streamBytes = IOUtils.readBytesFromStream(stream);
            String payload = new String(streamBytes);
            auditDataset.setPayload(payload);

            // restore the stream
            message.setContent(InputStream.class, new ByteArrayInputStream(streamBytes));
            
        } catch(IOException ioe) {
            // nop
        }
    }
}

