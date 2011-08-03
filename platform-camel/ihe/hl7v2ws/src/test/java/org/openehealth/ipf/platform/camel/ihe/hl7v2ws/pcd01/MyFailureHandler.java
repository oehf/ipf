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
import org.openehealth.ipf.platform.camel.ihe.hl7v2ws.Hl7v2WsFailureHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static org.openehealth.ipf.platform.camel.ihe.hl7v2ws.Hl7v2WsFaultHandlerInterceptor.*;

/**
 * Sample failure handler.
 * @author Dmytro Rud
 */
public class MyFailureHandler implements Hl7v2WsFailureHandler {
    private static final Log LOG = LogFactory.getLog(MyFailureHandler.class);

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static void resetCounter() {
        COUNTER.set(0);
    }

    public static int getCount() {
        return COUNTER.get();
    }

    @Override
    public void handleFailedExchange(Exchange cxfExchange) {
        StringBuilder sb = new StringBuilder()
                .append("Handler invocation # ")
                .append(COUNTER.getAndIncrement())
                .append(": request\n")
                .append(cxfExchange.getInMessage().getContent(String.class))
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
