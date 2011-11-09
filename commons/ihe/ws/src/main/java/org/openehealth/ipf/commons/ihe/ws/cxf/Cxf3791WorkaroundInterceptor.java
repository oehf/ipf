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
package org.openehealth.ipf.commons.ihe.ws.cxf;

import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapActionInInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.util.regex.Pattern;

/**
 * According to ITI TF CP-510, SOAP Action should be ignored.
 * CXF contains a direct contradiction to this requirement &mdash;
 * see <a href="https://issues.apache.org/jira/browse/CXF-3791">CXF-3791</a>.
 * <p>
 * This interceptor simply deletes the SOAP action from HTTP Content-Type header
 * and is intended for incoming chains on both client and server sides.
 *
 * @see SoapActionInInterceptor
 * @see <a href="http://www.w3.org/TR/2003/REC-soap12-part2-20030624/#ietf-action">SOAP 1.2 specification</a>
 *
 * @author Dmytro Rud
 */
public class Cxf3791WorkaroundInterceptor extends AbstractSoapInterceptor {
    static final Pattern PATTERN = Pattern.compile(";\\s*action\\s*=\\s*(\"?)[\\S&&[^\"]]+(\\1)\\s*");

    public Cxf3791WorkaroundInterceptor() {
        super(Phase.READ);
        addBefore(SoapActionInInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        if (message.getVersion() instanceof Soap12) {
            String s = (String) message.get(Message.CONTENT_TYPE);
            if (s != null) {
                s = PATTERN.matcher(s).replaceFirst("");
                message.put(Message.CONTENT_TYPE, s);
            }
        }
    }
}
