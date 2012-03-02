/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Interceptor which sets a context property to disable stopping collecting payload
 * in the {@link OutPayloadExtractorInterceptor}.
 *
 * @author Dmytro Rud
 */
public class DisablePayloadCollectingDeactivationInterceptor extends AbstractPhaseInterceptor<Message> {

    public DisablePayloadCollectingDeactivationInterceptor() {
        super(Phase.WRITE_ENDING);
        addBefore(OutPayloadExtractorInterceptor.class.getName());
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        message.setContextualProperty(
                OutPayloadExtractorInterceptor.PAYLOAD_COLLECTING_DEACTIVATION_ENABLED,
                Boolean.FALSE);
    }
}
