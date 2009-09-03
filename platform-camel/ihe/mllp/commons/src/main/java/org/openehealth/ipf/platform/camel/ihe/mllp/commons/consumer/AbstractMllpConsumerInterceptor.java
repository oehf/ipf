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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons.consumer;

import org.apache.camel.Processor;
import org.apache.commons.lang.Validate;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;


/**
 * Abstract consumer-side Camel interceptor.
 * @author Dmytro Rud
 */
public abstract class AbstractMllpConsumerInterceptor implements Processor {

    /**
     * Name of the Camel message header where a copy of the original request  
     * message (as a {@link MessageAdapter} instance) will be saved.  
     */
    public static final String ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME = "pixPdqOriginalMessageAdapter";

    /**
     * Name of the Camel message header where a copy of the original request
     * message (as a {@link String} instance) will be saved.  
     */
    public static final String ORIGINAL_MESSAGE_STRING_HEADER_NAME  = "pixPdqOriginalMessageString";
    
    
    private final MllpEndpoint endpoint;
    private final Processor wrappedProcessor;

    
    /**
     * Constructor.
     * @param endpoint
     *      The Camel endpoint to which this interceptor belongs.
     * @param wrappedProcessor
     *      Original camel-mina processor.
     */
    public AbstractMllpConsumerInterceptor(MllpEndpoint endpoint, Processor wrappedProcessor) {
        Validate.notNull(wrappedProcessor);
        Validate.notNull(endpoint);

        this.endpoint = endpoint;
        this.wrappedProcessor = wrappedProcessor;
    }

    
    // ----- getters -----

    public MllpEndpoint getMllpEndpoint() {
        return endpoint;
    }

    public Processor getWrappedProcessor() {
        return wrappedProcessor;
    }
    
}
