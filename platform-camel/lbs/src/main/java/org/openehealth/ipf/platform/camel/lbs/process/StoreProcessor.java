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

import java.util.Collection;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.spi.UnitOfWork;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentDataSource;

/**
 * Processor that extracts attachments from the body of an input message.
 * <p>
 * The message is passed to an attached {@link AttachmentHandler} that returns
 * the attachments which are added as Camel attachments to the input message.
 * @author Jens Riemschneider
 */
public class StoreProcessor extends AttachmentHandlingProcessor {
    private static final Log log = LogFactory.getLog(StoreProcessor.class);    

    /** Name of the exchange property that is used to store the list of created attachments */
    static final String CREATED_ATTACHMENTS = 
        "org.openehealth.ipf.platform.camel.lbs.process.StoreProcessor.CreatedAttachments";
    
    /* (non-Javadoc)
     * @see org.apache.camel.processor.DelegateProcessor#processNext(org.apache.camel.Exchange)
     */
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        performExtraction(exchange);
        
        super.processNext(exchange);
    }
    
    private void performExtraction(Exchange exchange) throws Exception {
        if (exchange.getPattern().isInCapable() && hasAttachmentHandler()) {
            Message inMessage = exchange.getIn();
            UnitOfWork unitOfWork = exchange.getUnitOfWork();
            String unitOfWorkId = "none";
            if (unitOfWork == null) {
                log.warn("No unit of work defined. StoreProcessor is unable to perform clean up of stored attachments");                
            }
            else {
                unitOfWorkId = unitOfWork.getId();
                unitOfWork.addSynchronization(new AttachmentCleanUp(unitOfWorkId));
            }
                            
            Collection<AttachmentDataSource> attachments = 
                getAttachmentHandler().extract(unitOfWorkId, inMessage);
            
            for (AttachmentDataSource attachment : attachments) {
                inMessage.addAttachment(attachment.getId(), new DataHandler(attachment));
            }
            
            log.info("extracted attachments: " + attachments);
        }
    }

    private final class AttachmentCleanUp implements Synchronization {
        private String unitOfWorkId;

        public AttachmentCleanUp(String unitOfWorkId) {
            this.unitOfWorkId = unitOfWorkId;
        }

        @Override
        public void onComplete(Exchange exchange) {
            getAttachmentHandler().cleanUp(unitOfWorkId, exchange.getOut());
        }

        @Override
        public void onFailure(Exchange exchange) {
            getAttachmentHandler().cleanUp(unitOfWorkId, exchange.getOut());
        }
    }
}
