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

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.Soap12FaultOutInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * CXF interceptor which provides the message with a property that is evaluated when creating fault details
 *
 * @author Christian Ohr
 * @since 4.1
 */
public class FaultMessageOutInterceptor extends AbstractSafeInterceptor {

    private final String faultMessage;

    public FaultMessageOutInterceptor(String faultMessage) {
        super(Phase.WRITE);
        addBefore(Soap12FaultOutInterceptor.class.getName());
        this.faultMessage = faultMessage;
    }

    @Override
    protected void process(SoapMessage message) {
        message.put("forced.faultstring", faultMessage);
    }
}
