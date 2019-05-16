/*
 * Copyright 2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.ws;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * CXF interceptor that claims to understand WS-Security headers.
 *
 * @author Dmytro Rud
 */
public class WsSecurityUnderstandingInInterceptor extends AbstractSoapInterceptor {

    private static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String WSSE11_NS = "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd";
    private static final String ENC_NS = "http://www.w3.org/2001/04/xmlenc#";

    private static final Set<QName> UNDERSTOOD_HEADERS =
            new HashSet<>(Arrays.asList(
                    new QName(WSSE_NS, "Security"),
                    new QName(WSSE11_NS, "Security"),
                    new QName(ENC_NS, "EncryptedData"))
            );

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