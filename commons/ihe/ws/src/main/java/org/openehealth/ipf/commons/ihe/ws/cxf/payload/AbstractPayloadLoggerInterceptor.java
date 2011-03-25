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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import groovy.xml.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

import java.util.List;
import java.util.Map;

/**
 * Abstract CXF interceptor which logs pretty-printed SOAP payload.
 * @author Dmytro Rud
 */
abstract public class AbstractPayloadLoggerInterceptor extends AbstractPhaseInterceptor<Message> {
    private static transient Log LOG = LogFactory.getLog(AbstractPayloadLoggerInterceptor.class);

    private final String title;

    /**
     * Constructor.
     * @param phase
     *      interceptor phase.
     * @param title
     *      log item title.
     */
    public AbstractPayloadLoggerInterceptor(String phase, String title) {
        super(phase);
        this.title = title;
    }


    /**
     * Pretty-prints the given XML payload and HTTP headers into the log.
     * @param payload
     *      XML payload as string.
     * @param httpHeaders
     *      map containing HTTP message headers, may be <code>null</code>.
     */
    protected void doLog(String payload, Map<?, ?> httpHeaders) {
        if (! LOG.isDebugEnabled()) {
            return;
        }

        if (payload.charAt(0) != '<') {
            int pos = payload.indexOf("\n<");
            payload = payload.substring(pos + 1);
        }

        StringBuilder sb = new StringBuilder()
                .append("\n--------------- ")
                .append(title)
                .append(" ---------------\n");

        if (httpHeaders != null) {
            for (Map.Entry<?, ?> header : httpHeaders.entrySet()) {
                Object value = header.getValue();
                if (value instanceof List) {
                    value = ((List) value).get(0);
                }
                sb.append(header.getKey()).append(": ").append(value).append('\n');
            }
            sb.append('\n');
        }

        sb.append(XmlUtil.serialize(payload)).append("\n-------------------------------");
        LOG.debug(sb.toString());
    }
}
