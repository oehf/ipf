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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.spi.UnitOfWork;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;

/**
 * Processor that extracts resources from the body of an input message.
 * <p>
 * The message is passed to an attached {@link ResourceHandler} that performs
 * the necessary extraction
 * @author Jens Riemschneider
 */
public class StoreProcessor extends ResourceHandlingProcessor {
    private static final Log log = LogFactory.getLog(StoreProcessor.class);    

    /* (non-Javadoc)
     * @see org.apache.camel.processor.DelegateProcessor#processNext(org.apache.camel.Exchange)
     */
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        performExtraction(exchange);
        
        super.processNext(exchange);
    }
    
    private void performExtraction(Exchange exchange) throws Exception {
        if (exchange.getPattern().isInCapable() && hasResourceHandler()) {
            Message inMessage = exchange.getIn();
            UnitOfWork unitOfWork = exchange.getUnitOfWork();
            String unitOfWorkId = "none";
            if (unitOfWork == null) {
                log.warn("No unit of work defined. StoreProcessor is unable to perform clean up of stored resources");                
            }
            else {
                unitOfWorkId = unitOfWork.getId();
                unitOfWork.addSynchronization(new ResourceCleanUp(unitOfWorkId));
            }
                
        	Collection<ResourceDataSource> resources = new ArrayList<ResourceDataSource>();
            for (ResourceHandler handler : getResourceHandlers()) {
            	resources.addAll(handler.extract(unitOfWorkId, inMessage));
            }
            
            log.debug("extracted resources: " + resources);
        }
    }

    private final class ResourceCleanUp implements Synchronization {
        private String unitOfWorkId;

        public ResourceCleanUp(String unitOfWorkId) {
            this.unitOfWorkId = unitOfWorkId;
        }

        @Override
        public void onComplete(Exchange exchange) {
            cleanUp(exchange);
        }

        @Override
        public void onFailure(Exchange exchange) {
            cleanUp(exchange);
        }

        private void cleanUp(Exchange exchange) {
            Message outMessage = exchange.getOut();
            
            List<ResourceDataSource> requiredResources = new ArrayList<ResourceDataSource>();
            for (ResourceHandler handler : getResourceHandlers()) {
                requiredResources.addAll(handler.getRequiredResources(outMessage));
            }

            for (ResourceHandler handler : getResourceHandlers()) {
                handler.cleanUp(unitOfWorkId, outMessage, requiredResources);
            }
        }
    }
}
