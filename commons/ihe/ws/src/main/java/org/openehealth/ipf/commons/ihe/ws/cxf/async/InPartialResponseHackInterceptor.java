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
package org.openehealth.ipf.commons.ihe.ws.cxf.async;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * CXF interceptor which removes the partial response flag from
 * incoming messages in order to let the CXF client unlock
 * the producer which is waiting for the response.
 * 
 * @author Dmytro Rud
 */
public class InPartialResponseHackInterceptor extends AbstractPhaseInterceptor<Message> {
    
    public InPartialResponseHackInterceptor() {
        super(Phase.POST_INVOKE);
    }

    @Override
    public void handleMessage(Message message) {
        /* 
         * See line #783 in org.apache.cxf.endpoint.ClientImpl.java (CXF 2.3.2) --
         * on partial responses, the exchange's waiters will be not notified 
         * and thus keep waiting until a timeout occurs.
         */
        message.remove(Message.PARTIAL_RESPONSE_MESSAGE);
    }
}

