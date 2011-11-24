/*
 * Copyright 2011 the original author or authors.
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
import org.apache.cxf.ws.addressing.MAPAggregator;

/**
 * Workaround for the bug
 * <a href="https://issues.apache.org/jira/browse/CXF-3768">CXF-3768</a>:
 * set HTTP response code 202 for WS-Addressing asynchrony ACKs.
 * Intended for server-side deployment.
 *
 * @see <a href="http://www.ws-i.org/Profiles/BasicProfile-2_0%28WGD%29.html#Use_of_Non-Anonymous_Reponse_EPR_in_a_Request-Response_Operation">WS-I Basic Profile v.2.0, Section 3.8.6</a>.
 *
 * @author Dmytro Rud
 */
public class Cxf3768WorkaroundInterceptor extends AbstractPhaseInterceptor<Message> {

    public Cxf3768WorkaroundInterceptor() {
        super(Phase.PRE_LOGICAL);
        addAfter(MAPAggregator.class.getName());
    }

    @Override
    public void handleMessage(Message message) {
        if (Boolean.TRUE.equals(message.get(Message.PARTIAL_RESPONSE_MESSAGE))) {
            message.put(Message.RESPONSE_CODE, 202);
        }
    }
}
