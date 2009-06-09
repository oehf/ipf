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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.SoapUtils;


/**
 * CXF interceptor that claims to understand WS-Security headers.
 * 
 * @author Dmytro Rud
 */
public class WsSecurityUnderstandingInInterceptor extends AbstractSoapInterceptor implements SoapInterceptor {

    private static final Set<QName> UNDERSTOOD_HEADERS;
    static {
        UNDERSTOOD_HEADERS = new HashSet<QName>();
        for(String nsUri: SoapUtils.WS_SECURITY_NS_URIS) {
            UNDERSTOOD_HEADERS.add(new QName(nsUri, "Security"));
        }
    }
    
    /**
     * Constructs the interceptor.
     */
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
