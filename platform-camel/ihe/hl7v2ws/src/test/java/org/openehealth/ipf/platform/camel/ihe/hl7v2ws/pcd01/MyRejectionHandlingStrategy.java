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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws.pcd01;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Exchange;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2ws.AbstractHl7v2WsRejectionHandlingStrategy;

import java.util.concurrent.atomic.AtomicInteger;

import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.extractOutgoingException;
import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.extractOutgoingPayload;

/**
 * Sample rejection handling strategy.
 * @author Dmytro Rud
 */
public class MyRejectionHandlingStrategy extends AbstractHl7v2WsRejectionHandlingStrategy {
    private static final Log LOG = LogFactory.getLog(MyRejectionHandlingStrategy.class);

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static void resetCounter() {
        COUNTER.set(0);
    }

    public static int getCount() {
        return COUNTER.get();
    }

    @Override
    public void handleRejectedExchange(Exchange cxfExchange) {
        StringBuilder sb = new StringBuilder()
                .append("Rejection strategy invocation # ")
                .append(COUNTER.getAndIncrement())
                .append(": request\n")
                .append(cxfExchange.getInMessage().getContent(StringPayloadHolder.class)
                        .get(StringPayloadHolder.PayloadType.SOAP_BODY))
                .append("\n lead to ");

        Exception exception = extractOutgoingException(cxfExchange);

        if (exception != null) {
            sb.append("SOAP Fault:");
        } else {
            sb.append("HL7v2 NAK:\n").append(extractOutgoingPayload(cxfExchange));
        }

        LOG.error(sb, exception);
    }
}
