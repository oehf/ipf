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
package org.openehealth.ipf.commons.ihe.ws.cxf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.message.Message;

/**
 * CXF interceptor which logs all errors instead of 
 * letting them break the processing flow.
 * @author Dmytro Rud
 */
abstract public class AbstractSafeInterceptor extends AbstractSoapInterceptor {
    private static final transient Log LOG = LogFactory.getLog(AbstractSafeInterceptor.class);

    /**
     * Constructs the interceptor.
     * @param phase
     *          the phase in which the interceptor is run.
     */
    protected AbstractSafeInterceptor(String phase) {
        super(phase);
    }
    
    /**
     * Performs the actual work, being called from {@link #handleMessage(Message)}.   
     * 
     * @param message
     *          CXF message to process.
     * @throws Exception
     *          any exception that occurred when processing the message.
     */
    abstract protected void process(SoapMessage message) throws Exception;
    
    /**
     * Calls {@link #process(org.apache.cxf.binding.soap.SoapMessage)}
     * and "forwards" all exceptions to the error log.
     * 
     * @param message CXF message to process.
     */
    @Override
    public final void handleMessage(SoapMessage message) {
        try {
            process(message);
        } catch(Exception e) {
            LOG.error(e);
        }
    }

}
