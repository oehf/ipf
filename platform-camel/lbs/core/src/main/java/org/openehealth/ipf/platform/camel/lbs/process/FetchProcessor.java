/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.lbs.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentDataSource;

/**
 * Processor that integrates attachments into the body of an input message.
 * <p>
 * All attachments are passed to an {@link AttachmentHandler}. The result is an 
 * input message that can be used with endpoints compatible with the handler. 
 * @author Jens Riemschneider
 */
public class FetchProcessor extends AttachmentHandlingProcessor {
    private static final Log log = LogFactory.getLog(FetchProcessor.class);    
    
    /* (non-Javadoc)
     * @see org.apache.camel.processor.DelegateProcessor#processNext(org.apache.camel.Exchange)
     */
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        performIntegration(exchange);
        
        super.processNext(exchange);
    }
    
    private void performIntegration(Exchange exchange) throws Exception {
        if (exchange.getPattern().isInCapable() && hasAttachmentHandler()) {
            Message inMessage = exchange.getIn();            
            Collection<AttachmentDataSource> attachments = getAttachments(inMessage);            
            for (AttachmentHandler handler : getAttachmentHandlers()) {
                handler.integrate(inMessage, attachments);
            }
            
            log.debug("integrated attachments: " + attachments);
        }
    }
    
    private Collection<AttachmentDataSource> getAttachments(Message message) throws IOException {
        Map<String, DataHandler> camelAttachments = message.getAttachments();
        Collection<AttachmentDataSource> attachments = new ArrayList<AttachmentDataSource>(camelAttachments.size());
        for (Map.Entry<String, DataHandler> entry : camelAttachments.entrySet()) {
            DataSource dataSource = entry.getValue().getDataSource();
            AttachmentDataSource attachment = new AttachmentDataSource(entry.getKey(), dataSource);
            attachments.add(attachment);
        }
        
        return attachments;
    }
}
