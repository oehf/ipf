/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xua;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.opensaml.soap.wssecurity.WSSecurityConstants;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Set;

/**
 * CXF interceptor that claims to understand WS-Security headers.
 * @author Dmytro Rud
 */
public class WsSecurityUnderstandingInInterceptor extends AbstractSoapInterceptor {

    private static final Set<QName> UNDERSTOOD_HEADERS =
            Collections.singleton(new QName(WSSecurityConstants.WSSE_NS, "Security"));

    public WsSecurityUnderstandingInInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        // nop
    }

    @Override
    public Set<QName> getUnderstoodHeaders() {
        return UNDERSTOOD_HEADERS;
    }
}